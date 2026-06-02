package br.com.web.messenger.controller;

import br.com.web.messenger.dto.group.CreateGroup;
import br.com.web.messenger.dto.group.GroupDetailsResponse;
import br.com.web.messenger.dto.group.UpdateGroup;
import br.com.web.messenger.dto.group.GroupResponse;
import br.com.web.messenger.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@RequestBody @Valid CreateGroup dto, Authentication authentication) {
        GroupResponse response = groupService.create(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateGroup dto,
            Authentication authentication) {
        GroupResponse response = groupService.update(id, dto, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getById(@PathVariable Long id) {
        GroupResponse response = groupService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GroupDetailsResponse> getGroupDetails(@PathVariable Long id) {
        GroupDetailsResponse response = groupService.getGroupDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups(Authentication authentication) {
        List<GroupResponse> groups = groupService.getAllGroups(authentication);
        return ResponseEntity.ok(groups);
    }
}