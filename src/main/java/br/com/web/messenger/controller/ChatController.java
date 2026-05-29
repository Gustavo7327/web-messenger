package br.com.web.messenger.controller;

import br.com.web.messenger.entity.Message;
import br.com.web.messenger.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    public void processMessage(Message message, Authentication authentication) {

        Long senderId = (Long) authentication.getCredentials();

        message.setSenderId(senderId);

        Message savedMessage = messageService.saveMessage(message);

        if (savedMessage.getGroupId() != null) {

            messagingTemplate.convertAndSend("/topic/group." + savedMessage.getGroupId(), savedMessage);

        } else if (savedMessage.getRecipientId() != null) {

            String recipientIdString = savedMessage.getRecipientId().toString();

            messagingTemplate.convertAndSendToUser(
                    recipientIdString,
                    "/queue/feed",
                    savedMessage
            );
        }
    }
}