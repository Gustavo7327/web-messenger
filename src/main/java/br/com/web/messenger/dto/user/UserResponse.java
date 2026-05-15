package br.com.web.messenger.dto.user;

import br.com.web.messenger.entity.User;

import java.util.List;

public record UserResponse(String name, String username, String bio, String photoUrl, List<String> profileLinks) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getName(), user.getUsername(), user.getBiography(), user.getPhotoUrl(), user.getProfileLinks());
    }
}
