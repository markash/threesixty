package com.github.markash.threesixty.assessment.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=false"
})
class ActivityJpaTests {

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Sql("timeline_create_01.sql")
    void whenInitializedByDbUnit_thenFindsByName() {

        List<Activity> activities = activityRepository.findAll();
        activities.forEach(System.out::println);
    }
}