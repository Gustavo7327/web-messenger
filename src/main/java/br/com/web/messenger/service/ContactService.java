package br.com.web.messenger.service;

import br.com.web.messenger.dto.contact.ContactListResponse;
import br.com.web.messenger.dto.contact.ContactResponse;
import br.com.web.messenger.dto.contact.SaveContact;
import br.com.web.messenger.entity.Contact;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.repository.jpa.ContactRepository;
import br.com.web.messenger.repository.jpa.UserRepository;
import org.springframework.security.access.AccessDeniedException;
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

    public ContactListResponse findAll(Authentication authentication) {
        String email = authentication.getName();
        List<ContactResponse> contacts = contactRepository.findContactResponsesByOwnerEmail(email);
        return new ContactListResponse(contacts);
    }

    public ContactResponse update(Long id, String nickname, Authentication authentication) {
        Optional<Contact> contactOpt = contactRepository.findById(id);

        if (contactOpt.isEmpty()) {
            throw new ResourceNotFoundException("Contact not found");
        }

        if (!contactOpt.get().getOwner().getEmail().equals(authentication.getName())) {
            throw new AccessDeniedException("Você não tem permissão para atualizar o contato");
        }

        Contact contact = contactOpt.get();
        contact.setNickname(nickname);
        contactRepository.save(contact);
        return ContactResponse.from(contact);
    }

    public ContactResponse save(SaveContact dto, Authentication authentication) {
        Optional<User> user = userRepository.findByUsername(dto.username());

        if (user.isEmpty()) {
           throw new ResourceNotFoundException("username", dto.username());
        }

        Optional<User> owner = userRepository.findByEmail(authentication.getName());
        if (owner.isEmpty()) {
            throw new ResourceNotFoundException("email", authentication.getName());
        }

        Contact contact = new Contact();
        contact.setNickname(dto.nickname());
        contact.setContact(user.get());
        contact.setOwner(owner.get());

        contactRepository.save(contact);
        return ContactResponse.from(contact);
    }

    public void delete(Long id, Authentication authentication) {
        Optional<Contact> contact = contactRepository.findById(id);

        if (contact.isEmpty()) {
            throw new ResourceNotFoundException("Contact not found");
        }

        if (!contact.get().getOwner().getEmail().equals(authentication.getName())) {
            throw new AccessDeniedException("Você não tem permissão para atualizar o contato");
        }

        contactRepository.delete(contact.get());
    }
}
