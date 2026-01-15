
package br.com.web.messenger.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.web.messenger.entity.EmailCode;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.repository.EmailCodeRepository;
import br.com.web.messenger.repository.UserRepository;

@Service
public class EmailCodeService {
    
    private final EmailCodeRepository emailCodeRepository;

    private final UserRepository userRepository;
    
    @Autowired
    private final EmailService emailService;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailCodeService(EmailCodeRepository emailCodeRepository, UserRepository userRepository, EmailService emailService) {
        this.emailCodeRepository = emailCodeRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    

    public void createEmailVerificationForEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        User user = userOpt.get();
        int code = new Random().nextInt(900000) + 100000; 
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        EmailCode emailCode = new EmailCode(user, code, token, "EMAIL_VERIFICATION", expiresAt);
        emailCodeRepository.save(emailCode);

        emailService.sendVerificationEmail(user, token, code, baseUrl);
    }


    public boolean verifyEmailCode(String token, int code) {
        Optional<EmailCode> opt = emailCodeRepository.findByTokenAndCodeAndExpiresAtGreaterThan(token, code, LocalDateTime.now());
        if (opt.isEmpty()) {
            return false;
        }

        EmailCode emailCode = opt.get();
        User user = emailCode.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        emailCodeRepository.delete(emailCode);

        return true;
    }

}
