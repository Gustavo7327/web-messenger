
package br.com.web.messenger.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.web.messenger.entity.EmailCode;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.repository.EmailCodeRepository;
import br.com.web.messenger.repository.UserRepository;

@Service
public class EmailCodeService {
    
    private final EmailCodeRepository emailCodeRepository;

    private final UserRepository userRepository;

    public EmailCodeService(EmailCodeRepository emailCodeRepository, UserRepository userRepository){
        this.emailCodeRepository = emailCodeRepository;
        this.userRepository = userRepository;
    }
    

    public String createEmailVerificationForEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        User user = userOpt.get();
        int code = new Random().nextInt(900000) + 100000; // 6-digit code
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        EmailCode emailCode = new EmailCode(user, code, token, "EMAIL_VERIFICATION", expiresAt);
        emailCodeRepository.save(emailCode);

        sendVerificationEmail(user, token, code);

        return token;
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


    private void sendVerificationEmail(User user, String token, int code) {
        System.out.println("[email] to=" + user.getEmail() + " token=" + token + " code=" + code);
    }
}
