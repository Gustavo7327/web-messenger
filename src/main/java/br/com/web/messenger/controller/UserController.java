package br.com.web.messenger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.web.messenger.dto.user.UserUpdate;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdate userUpdate,
            Authentication authentication) {
        
        User updatedUser = userService.updateUser(id, userUpdate, authentication.getName());
        return ResponseEntity.ok(updatedUser);
    }
    
}
