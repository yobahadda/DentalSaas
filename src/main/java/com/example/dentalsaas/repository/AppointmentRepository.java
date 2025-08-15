package com.example.dentalsaas.repository;

import com.example.dentalsaas.entity.Appointment;
import com.example.dentalsaas.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Multi-tenant: Always filter by clinic
    List<Appointment> findByClinicId(Long clinicId);

    Page<Appointment> findByClinicId(Long clinicId, Pageable pageable);

    // Date range queries
    List<Appointment> findByClinicIdAndDateTimeBetween(Long clinicId,
                                                       LocalDateTime startDateTime,
                                                       LocalDateTime endDateTime);

    @Query("SELECT a FROM Appointment a WHERE a.clinic.id = :clinicId AND " +
            "DATE(a.dateTime) BETWEEN :startDate AND :endDate ORDER BY a.dateTime")
    List<Appointment> findByClinicIdAndDateBetween(@Param("clinicId") Long clinicId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    // Patient appointments
    List<Appointment> findByPatientIdOrderByDateTimeDesc(Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.clinic.id = :clinicId " +
            "ORDER BY a.dateTime DESC")
    List<Appointment> findByPatientIdAndClinicIdOrderByDateTimeDesc(@Param("patientId") Long patientId,
                                                                    @Param("clinicId") Long clinicId);

    // Dentist appointments
    List<Appointment> findByDentistIdAndDateTimeBetween(Long dentistId,
                                                        LocalDateTime start,
                                                        LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId AND a.clinic.id = :clinicId AND " +
            "a.dateTime BETWEEN :start AND :end ORDER BY a.dateTime")
    List<Appointment> findByDentistIdAndClinicIdAndDateTimeBetween(@Param("dentistId") Long dentistId,
                                                                   @Param("clinicId") Long clinicId,
                                                                   @Param("start") LocalDateTime start,
                                                                   @Param("end") LocalDateTime end);

    // Status-based queries
    List<Appointment> findByClinicIdAndStatus(Long clinicId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.clinic.id = :clinicId AND a.status = :status AND " +
            "DATE(a.dateTime) = :date ORDER BY a.dateTime")
    List<Appointment> findByClinicIdAndStatusAndDate(@Param("clinicId") Long clinicId,
                                                     @Param("status") AppointmentStatus status,
                                                     @Param("date") LocalDate date);

    // Today's appointments
    @Query("SELECT a FROM Appointment a WHERE a.clinic.id = :clinicId AND " +
            "DATE(a.dateTime) = CURRENT_DATE ORDER BY a.dateTime")
    List<Appointment> findTodayAppointmentsByClinic(@Param("clinicId") Long clinicId);

    // Upcoming appointments
    @Query("SELECT a FROM Appointment a WHERE a.clinic.id = :clinicId AND " +
            "a.dateTime >= CURRENT_TIMESTAMP AND a.status != 'CANCELLED' " +
            "ORDER BY a.dateTime")
    List<Appointment> findUpcomingAppointmentsByClinic(@Param("clinicId") Long clinicId);

    // Conflict detection
    @Query("SELECT a FROM Appointment a WHERE a.dentist.id = :dentistId AND " +
            "a.status != 'CANCELLED' AND " +
            "a.dateTime < :endDateTime AND a.dateTime >= :startDateTime")
    List<Appointment> findConflictingAppointments(@Param("dentistId") Long dentistId,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);


    // Security validation
    @Query("SELECT a FROM Appointment a WHERE a.id = :appointmentId AND a.clinic.id = :clinicId")
    Optional<Appointment> findByIdAndClinicId(@Param("appointmentId") Long appointmentId,
                                              @Param("clinicId") Long clinicId);

    // Statistics
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId")
    Long countAppointmentsByClinic(@Param("clinicId") Long clinicId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId AND a.status = :status")
    Long countAppointmentsByClinicAndStatus(@Param("clinicId") Long clinicId,
                                            @Param("status") AppointmentStatus status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId AND " +
            "DATE(a.dateTime) = CURRENT_DATE")
    Long countTodayAppointmentsByClinic(@Param("clinicId") Long clinicId);

    // Monthly statistics
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId AND " +
            "FUNCTION('YEAR', a.dateTime) = :year AND FUNCTION('MONTH', a.dateTime) = :month")
    Long countAppointmentsByClinicAndMonth(@Param("clinicId") Long clinicId,
                                           @Param("year") int year,
                                           @Param("month") int month);
}