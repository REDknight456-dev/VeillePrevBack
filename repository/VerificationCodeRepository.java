package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmailAndCodeAndUsedFalse(String email, String code);
}