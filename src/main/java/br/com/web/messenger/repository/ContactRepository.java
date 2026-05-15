package br.com.web.messenger.repository;

import br.com.web.messenger.dto.contact.ContactResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.web.messenger.entity.Contact;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

    @Query("SELECT new br.com.web.messenger.dto.contact.ContactResponse(c.nickname, c.createdAt, c.id) " +
            "FROM Contact c WHERE c.owner.email = :email")
    List<ContactResponse> findContactResponsesByOwnerEmail(@Param("email") String email);
}
