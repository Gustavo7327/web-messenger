package br.com.web.messenger.controller;

import br.com.web.messenger.dto.message.MessageDirect;
import br.com.web.messenger.entity.Message;
import br.com.web.messenger.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Message>> getAllMessagesUnread(Authentication authentication) {
        return ResponseEntity.ok(messageService.getUnreadMessages(authentication));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markReadMessages(Authentication authentication, @RequestBody MessageDirect dto) {
        messageService.markMessagesAsRead(dto.senderId(), dto.recipientId(), authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/direct-history")
    public ResponseEntity<Page<Message>> getMessagesDirectHistory(Authentication authentication, @RequestBody MessageDirect dto, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(messageService.getDirectMessagesHistory(dto.senderId(), dto.recipientId(), page, size));
    }

    @GetMapping("/group/{id}/direct-history")
    public ResponseEntity<Page<Message>> getGroupMessagesHistory(Authentication authentication, @PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(messageService.getGroupMessagesHistory(id, page, size));
    }

}
