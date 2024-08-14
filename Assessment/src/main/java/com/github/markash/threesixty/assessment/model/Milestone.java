package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Milestone extends Activity {

    @Column(name = "due_date")
    private LocalDateTime dueDateTime;

    public LocalDateTime getDueDateTime() { return dueDateTime; }
    public void setDueDateTime(LocalDateTime dueDateTime) { this.dueDateTime = dueDateTime; }
    public void setDueDateTime(LocalDate dueDateTime) { this.dueDateTime = dueDateTime.atStartOfDay(); }

    @Transient
    @Override
    public List<LocalDateTime> getTimelineDates() {
        return getDueDateTime() != null ? List.of(getDueDateTime()) : new ArrayList<>();
    }
}
