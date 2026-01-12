package br.com.web.messenger.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.web.messenger.dto.auth.LoginRequest;
import br.com.web.messenger.dto.auth.LoginResponse;
import br.com.web.messenger.dto.user.UserRegister;
import br.com.web.messenger.service.UserService;
import br.com.web.messenger.util.ValidationUtils;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
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
}
