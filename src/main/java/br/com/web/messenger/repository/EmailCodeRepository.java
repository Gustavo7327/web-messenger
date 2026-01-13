package br.com.web.messenger.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.web.messenger.entity.EmailCode;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode, Long>{

    Optional<EmailCode> findByTokenAndCodeAndExpiresAtGreaterThan(String token, int code, LocalDateTime now);
    
}
