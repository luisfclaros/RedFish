package com.RedFish.RedFish.security.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.RedFish.RedFish.security.application.port.UserRepositoryPort;

@Service
public class RedFishUserDetailsService implements UserDetailsService {
    private final UserRepositoryPort users;
    public RedFishUserDetailsService(UserRepositoryPort users) { this.users=users; }
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user=users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("invalid username or password"));
        return org.springframework.security.core.userdetails.User.withUsername(user.username())
                .password(user.passwordHash())
                .authorities("ROLE_"+user.role().name())
                .disabled(!user.active())
                .build();
    }
}
