package com.teamknowledgeassistant.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing (created/updated timestamps) for entities extending
 * {@link com.teamknowledgeassistant.common.AuditableEntity}.
 * <p>
 * Kept separate from the main application class so that web-layer slice tests
 * (e.g. {@code @WebMvcTest}) do not attempt to initialize the JPA auditing
 * infrastructure without a JPA context available.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
