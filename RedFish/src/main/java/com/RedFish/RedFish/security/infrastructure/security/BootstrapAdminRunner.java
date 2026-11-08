package com.RedFish.RedFish.security.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.RedFish.RedFish.security.application.service.RegisterUserService;

@Component
public class BootstrapAdminRunner implements ApplicationRunner {
    private static final Logger log=LoggerFactory.getLogger(BootstrapAdminRunner.class);
    private final RegisterUserService register;
    @Value("${REDFISH_BOOTSTRAP_ADMIN_USERNAME:}") private String username;
    @Value("${REDFISH_BOOTSTRAP_ADMIN_PASSWORD:}") private String password;
    @Value("${REDFISH_BOOTSTRAP_ADMIN_NAME:Administrador RedFish}") private String name;
    public BootstrapAdminRunner(RegisterUserService register) { this.register=register; }
    @Override public void run(ApplicationArguments args) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            log.warn("No bootstrap administrator configured. Set REDFISH_BOOTSTRAP_ADMIN_USERNAME and REDFISH_BOOTSTRAP_ADMIN_PASSWORD.");
            return;
        }
        try {
            register.register(name, username, password, "ADMIN");
            log.info("Initial RedFish administrator created: {}", username);
        } catch (IllegalArgumentException existingOrInvalid) {
            if (existingOrInvalid.getMessage() != null && existingOrInvalid.getMessage().contains("already exists")) {
                log.info("Bootstrap administrator already exists: {}", username);
                return;
            }
            throw existingOrInvalid;
        }
    }
}
