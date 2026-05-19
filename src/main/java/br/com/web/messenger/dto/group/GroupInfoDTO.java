package br.com.web.messenger.dto.group;

public record GroupInfoDTO(
        Long id,
        String name,
        String description,
        String photoUrl,
        String ownerEmail
) {}