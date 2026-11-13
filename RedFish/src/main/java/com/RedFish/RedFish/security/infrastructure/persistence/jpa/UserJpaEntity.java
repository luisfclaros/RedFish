package com.RedFish.RedFish.security.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import com.RedFish.RedFish.security.domain.model.Role;
import com.RedFish.RedFish.security.domain.model.User;
import jakarta.persistence.*;

@Entity
@Table(name="usuarios")
public class UserJpaEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String name;
    @Column(nullable=false, unique=true, length=60) private String username;
    @Column(nullable=false, length=100) private String passwordHash;
    @Column(nullable=false, length=30) private String roleName;
    @Column(nullable=false) private boolean active;
    @Column(nullable=false) private LocalDateTime createdAt;
    protected UserJpaEntity() {}
    private UserJpaEntity(Long id, String name, String username, String passwordHash,
            String roleName, boolean active, LocalDateTime createdAt) {
        this.id=id; this.name=name; this.username=username; this.passwordHash=passwordHash;
        this.roleName=roleName; this.active=active; this.createdAt=createdAt;
    }
    public static UserJpaEntity fromDomain(User u) {
        return new UserJpaEntity(u.id(), u.name(), u.username(), u.passwordHash(),
                u.role().name(), u.active(), u.createdAt());
    }
    public User toDomain() {
        return new User(id, name, username, passwordHash, new Role(null, roleName, roleName), active, createdAt);
    }
}
