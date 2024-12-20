package com.creditsystem.repository;

import com.creditsystem.entity.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditApplicationRepository  extends JpaRepository<CreditApplication, Long> {
    Optional<CreditApplication> findByNationalId(String nationalId);
}
