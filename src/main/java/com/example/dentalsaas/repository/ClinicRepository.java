package com.example.dentalsaas.repository;
import com.example.dentalsaas.entity.Clinic;
import com.example.dentalsaas.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ClinicRepository extends JpaRepository<Clinic,Long> {
    Optional<Clinic> findByEmail(String email);

    boolean existsByEmail(String email);
    @Query("SELECT c FROM Clinic c WHERE c.subscriptionPlan = :plan")
    List<Clinic> findBySubscriptionPlan(@Param("plan") SubscriptionPlan plan);

    @Query("SELECT COUNT(c) FROM Clinic c WHERE c.subscriptionPlan = :plan")
    Long countBySubscriptionPlan(@Param("plan") SubscriptionPlan plan);

}
