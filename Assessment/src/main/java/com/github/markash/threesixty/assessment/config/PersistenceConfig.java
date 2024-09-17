package com.github.markash.threesixty.assessment.config;

import com.github.markash.threesixty.assessment.audit.AuditorAwareImpl;
import com.github.markash.threesixty.assessment.model.Timeline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {Timeline.class})
public class PersistenceConfig {

    // Clash with system provided bean after adding Spring Data Rest
//    @Bean("auditorProvider")
//    public AuditorAware<String> auditorProvider() {
//        return new AuditorAwareImpl();
//    }

    @Bean
    IsNewAwareAuditingHandler isNewAwareAuditingHandler(PersistentEntities context) {

        return new IsNewAwareAuditingHandler(context);
    }
}
