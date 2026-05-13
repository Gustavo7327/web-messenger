package br.com.web.messenger.service;

import br.com.web.messenger.dto.contact.SaveContact;
import br.com.web.messenger.entity.Contact;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.repository.ContactRepository;
import br.com.web.messenger.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> findAll(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return contactRepository.findAllByOwnerId(user.getId());
    }

    public Contact update(Long id, String nickname) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) {
            throw new ResourceNotFoundException("Contact not found");
        }
        contact.get().setNickname(nickname);
        return contactRepository.save(contact.get());
    }

    public Contact save(SaveContact dto, Authentication authentication) {
        Optional<User> user = userRepository.findByUsername(dto.username());

        if (user.isEmpty()) {
           throw new ResourceNotFoundException("username", dto.username());
        }

        Contact contact = new Contact();
        contact.setNickname(dto.nickname());
        contact.setContact(user.get());
        contact.setOwner((User) authentication.getPrincipal());

        return contactRepository.save(contact);
    }

    public void delete(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) {
            throw new ResourceNotFoundException("Contact not found");
        }
        contactRepository.delete(contact.get());
    }
}
