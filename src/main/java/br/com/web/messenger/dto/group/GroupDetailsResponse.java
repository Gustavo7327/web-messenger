package br.com.web.messenger.dto.group;

import java.util.List;

public record GroupDetailsResponse(
        GroupInfoDTO group,
        List<GroupMemberDTO> members
) {}
