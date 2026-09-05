package com.RedFish.RedFish.security.application.port;

import java.util.Optional;

import com.RedFish.RedFish.security.domain.model.User;

public interface UserRepositoryPort {

	Optional<User> findByUsername(String username);

	User save(User user);
}
