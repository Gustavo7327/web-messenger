package br.com.web.messenger.dto.contact;

import java.util.List;

public record ContactListResponse(List<ContactResponse> contacts) {
}
