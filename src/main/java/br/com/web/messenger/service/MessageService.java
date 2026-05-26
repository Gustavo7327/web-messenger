package br.com.web.messenger.service;

import br.com.web.messenger.entity.Message;
import br.com.web.messenger.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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


    public void markMessagesAsRead(Long senderId, Long recipientId) {
        List<Message> unreadMessages = messageRepository.findUnreadMessages(senderId, recipientId);

        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> msg.setMessageRead(true));
            messageRepository.saveAll(unreadMessages);
        }
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countUnreadMessagesByRecipient(userId);
    }
}
