package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Auditable;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@MappedSuperclass
public abstract class AbstractLongAuditable implements Auditable<String, Long, LocalDateTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active = true;
    private String createdBy;
    private String modifiedBy;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public AbstractLongAuditable() {}

    public AbstractLongAuditable(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @NonNull
    @Override
    public Optional<String> getCreatedBy() { return Optional.ofNullable(this.createdBy); }

    @Override
    public void setCreatedBy(@NonNull String createdBy) { this.createdBy = createdBy; }

    @NonNull
    @Override
    public Optional<LocalDateTime> getCreatedDate() { return Optional.ofNullable(this.createdDate); }

    @Override
    public void setCreatedDate(@NonNull LocalDateTime creationDate) { this.createdDate = creationDate; }

    @NonNull
    @Override
    public Optional<String> getLastModifiedBy() { return Optional.ofNullable(this.modifiedBy); }

    @Override
    public void setLastModifiedBy(@NonNull final String lastModifiedBy) { this.modifiedBy = lastModifiedBy; }

    @NonNull
    @Override
    public Optional<LocalDateTime> getLastModifiedDate() { return Optional.ofNullable(this.modifiedDate); }

    @Override
    public void setLastModifiedDate(@NonNull LocalDateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }

    @Override
    public boolean isNew() { return Objects.isNull(this.id); }

    public void auditChangedBy(final String user) {
        if (isNew()) {
            setCreatedBy(user);
            setCreatedDate(LocalDateTime.now());
        } else {
            setLastModifiedBy(user);
            setLastModifiedDate(LocalDateTime.now());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLongAuditable that = (AbstractLongAuditable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", active=" + active +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate
                ;
    }
}