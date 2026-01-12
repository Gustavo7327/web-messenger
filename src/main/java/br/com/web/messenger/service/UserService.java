package br.com.web.messenger.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.web.messenger.dto.auth.LoginRequest;
import br.com.web.messenger.dto.auth.LoginResponse;
import br.com.web.messenger.dto.user.UserRegister;
import br.com.web.messenger.entity.User;
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
    

    public void createUser(UserRegister dto){
        User user = new User(dto, passwordEncoder);
        userRepository.save(user);
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

}
