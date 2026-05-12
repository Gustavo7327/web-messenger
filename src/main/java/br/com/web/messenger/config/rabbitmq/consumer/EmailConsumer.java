package br.com.web.messenger.config.rabbitmq.consumer;

import br.com.web.messenger.config.rabbitmq.RabbitMQConfig;
import br.com.web.messenger.constants.EmailType;
import br.com.web.messenger.dto.email.EmailNotification;
import br.com.web.messenger.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveEmailTask(EmailNotification dto) {

        try {
            switch (dto.emailType()) {
                case EMAIL_VERIFICATION -> emailService.sendVerificationEmail(dto.toEmail(), dto.userName(), dto.code());
                case EMAIL_ACTIVATION -> emailService.sendActivationEmail(dto.toEmail(), dto.userName(), dto.code());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar email: " + e.getMessage(), e);
        }
    }
}
