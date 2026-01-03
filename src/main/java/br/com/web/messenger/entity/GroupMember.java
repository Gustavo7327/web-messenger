package br.com.web.messenger.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id = new GroupMemberId();

    @ManyToOne
    @MapsId("groupId") 
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("userId") 
    @JoinColumn(name = "member_id") 
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GroupRole role = GroupRole.MEMBER; 

    @CreationTimestamp
    private LocalDateTime joinedAt;

    public GroupMember() {}

    public GroupMember(Group group, User user, GroupRole role) {
        this.group = group;
        this.user = user;
        this.role = role;
        this.id = new GroupMemberId(group.getId(), user.getId());
    }

    public GroupMemberId getId() { return id; }
    public void setId(GroupMemberId id) { this.id = id; }
    
    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public GroupRole getRole() { return role; }
    public void setRole(GroupRole role) { this.role = role; }
    
    public LocalDateTime getJoinedAt() { return joinedAt; }
}