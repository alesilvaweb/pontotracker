package com.timesheetsystem.service.dto;

import com.timesheetsystem.domain.enumeration.EntryType;
import com.timesheetsystem.domain.enumeration.OvertimeCategory;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.TimeEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    @NotNull
    private EntryType type;

    private OvertimeCategory overtimeCategory;

    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private Double hoursWorked;

    @NotNull
    private TimesheetDTO timesheet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public OvertimeCategory getOvertimeCategory() {
        return overtimeCategory;
    }

    public void setOvertimeCategory(OvertimeCategory overtimeCategory) {
        this.overtimeCategory = overtimeCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public TimesheetDTO getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(TimesheetDTO timesheet) {
        this.timesheet = timesheet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeEntryDTO)) {
            return false;
        }

        TimeEntryDTO timeEntryDTO = (TimeEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeEntryDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", type='" + getType() + "'" +
            ", overtimeCategory='" + getOvertimeCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", hoursWorked=" + getHoursWorked() +
            ", timesheet=" + getTimesheet() +
            "}";
    }
}
