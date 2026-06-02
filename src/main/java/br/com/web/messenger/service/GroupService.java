package br.com.web.messenger.service;

import br.com.web.messenger.dto.group.*;
import br.com.web.messenger.entity.Group;
import br.com.web.messenger.entity.GroupRole;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.repository.jpa.GroupRepository;
import br.com.web.messenger.repository.jpa.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupResponse create(CreateGroup dto, Authentication authentication) {
        User owner = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Group group = new Group();
        group.setName(dto.name());
        group.setDescription(dto.description());
        group.setOwner(owner);

        group.addMember(owner, GroupRole.ADMIN);

        group = groupRepository.save(group);
        return GroupResponse.from(group);
    }

    @Transactional
    public GroupResponse update(Long groupId, UpdateGroup dto, Authentication authentication) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado"));

        if (!group.getOwner().getEmail().equals(authentication.getName())) {
            throw new AccessDeniedException("Você não tem permissão para alterar este grupo.");
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            group.setName(dto.name());
        }
        if (dto.description() != null) {
            group.setDescription(dto.description());
        }

        group = groupRepository.save(group);
        return GroupResponse.from(group);
    }

    @Transactional(readOnly = true)
    public GroupResponse getById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado"));

        return GroupResponse.from(group);
    }

    @Transactional(readOnly = true)
    public GroupDetailsResponse getGroupDetails(Long groupId) {
        GroupInfoDTO groupInfo = groupRepository.findGroupInfoById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado"));

        List<GroupMemberDTO> members = groupRepository.findGroupMembersById(groupId);

        return new GroupDetailsResponse(groupInfo, members);
    }

    public List<GroupResponse> getAllGroups(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return groupRepository.findAllGroupsByUserId(user.getId());
    }
}
