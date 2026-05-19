package br.com.web.messenger.dto.group;

import br.com.web.messenger.entity.Group;

public record GroupResponse(String name, String description, String photoUrl) {

    public static GroupResponse from(Group group) {
        return new GroupResponse(group.getName(), group.getDescription(), group.getPhotoUrl());
    }
}
