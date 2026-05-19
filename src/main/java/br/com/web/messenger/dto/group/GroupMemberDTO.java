package br.com.web.messenger.dto.group;

public record GroupMemberDTO(
        Long memberId,
        String name,
        String photoUrl,
        String role
) {}
