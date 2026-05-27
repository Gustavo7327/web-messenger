package br.com.web.messenger.service;

import br.com.web.messenger.entity.Notification;
import br.com.web.messenger.entity.User;
import br.com.web.messenger.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Transactional
    public Notification createNotification(User user, String title, String description) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setDescription(description);

        return notificationRepository.save(notification);
    }


    public Page<Notification> getUserNotifications(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageRequest);
    }


    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }


    @Transactional
    public void clearAllUserNotifications(Long userId) {
        notificationRepository.deleteAllByUserId(userId);
    }
}