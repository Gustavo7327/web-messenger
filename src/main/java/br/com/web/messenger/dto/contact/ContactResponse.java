package br.com.web.messenger.dto.contact;

import br.com.web.messenger.entity.Contact;

import java.time.LocalDateTime;

public record ContactResponse(String nickname, LocalDateTime createdAt, Long id) {

    public static ContactResponse from(Contact contact) {
        return new ContactResponse(contact.getNickname(), contact.getCreatedAt(), contact.getId());
    }
}
