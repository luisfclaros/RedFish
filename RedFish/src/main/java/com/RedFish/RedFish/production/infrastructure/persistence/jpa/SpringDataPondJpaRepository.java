package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPondJpaRepository extends JpaRepository<PondJpaEntity, Long> {
    boolean existsByCodeIgnoreCase(String code);
}
