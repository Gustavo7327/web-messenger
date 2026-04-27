package br.com.web.messenger.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String operationalSystem;
    
    @Column(nullable = true)
    private String navigator;

    private String publicKey;

    private LocalDateTime lastAccess;

    @CreationTimestamp
    private LocalDateTime authorized_at;


    public Device() {
    }


    public Device(User user, String operationalSystem, String navigator, String publicKey, LocalDateTime lastAccess,
            LocalDateTime authorized_at) {
        this.user = user;
        this.operationalSystem = operationalSystem;
        this.navigator = navigator;
        this.publicKey = publicKey;
        this.lastAccess = lastAccess;
        this.authorized_at = authorized_at;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public String getOperationalSystem() {
        return operationalSystem;
    }


    public void setOperationalSystem(String operationalSystem) {
        this.operationalSystem = operationalSystem;
    }


    public String getNavigator() {
        return navigator;
    }


    public void setNavigator(String navigator) {
        this.navigator = navigator;
    }


    public String getPublicKey() {
        return publicKey;
    }


    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    public LocalDateTime getLastAccess() {
        return lastAccess;
    }


    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }


    public LocalDateTime getAuthorized_at() {
        return authorized_at;
    }


    public void setAuthorized_at(LocalDateTime authorized_at) {
        this.authorized_at = authorized_at;
    }

    
    
}
