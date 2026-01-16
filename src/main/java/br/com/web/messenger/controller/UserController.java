package br.com.web.messenger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.web.messenger.dto.auth.EmailRequest;
import br.com.web.messenger.dto.auth.VerifyEmailRequest;
import br.com.web.messenger.dto.user.UserUpdate;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.service.EmailCodeService;
import br.com.web.messenger.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    private final EmailCodeService emailCodeService;

    public UserController(UserService userService, EmailCodeService emailCodeService){
        this.userService = userService;
        this.emailCodeService = emailCodeService;
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdate userUpdate,
            Authentication authentication) {
        
        User updatedUser = userService.updateUser(id, userUpdate, authentication.getName());
        return ResponseEntity.ok(updatedUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id, Authentication authentication) {
        userService.deactivateUser(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/activate")
    public ResponseEntity<Void> activateUser(@RequestBody EmailRequest request) {
        emailCodeService.createActiveUserCode(request.email());
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/activate/{token}")
    public ResponseEntity<Void> verifyActivateUser(@RequestBody @Valid VerifyEmailRequest body, @PathVariable String token) {
        int code;
        try {
            code = Integer.parseInt(body.code().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Formato de código inválido");
        }
        boolean isVerified = emailCodeService.verifyActiveUserCode(token, code);

        if (!isVerified) {
            throw new IllegalArgumentException("Código de verificação inválido");
        }

        return ResponseEntity.ok().build();
    }
}
