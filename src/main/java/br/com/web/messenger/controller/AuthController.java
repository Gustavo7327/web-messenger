package br.com.web.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.web.messenger.dto.auth.LoginRequest;
import br.com.web.messenger.dto.auth.LoginResponse;
import br.com.web.messenger.dto.auth.EmailRequest;
import br.com.web.messenger.dto.auth.VerifyEmailRequest;
import br.com.web.messenger.dto.user.UserRegister;
import br.com.web.messenger.service.EmailCodeService;
import br.com.web.messenger.service.UserService;
import br.com.web.messenger.util.ValidationUtils;

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
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegister dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String errors = ValidationUtils.joinFieldErrors(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }

        if (userService.emailExists(dto.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
        }

        userService.createUser(dto);

        try {
            emailCodeService.createEmailVerificationForEmail(dto.email());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar email de verificação");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errors = ValidationUtils.joinFieldErrors(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }

        LoginResponse response = userService.generateToken(loginRequest);

        return ResponseEntity.ok(response);    
    }


    @PostMapping("/verify-email/{token}")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid VerifyEmailRequest body, @PathVariable String token){
        int code;
        try {
            code = Integer.parseInt(body.code().trim());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Formato de código inválido");
        }

        boolean isVerified = emailCodeService.verifyEmailCode(token, code);
        if (isVerified) {
            return ResponseEntity.ok("Email verificado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de verificação inválido");
        }
    }


    @PostMapping("/verify-email/request")
    public ResponseEntity<String> requestVerification(@RequestBody @Valid EmailRequest body){
        String email = body.email();

        if (!userService.emailExists(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        emailCodeService.createEmailVerificationForEmail(email);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
