package br.com.web.messenger.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.web.messenger.dto.user.UserRegister;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    
    private String email;

    @JsonIgnore
    private String password;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String biography;

    @Column(nullable = true)
    private String photoUrl;

    @Column(nullable = true)
    private Boolean emailVerified;
 
    private Boolean active = true;

    @ElementCollection
    private List<String> profileLinks = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;


    public User() {
    }

    public User(UserRegister dto, BCryptPasswordEncoder passwordEncoder) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = passwordEncoder.encode(dto.password());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getProfileLinks() {
        return profileLinks;
    }

    public void setProfileLinks(List<String> profileLinks) {
        this.profileLinks = profileLinks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isCorrectPassword(String rawPassword, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(rawPassword, this.password);
    }

}
