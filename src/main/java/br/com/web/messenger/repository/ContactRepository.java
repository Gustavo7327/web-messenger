package br.com.web.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.web.messenger.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
    
}
