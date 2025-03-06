package com.timesheetsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timesheetsystem.domain.enumeration.EntryType;
import com.timesheetsystem.domain.enumeration.OvertimeCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimeEntry.
 */
@Entity
@Table(name = "time_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EntryType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "overtime_category")
    private OvertimeCategory overtimeCategory;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "hours_worked", nullable = false)
    private Double hoursWorked;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "timeEntries", "user", "company" }, allowSetters = true)
    private Timesheet timesheet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public TimeEntry startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public TimeEntry endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public EntryType getType() {
        return this.type;
    }

    public TimeEntry type(EntryType type) {
        this.setType(type);
        return this;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public OvertimeCategory getOvertimeCategory() {
        return this.overtimeCategory;
    }

    public TimeEntry overtimeCategory(OvertimeCategory overtimeCategory) {
        this.setOvertimeCategory(overtimeCategory);
        return this;
    }

    public void setOvertimeCategory(OvertimeCategory overtimeCategory) {
        this.overtimeCategory = overtimeCategory;
    }

    public String getDescription() {
        return this.description;
    }

    public TimeEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getHoursWorked() {
        return this.hoursWorked;
    }

    public TimeEntry hoursWorked(Double hoursWorked) {
        this.setHoursWorked(hoursWorked);
        return this;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Timesheet getTimesheet() {
        return this.timesheet;
    }

    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    public TimeEntry timesheet(Timesheet timesheet) {
        this.setTimesheet(timesheet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeEntry{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", type='" + getType() + "'" +
            ", overtimeCategory='" + getOvertimeCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", hoursWorked=" + getHoursWorked() +
            "}";
    }
}
