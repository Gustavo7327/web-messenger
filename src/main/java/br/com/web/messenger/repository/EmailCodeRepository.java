package br.com.web.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.web.messenger.entity.EmailCode;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode, Long>{
    
}
