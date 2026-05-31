package br.com.web.messenger.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.web.messenger.entity.UserBlocked;

public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long>{
    
}
