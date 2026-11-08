package com.RedFish.RedFish.modules.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.security.domain.model.Role;
import com.RedFish.RedFish.security.domain.model.User;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

class SecurityAndVehicleDomainTests {

    @Test
    void roleRequiresANameAndKeepsItsOtherAttributes() {
        Role role = new Role(1L, "ADMIN", "Administrator");

        assertThat(role.id()).isEqualTo(1L);
        assertThat(role.name()).isEqualTo("ADMIN");
        assertThat(role.description()).isEqualTo("Administrator");
        assertThatThrownBy(() -> new Role(2L, " ", "Invalid"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("role name");
    }

    @Test
    void userCanBeActivatedAndDeactivated() {
        Role role = new Role(1L, "ADMIN", "Administrator");
        LocalDateTime createdAt = LocalDateTime.of(2026, 11, 5, 8, 0);
        User user = new User(7L, "Ana", "ana", "hash", role, false, createdAt);

        assertThat(user.id()).isEqualTo(7L);
        assertThat(user.name()).isEqualTo("Ana");
        assertThat(user.username()).isEqualTo("ana");
        assertThat(user.passwordHash()).isEqualTo("hash");
        assertThat(user.role()).isEqualTo(role);
        assertThat(user.createdAt()).isEqualTo(createdAt);
        assertThat(user.active()).isFalse();

        user.activate();
        assertThat(user.active()).isTrue();

        user.deactivate();
        assertThat(user.active()).isFalse();
    }

    @Test
    void userRejectsMissingRequiredAttributes() {
        Role role = new Role(1L, "ADMIN", "Administrator");

        assertThatThrownBy(() -> new User(1L, " ", "ana", "hash", role, true, LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("user name");
        assertThatThrownBy(() -> new User(1L, "Ana", "ana", " ", role, true, LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("password hash");
        assertThatThrownBy(() -> new User(1L, "Ana", "ana", "hash", null, true, LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("role");
    }

    @Test
    void activeVehicleCanBeAssignedAndInactiveVehicleCannot() {
        Vehicle activeVehicle = new Vehicle(1L, "ABC123", "Chevrolet", "NPR",
            new BigDecimal("3500.00"), true, "Refrigerated");
        Vehicle inactiveVehicle = new Vehicle(2L, "XYZ789", "Hino", "300",
            new BigDecimal("2500.00"), false, null);

        assertThat(activeVehicle.id()).isEqualTo(1L);
        assertThat(activeVehicle.plate()).isEqualTo("ABC123");
        assertThat(activeVehicle.active()).isTrue();
        activeVehicle.ensureCanBeAssigned();

        assertThat(inactiveVehicle.active()).isFalse();
        assertThatThrownBy(inactiveVehicle::ensureCanBeAssigned)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("inactive vehicles");
    }

    @Test
    void vehicleRejectsInvalidRequiredFieldsAndNegativeCapacity() {
        assertThatThrownBy(() -> new Vehicle(1L, " ", "Chevrolet", "NPR",
            BigDecimal.ONE, true, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("vehicle plate");
        assertThatThrownBy(() -> new Vehicle(1L, "ABC123", "Chevrolet", "NPR",
            new BigDecimal("-1"), true, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("load capacity");
    }
}
