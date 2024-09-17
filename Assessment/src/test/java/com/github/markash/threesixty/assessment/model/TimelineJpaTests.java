package com.github.markash.threesixty.assessment.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=false"
})
class TimelineJpaTests {

    @Autowired
    private TimelineRepository timelineRepository;

    @Test
    @Sql("timeline_create_01.sql")
    void whenInitializedByDbUnit_thenFindsByName() {

        Timeline timeline = timelineRepository.findByName("Test Timeline");
        assertThat(timeline.getId()).isNotNull();
        assertThat(timeline).isNotNull();
        assertThat(timeline.getStart()).isEqualTo(LocalDate.parse("2024-05-01"));
        assertThat(timeline.getEnd()).isEqualTo(LocalDate.parse("2024-05-31"));
        assertThat(timeline.isActive()).isTrue();

        Milestone milestone = new Milestone();
        milestone.setDueDateTime(LocalDate.parse("2024-05-17"));

        timeline.addActivity(milestone);
        assertThat(timeline.getStart()).isEqualTo(LocalDate.parse("2024-05-01"));
        assertThat(timeline.getEnd()).isEqualTo(LocalDate.parse("2024-05-31"));


        Milestone milestone02 = new Milestone();
        milestone02.setDueDateTime(LocalDate.parse("2024-06-24"));

        timeline.addActivity(milestone02);
        assertThat(timeline.getStart()).isEqualTo(LocalDate.parse("2024-05-01"));
        assertThat(timeline.getEnd()).isEqualTo(LocalDate.parse("2024-06-24"));

        Timeline x = timelineRepository.save(timeline);

        timeline = timelineRepository.findByName("Test Timeline");
        assertThat(timeline.getId()).isNotNull();
        assertThat(timeline).isNotNull();
        assertThat(timeline.getStart()).isEqualTo(LocalDate.parse("2024-05-01"));
        assertThat(timeline.getEnd()).isEqualTo(LocalDate.parse("2024-06-24"));
        assertThat(timeline.isActive()).isTrue();
        assertThat(timeline.getCreatedDate()).isPresent();
        assertThat(timeline.getLastModifiedDate()).isPresent();

        //TODO Resolve the activities that are not persisted
        //assertThat(timeline.getActivities().size()).isEqualTo(2);
    }
}