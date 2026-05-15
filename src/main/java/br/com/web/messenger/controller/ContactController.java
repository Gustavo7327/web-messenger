package br.com.web.messenger.controller;

import br.com.web.messenger.dto.contact.ContactListResponse;
import br.com.web.messenger.dto.contact.ContactResponse;
import br.com.web.messenger.dto.contact.SaveContact;
import br.com.web.messenger.dto.contact.UpdateContact;
import br.com.web.messenger.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public ResponseEntity<ContactListResponse> findAll(Authentication authentication) {
        return ResponseEntity.ok(contactService.findAll(authentication));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateContact dto) {
        return ResponseEntity.ok(contactService.update(id, dto.nickname()));
    }

    @PostMapping("/")
    public ResponseEntity<ContactResponse> save(@Valid @RequestBody SaveContact dto, Authentication authentication) {
        return ResponseEntity.ok(contactService.save(dto, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
