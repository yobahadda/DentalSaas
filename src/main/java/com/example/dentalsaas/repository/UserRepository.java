package com.example.dentalsaas.repository;

import com.example.dentalsaas.entity.Role;
import com.example.dentalsaas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Multi-tenant: Always filter by clinic
    List<User> findByClinicId(Long clinicId);

    List<User> findByClinicIdAndRole(Long clinicId, Role role);

    @Query("SELECT u FROM User u WHERE u.clinic.id = :clinicId AND u.role = 'DENTIST'")
    List<User> findDentistsByClinic(@Param("clinicId") Long clinicId);

    @Query("SELECT u FROM User u WHERE u.clinic.id = :clinicId AND u.role = 'ASSISTANT'")
    List<User> findAssistantsByClinic(@Param("clinicId") Long clinicId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.clinic.id = :clinicId")
    Long countUsersByClinic(@Param("clinicId") Long clinicId);

    // For security - validate user belongs to clinic
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.clinic.id = :clinicId")
    Optional<User> findByIdAndClinicId(@Param("userId") Long userId, @Param("clinicId") Long clinicId);

    boolean existsByEmailAndClinicId(String email, Long clinicId);

    }
