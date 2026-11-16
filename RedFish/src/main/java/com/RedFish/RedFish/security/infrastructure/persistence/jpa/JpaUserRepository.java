package com.RedFish.RedFish.security.infrastructure.persistence.jpa;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.RedFish.RedFish.security.application.port.UserRepositoryPort;
import com.RedFish.RedFish.security.domain.model.User;

@Repository
public class JpaUserRepository implements UserRepositoryPort {
    private final SpringDataUserJpaRepository repository;
    public JpaUserRepository(SpringDataUserJpaRepository repository) { this.repository=repository; }
    @Override public Optional<User> findByUsername(String username) {
        return repository.findByUsernameIgnoreCase(username).map(UserJpaEntity::toDomain);
    }
    @Override public User save(User user) {
        return repository.save(UserJpaEntity.fromDomain(user)).toDomain();
    }
    @Override public boolean existsByUsername(String username) {
        return repository.existsByUsernameIgnoreCase(username);
    }
}
