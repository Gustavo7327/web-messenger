package br.com.web.messenger.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.web.messenger.dto.auth.LoginRequest;
import br.com.web.messenger.dto.auth.LoginResponse;
import br.com.web.messenger.dto.user.UserRegister;
import br.com.web.messenger.dto.user.UserUpdate;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }
    

    public User createUser(UserRegister dto){
        User user = new User(dto, passwordEncoder);
        return userRepository.save(user);
    }


    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }


    public LoginResponse generateToken(LoginRequest loginRequest) {
        Optional<User> user = findByEmail(loginRequest.email());
        if (user.isEmpty() || !user.get().isCorrectPassword(loginRequest.password(), passwordEncoder)) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        Instant now = Instant.now();

        long expirationTime = 604800L;

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("web-messenger")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationTime))
            .subject(user.get().getEmail())
            .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(token, expirationTime);
    }


    public User updateUser(Long id, UserUpdate userUpdate, String authenticatedEmail) {
        Optional<User> userOpt = userRepository.findById(id);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        User user = userOpt.get();
        
        if (!user.getEmail().equals(authenticatedEmail)) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este usuário");
        }
        
        if (userUpdate.name() != null && !userUpdate.name().isBlank()) {
            user.setName(userUpdate.name());
        }
        
        if (userUpdate.password() != null && !userUpdate.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdate.password()));
        }
        
        if (userUpdate.bio() != null && !userUpdate.bio().isBlank()) {
            user.setBiography(userUpdate.bio());
        }
        
        if (userUpdate.links() != null && !userUpdate.links().isEmpty()) {
            user.setProfileLinks(userUpdate.links());
        }
        
        return userRepository.save(user);
    }


    public User findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return user;
    }

    
    public boolean isActiveUser(String email){
        Optional<User> userOpt = findByEmail(email);
        if(userOpt.isPresent() && !userOpt.get().isActive()){
            return false;
        }
        return userOpt.get().isActive();
    }

    
    public void deactivateUser(Long id, String authenticatedEmail) {
        User user = findById(id);
        if (!user.getEmail().equals(authenticatedEmail)) {
            throw new AccessDeniedException("Você não tem permissão para desativar este usuário");     
        }
        user.setActive(false);
        userRepository.save(user);
    }


}
