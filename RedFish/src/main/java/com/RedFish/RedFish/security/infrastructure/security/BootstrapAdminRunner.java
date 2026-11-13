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
    private static final Logger log = LoggerFactory.getLogger(BootstrapAdminRunner.class);
    private final RegisterUserService register;

    @Value("${REDFISH_BOOTSTRAP_ADMIN_USERNAME:}") private String adminUsername;
    @Value("${REDFISH_BOOTSTRAP_ADMIN_PASSWORD:}") private String adminPassword;
    @Value("${REDFISH_BOOTSTRAP_ADMIN_NAME:Administrador RedFish}") private String adminName;
    @Value("${REDFISH_BOOTSTRAP_PRODUCTION_USERNAME:}") private String productionUsername;
    @Value("${REDFISH_BOOTSTRAP_PRODUCTION_PASSWORD:}") private String productionPassword;
    @Value("${REDFISH_BOOTSTRAP_PRODUCTION_NAME:Usuario Producción}") private String productionName;
    @Value("${REDFISH_BOOTSTRAP_OPERATIONS_USERNAME:}") private String operationsUsername;
    @Value("${REDFISH_BOOTSTRAP_OPERATIONS_PASSWORD:}") private String operationsPassword;
    @Value("${REDFISH_BOOTSTRAP_OPERATIONS_NAME:Usuario Operaciones}") private String operationsName;
    @Value("${REDFISH_BOOTSTRAP_COMMERCIAL_USERNAME:}") private String commercialUsername;
    @Value("${REDFISH_BOOTSTRAP_COMMERCIAL_PASSWORD:}") private String commercialPassword;
    @Value("${REDFISH_BOOTSTRAP_COMMERCIAL_NAME:Usuario Comercial}") private String commercialName;

    public BootstrapAdminRunner(RegisterUserService register) { this.register = register; }

    @Override
    public void run(ApplicationArguments args) {
        createIfConfigured(adminName, adminUsername, adminPassword, "ADMIN");
        createIfConfigured(productionName, productionUsername, productionPassword, "PRODUCTION");
        createIfConfigured(operationsName, operationsUsername, operationsPassword, "OPERATIONS");
        createIfConfigured(commercialName, commercialUsername, commercialPassword, "COMMERCIAL");
    }

    private void createIfConfigured(String name, String username, String password, String role) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            log.warn("Bootstrap account for role {} not configured; set its REDFISH_BOOTSTRAP_* username and password.", role);
            return;
        }
        try {
            register.register(name, username, password, role);
            log.info("Initial RedFish account created for role {}: {}", role, username);
        } catch (IllegalArgumentException existingOrInvalid) {
            if (existingOrInvalid.getMessage() != null && existingOrInvalid.getMessage().contains("already exists")) {
                log.info("Bootstrap account already exists for role {}: {}", role, username);
                return;
            }
            throw existingOrInvalid;
        }
    }
}
