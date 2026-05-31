
package br.com.web.messenger.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import br.com.web.messenger.config.rabbitmq.producer.EmailProducer;
import br.com.web.messenger.constants.EmailType;
import br.com.web.messenger.dto.email.EmailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.web.messenger.entity.EmailCode;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.repository.jpa.EmailCodeRepository;
import br.com.web.messenger.repository.jpa.UserRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailCodeService {
    
    private final EmailCodeRepository emailCodeRepository;

    private final UserRepository userRepository;
    
    @Autowired
    private final EmailProducer emailProducer;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailCodeService(EmailCodeRepository emailCodeRepository, UserRepository userRepository, EmailProducer emailProducer) {
        this.emailCodeRepository = emailCodeRepository;
        this.userRepository = userRepository;
        this.emailProducer = emailProducer;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void createEmailVerificationForEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        User user = userOpt.get();
        int code = new Random().nextInt(900000) + 100000;
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        EmailCode emailCode = new EmailCode(user, code, EmailType.EMAIL_VERIFICATION.name(), expiresAt);
        emailCodeRepository.save(emailCode);

        emailProducer.sendEmailTask(new EmailNotification(email, user.getName(), code, EmailType.EMAIL_VERIFICATION));
    }


    public boolean verifyEmailCode(String email, int code) {
        Optional<EmailCode> opt = emailCodeRepository.findByUserEmailAndCodeAndExpiresAtGreaterThan(email, code, LocalDateTime.now());
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


    public void createActiveUserCode(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        User user = userOpt.get();
        int code = new Random().nextInt(900000) + 100000;
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        EmailCode emailCode = new EmailCode(user, code, EmailType.EMAIL_ACTIVATION.name(), expiresAt);
        emailCodeRepository.save(emailCode);

        emailProducer.sendEmailTask(new EmailNotification(email, user.getName(), code, EmailType.EMAIL_ACTIVATION));
    }


    public boolean verifyActiveUserCode(String email, int code) {
        Optional<EmailCode> opt = emailCodeRepository.findByUserEmailAndCodeAndExpiresAtGreaterThan(email, code, LocalDateTime.now());
        if (opt.isEmpty() || !opt.get().getType().equals("EMAIL_ACTIVATION")) {
            return false;
        }

        EmailCode emailCode = opt.get();
        User user = emailCode.getUser();
        user.setActive(true);
        userRepository.save(user);

        emailCodeRepository.delete(emailCode);

        return true;
    }

}
