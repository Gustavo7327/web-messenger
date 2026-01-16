package br.com.web.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.web.messenger.dto.auth.LoginRequest;
import br.com.web.messenger.dto.auth.LoginResponse;
import br.com.web.messenger.dto.auth.EmailRequest;
import br.com.web.messenger.dto.auth.VerifyEmailRequest;
import br.com.web.messenger.dto.user.UserRegister;
import br.com.web.messenger.exceptions.ConflictException;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.service.EmailCodeService;
import br.com.web.messenger.service.UserService;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    
    private final UserService userService;

    private final EmailCodeService emailCodeService;

    public AuthController(UserService userService, EmailCodeService emailCodeService){
        this.userService = userService;
        this.emailCodeService = emailCodeService;
    }
    

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegister dto) {
        if (userService.emailExists(dto.email())) {
            throw new ConflictException("Usuário", "email", dto.email());
        }

        userService.createUser(dto);

        try {
            emailCodeService.createEmailVerificationForEmail(dto.email());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Erro ao enviar email de verificação", ex);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        if(!userService.isActiveUser(loginRequest.email())){
            throw new ResourceNotFoundException("Usuário ativo", loginRequest.email());
        }
        LoginResponse response = userService.generateToken(loginRequest);
        return ResponseEntity.ok(response);    
    }


    @PostMapping("/verify-email/{token}")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid VerifyEmailRequest body, @PathVariable String token) {
        int code;
        try {
            code = Integer.parseInt(body.code().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Formato de código inválido");
        }

        boolean isVerified = emailCodeService.verifyEmailCode(token, code);
        if (!isVerified) {
            throw new IllegalArgumentException("Código de verificação inválido");
        }

        return ResponseEntity.ok().build();
    }


    @PostMapping("/verify-email/request")
    public ResponseEntity<Void> requestVerification(@RequestBody @Valid EmailRequest body) {
        String email = body.email();

        if (!userService.emailExists(email)) {
            throw new ResourceNotFoundException("Usuário", email);
        }

        emailCodeService.createEmailVerificationForEmail(email);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
