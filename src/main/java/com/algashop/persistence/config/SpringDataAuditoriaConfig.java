package com.algashop.persistence.config;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(
    dateTimeProviderRef = "dateTimeProvider",
    auditorAwareRef = "auditorProvider"
)
public class SpringDataAuditoriaConfig {
    
    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(
            OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        );
    }

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        // como ainda não tem a camada de segurança implementada
        // temporariamente será criado um usuário aleatório
        return () -> Optional.of(UUID.randomUUID());
    }
}
