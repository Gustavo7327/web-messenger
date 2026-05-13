package br.com.web.messenger.controller;

import br.com.web.messenger.dto.contact.SaveContact;
import br.com.web.messenger.entity.Contact;
import br.com.web.messenger.repository.ContactRepository;
import br.com.web.messenger.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Contact>> findAll(Authentication authentication) {
        return ResponseEntity.ok(contactService.findAll(authentication));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Contact> update(@PathVariable Long id, @RequestBody String nickname) {
        return ResponseEntity.ok(contactService.update(id, nickname));
    }

    @PostMapping("/")
    public ResponseEntity<Contact> save(@Valid @RequestBody SaveContact dto, Authentication authentication) {
        return ResponseEntity.ok(contactService.save(dto, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
