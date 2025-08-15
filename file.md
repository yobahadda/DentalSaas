# Plan de Développement SaaS Dentaire - Spring Boot

## Phase 1 : Configuration du projet et base

### Étape 1.1 : Initialisation du projet
```bash
# Créer le projet sur Spring Initializr ou avec CLI
spring init --dependencies=web,data-jpa,security,validation,mysql,redis dental-saas
```

**Dépendances principales à inclure :**
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Boot Validation
- MySQL Driver
- Redis
- Spring Boot DevTools
- Lombok

### Étape 1.2 : Structure du projet
```
src/main/java/com/dentalsaas/
├── config/          # Configurations (Security, Database, Redis)
├── controller/      # Controllers REST
├── service/         # Logique métier
├── repository/      # Accès données
├── entity/          # Entités JPA
├── dto/             # Data Transfer Objects
├── exception/       # Gestion des exceptions
├── security/        # JWT, Auth
├── util/           # Utilitaires
└── DentalSaasApplication.java
```

### Étape 1.3 : Configuration de base
**application.yml :**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dental_saas?createDatabaseIfNotExist=true
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  redis:
    host: localhost
    port: 6379

server:
  port: 8080

jwt:
  secret: mySecretKey
  expiration: 86400
```

## Phase 2 : Entités et Base de Données

### Étape 2.1 : Créer les entités principales
```java
// 1. Clinic.java
@Entity
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
    private List<User> users;
    
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
    private List<Patient> patients;
}

// 2. User.java (Dentistes, assistants)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}

// 3. Patient.java
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}

// 4. Appointment.java
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private String notes;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "dentist_id")
    private User dentist;
    
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}
```

### Étape 2.2 : Créer les enums
```java
public enum Role {
    ADMIN, DENTIST, ASSISTANT
}

public enum SubscriptionPlan {
    BASIC, PREMIUM, ENTERPRISE
}

public enum AppointmentStatus {
    SCHEDULED, CONFIRMED, COMPLETED, CANCELLED
}
```

## Phase 3 : Sécurité et Authentification

### Étape 3.1 : Configuration JWT
```java
// JwtUtil.java
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(UserDetails userDetails) {
        // Implémentation JWT
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Validation JWT
    }
}

// SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

## Phase 4 : Repositories

### Étape 4.1 : Créer les repositories
```java
@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByEmail(String email);
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByClinicId(Long clinicId);
}

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByClinicId(Long clinicId);
    List<Patient> findByClinicIdAndFirstNameContainingIgnoreCase(Long clinicId, String firstName);
}

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClinicId(Long clinicId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDentistIdAndDateTimeBetween(Long dentistId, LocalDateTime start, LocalDateTime end);
}
```

## Phase 5 : Services (Logique Métier)

### Étape 5.1 : Services principaux
```java
@Service
@Transactional
public class AuthService {
    
    public JwtResponse login(LoginRequest request) {
        // Authentification et génération JWT
    }
    
    public void registerClinic(ClinicRegistrationRequest request) {
        // Inscription nouveau cabinet
    }
}

@Service
@Transactional
public class PatientService {
    
    public List<PatientDto> getPatientsByClinic(Long clinicId) {
        // Récupération patients par cabinet
    }
    
    public PatientDto createPatient(CreatePatientRequest request, Long clinicId) {
        // Création nouveau patient
    }
    
    public PatientDto updatePatient(Long patientId, UpdatePatientRequest request) {
        // Mise à jour patient
    }
}

@Service
@Transactional
public class AppointmentService {
    
    public List<AppointmentDto> getAppointmentsByDateRange(Long clinicId, LocalDate start, LocalDate end) {
        // Rendez-vous par période
    }
    
    public AppointmentDto scheduleAppointment(CreateAppointmentRequest request) {
        // Planification rendez-vous
    }
    
    public void cancelAppointment(Long appointmentId) {
        // Annulation rendez-vous
    }
}
```

## Phase 6 : Controllers (APIs REST)

### Étape 6.1 : Controllers principaux
```java
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        // Endpoint connexion
    }
    
    @PostMapping("/register-clinic")
    public ResponseEntity<String> registerClinic(@Valid @RequestBody ClinicRegistrationRequest request) {
        // Endpoint inscription cabinet
    }
}

@RestController
@RequestMapping("/api/patients")
@PreAuthorize("hasRole('DENTIST') or hasRole('ASSISTANT')")
public class PatientController {
    
    @GetMapping
    public ResponseEntity<List<PatientDto>> getPatients() {
        // Liste patients du cabinet
    }
    
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        // Création patient
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id) {
        // Détails patient
    }
}

@RestController
@RequestMapping("/api/appointments")
@PreAuthorize("hasRole('DENTIST') or hasRole('ASSISTANT')")
public class AppointmentController {
    
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAppointments(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // Rendez-vous par période
    }
    
    @PostMapping
    public ResponseEntity<AppointmentDto> scheduleAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        // Planification
    }
}
```

## Phase 7 : DTOs et Validation

### Étape 7.1 : Créer les DTOs
```java
// Requests
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6)
    private String password;
}

public class CreatePatientRequest {
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @Past
    private LocalDate birthDate;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]+$")
    private String phone;
}

// Responses
@Data
public class PatientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private int age;
}
```

## Phase 8 : Gestion des Exceptions

### Étape 8.1 : Exception Handler global
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Gestion erreurs validation
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        // Gestion accès refusé
    }
}
```

## Phase 9 : Tests

### Étape 9.1 : Tests unitaires et d'intégration
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class PatientServiceTest {
    
    @Test
    void shouldCreatePatientSuccessfully() {
        // Test création patient
    }
    
    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        // Test cas d'erreur
    }
}

@WebMvcTest(PatientController.class)
class PatientControllerTest {
    
    @Test
    void shouldReturnPatientsListWhenValidRequest() throws Exception {
        // Test endpoint
    }
}
```

## Phase 10 : Déploiement et Configuration Production

### Étape 10.1 : Docker et déploiement
```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim
COPY target/dental-saas-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/dental_saas
      
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: dental_saas
      MYSQL_ROOT_PASSWORD: password
    ports:
      - "3306:3306"
      
  redis:
    image: redis:alpine
    ports:
      - "6379:6379"
```

## Ordre de développement recommandé :

1. **Semaine 1-2** : Phases 1-2 (Setup + Entités)
2. **Semaine 3** : Phase 3 (Sécurité)
3. **Semaine 4** : Phase 4 (Repositories)
4. **Semaine 5-6** : Phase 5 (Services)
5. **Semaine 7** : Phase 6 (Controllers)
6. **Semaine 8** : Phase 7 (DTOs)
7. **Semaine 9** : Phase 8 (Exceptions)
8. **Semaine 10** : Phase 9 (Tests)
9. **Semaine 11** : Phase 10 (Déploiement)

## Points clés à retenir :

- **Multi-tenancy** : Chaque requête doit être filtrée par `clinic_id`
- **Sécurité** : JWT + filtrage par rôle + tenant
- **Validation** : Utiliser Bean Validation partout
- **Tests** : TDD recommandé, minimum 80% coverage
- **Documentation** : Swagger/OpenAPI pour les APIs