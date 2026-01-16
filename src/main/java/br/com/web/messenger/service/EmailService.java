package br.com.web.messenger.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.web.messenger.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private final JavaMailSender javaMailSender;
     
    private final TemplateEngine templateEngine;
    
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(User user, String token, int code, String baseUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("Verificação de Email - Web Messenger");
            
            Context context = new Context();
            context.setVariable("userName", user.getName());
            context.setVariable("code", String.format("%06d", code));
            context.setVariable("verificationLink", baseUrl + "/verify-email/" + token);
            
            String htmlContent = templateEngine.process("email-verification", context);
            
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            
        } catch (MessagingException ex) {
            throw new RuntimeException("Erro ao enviar email de verificação: " + ex.getMessage(), ex);
        }
    }

    public void sendActivationEmail(User user, String token, int code, String baseUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("Ativação de Conta - Web Messenger");
            
            Context context = new Context();
            context.setVariable("userName", user.getName());
            context.setVariable("code", String.format("%06d", code));
            context.setVariable("activationLink", baseUrl + "/api/users/activate/" + token);
            
            String htmlContent = templateEngine.process("account-activation", context);
            
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            
        } catch (MessagingException ex) {
            throw new RuntimeException("Erro ao enviar email de ativação: " + ex.getMessage(), ex);
        }
    }
}
