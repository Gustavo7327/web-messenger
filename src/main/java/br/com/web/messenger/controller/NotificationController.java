package br.com.web.messenger.controller;

import br.com.web.messenger.entity.Notification;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.exceptions.ResourceNotFoundException;
import br.com.web.messenger.service.NotificationService;
import br.com.web.messenger.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Page<Notification>> getMyNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Long currentUserId = extractUserIdFromAuthentication(authentication);

        Page<Notification> notifications = notificationService.getUserNotifications(currentUserId, page, size);

        return ResponseEntity.ok(notifications);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearMyNotifications(Authentication authentication) {
        Long currentUserId = extractUserIdFromAuthentication(authentication);
        notificationService.clearAllUserNotifications(currentUserId);
        return ResponseEntity.noContent().build();
    }


    private Long extractUserIdFromAuthentication(Authentication authentication) {
        Optional<User> user = userService.findByEmail(authentication.getName());
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new ResourceNotFoundException("User not found");
    }
}