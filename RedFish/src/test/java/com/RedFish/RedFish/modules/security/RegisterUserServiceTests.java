package com.RedFish.RedFish.modules.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.RedFish.RedFish.security.application.port.UserRepositoryPort;
import com.RedFish.RedFish.security.application.service.RegisterUserService;
import com.RedFish.RedFish.security.domain.model.User;

class RegisterUserServiceTests {

    @Test
    void storesOnlyEncodedPasswordAndAssignsRequestedRole() {
        AtomicReference<User> saved = new AtomicReference<>();
        UserRepositoryPort repository = new UserRepositoryPort() {
            @Override public Optional<User> findByUsername(String username) { return Optional.empty(); }
            @Override public User save(User user) { saved.set(user); return user; }
        };
        var encoder = new BCryptPasswordEncoder();
        User result = new RegisterUserService(repository, encoder)
                .register("Operador", "operador", "ClaveSegura123", "OPERATOR");

        assertThat(result).isSameAs(saved.get());
        assertThat(result.passwordHash()).isNotEqualTo("ClaveSegura123");
        assertThat(encoder.matches("ClaveSegura123", result.passwordHash())).isTrue();
        assertThat(result.role().name()).isEqualTo("OPERATOR");
        assertThat(result.active()).isTrue();
    }

    @Test
    void acceptsTheFourAreaRoles() {
        AtomicReference<User> saved = new AtomicReference<>();
        UserRepositoryPort repository = new UserRepositoryPort() {
            @Override public Optional<User> findByUsername(String username) { return Optional.empty(); }
            @Override public User save(User user) { saved.set(user); return user; }
        };
        var service = new RegisterUserService(repository, new BCryptPasswordEncoder());

        for (String role : new String[] {"ADMIN", "PRODUCTION", "OPERATIONS", "COMMERCIAL"}) {
            User user = service.register("Usuario " + role, "user_" + role.toLowerCase(),
                    "ClaveSegura123", role);
            assertThat(user.role().name()).isEqualTo(role);
        }
    }

    @Test
    void rejectsShortPasswordsAndInvalidRoles() {
        UserRepositoryPort repository = new UserRepositoryPort() {
            @Override public Optional<User> findByUsername(String username) { return Optional.empty(); }
            @Override public User save(User user) { return user; }
        };
        var service = new RegisterUserService(repository, new BCryptPasswordEncoder());

        assertThatThrownBy(() -> service.register("Operador", "operador", "123", "OPERATOR"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("10 characters");
        assertThatThrownBy(() -> service.register("Operador", "operador", "ClaveSegura123", "ROOT"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ADMIN, PRODUCTION, OPERATIONS or COMMERCIAL");
    }
}
