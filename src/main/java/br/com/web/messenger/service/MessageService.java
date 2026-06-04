package br.com.web.messenger.service;

import br.com.web.messenger.entity.Message;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.repository.jpa.UserRepository;
import br.com.web.messenger.repository.mongo.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }


    public Message saveMessage(Message message) {
        if (message.getGroupId() == null && message.getRecipientId() == null) {
            throw new IllegalArgumentException("A mensagem deve ter um destinatário (usuário ou grupo).");
        }

        message.setMessageRead(false);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }


    public Page<Message> getDirectMessagesHistory(Long userId1, Long userId2, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return messageRepository.findDirectMessages(userId1, userId2, pageRequest);
    }


    public Page<Message> getGroupMessagesHistory(Long groupId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return messageRepository.findByGroupId(groupId, pageRequest);
    }


    public void markMessagesAsRead(Long senderId, Long recipientId, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (user.getId().equals(senderId)) {
            throw new IllegalArgumentException("Você não pode marcar como lido");
        }

        if (!user.getId().equals(recipientId)) {
            throw new IllegalArgumentException("Você não tem permissão de acesso");
        }

        List<Message> unreadMessages = messageRepository.findUnreadMessages(senderId, recipientId);

        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> msg.setMessageRead(true));
            messageRepository.saveAll(unreadMessages);
        }
    }


    public long getUnreadCount(Long userId) {
        return messageRepository.countUnreadMessagesByRecipient(userId);
    }

    public List<Message> getUnreadMessages(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return messageRepository.findUnreadMessagesByRecipient(user.getId());
    }
}
