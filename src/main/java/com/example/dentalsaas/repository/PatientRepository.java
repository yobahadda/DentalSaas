package com.example.dentalsaas.repository;

import com.example.dentalsaas.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Multi-tenant: Always filter by clinic
    List<Patient> findByClinicId(Long clinicId);

    Page<Patient> findByClinicId(Long clinicId, Pageable pageable);

    // Search functionality
    List<Patient> findByClinicIdAndFirstNameContainingIgnoreCase(Long clinicId, String firstName);

    List<Patient> findByClinicIdAndLastNameContainingIgnoreCase(Long clinicId, String lastName);

    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "p.phone LIKE CONCAT('%', :searchTerm, '%'))")
    List<Patient> searchPatients(@Param("clinicId") Long clinicId, @Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "p.phone LIKE CONCAT('%', :searchTerm, '%'))")
    Page<Patient> searchPatients(@Param("clinicId") Long clinicId,
                                 @Param("searchTerm") String searchTerm,
                                 Pageable pageable);

    // Security validation
    @Query("SELECT p FROM Patient p WHERE p.id = :patientId AND p.clinic.id = :clinicId")
    Optional<Patient> findByIdAndClinicId(@Param("patientId") Long patientId, @Param("clinicId") Long clinicId);

    // Birthday reminders
    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "FUNCTION('MONTH', p.birthDate) = FUNCTION('MONTH', :date) AND " +
            "FUNCTION('DAY', p.birthDate) = FUNCTION('DAY', :date)")
    List<Patient> findPatientsBirthdayToday(@Param("clinicId") Long clinicId, @Param("date") LocalDate date);

    // Age-based queries - VERSION CORRIGÉE
    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "p.birthDate <= :maxBirthDate AND p.birthDate >= :minBirthDate")
    List<Patient> findPatientsByAgeRange(@Param("clinicId") Long clinicId,
                                         @Param("minBirthDate") LocalDate minBirthDate,
                                         @Param("maxBirthDate") LocalDate maxBirthDate);

    // Alternative: Version avec requête native pour MySQL
    @Query(value = "SELECT * FROM patients p WHERE p.clinic_id = :clinicId AND " +
            "TIMESTAMPDIFF(YEAR, p.birth_date, CURDATE()) BETWEEN :minAge AND :maxAge",
            nativeQuery = true)
    List<Patient> findPatientsByAgeRangeNative(@Param("clinicId") Long clinicId,
                                               @Param("minAge") int minAge,
                                               @Param("maxAge") int maxAge);

    // Version simple pour les enfants (moins de 18 ans)
    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "p.birthDate > :eighteenYearsAgo")
    List<Patient> findMinorPatients(@Param("clinicId") Long clinicId,
                                    @Param("eighteenYearsAgo") LocalDate eighteenYearsAgo);

    // Version simple pour les adultes (18 ans et plus)
    @Query("SELECT p FROM Patient p WHERE p.clinic.id = :clinicId AND " +
            "p.birthDate <= :eighteenYearsAgo")
    List<Patient> findAdultPatients(@Param("clinicId") Long clinicId,
                                    @Param("eighteenYearsAgo") LocalDate eighteenYearsAgo);

    // Statistics
    @Query("SELECT COUNT(p) FROM Patient p WHERE p.clinic.id = :clinicId")
    Long countPatientsByClinic(@Param("clinicId") Long clinicId);

    boolean existsByEmailAndClinicId(String email, Long clinicId);

    boolean existsByPhoneAndClinicId(String phone, Long clinicId);
}