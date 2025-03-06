package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.TimesheetAlert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetAlertDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long timesheetId;

    @NotNull
    private LocalDate date;

    @NotNull
    private String type;

    @NotNull
    private String message;

    @NotNull
    private String severity;

    @NotNull
    private String status;

    @NotNull
    private ZonedDateTime createdAt;

    private ZonedDateTime resolvedAt;

    @Size(max = 500)
    private String resolution;

    private UserDTO resolvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(Long timesheetId) {
        this.timesheetId = timesheetId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(ZonedDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public UserDTO getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(UserDTO resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimesheetAlertDTO)) {
            return false;
        }

        TimesheetAlertDTO timesheetAlertDTO = (TimesheetAlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timesheetAlertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetAlertDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", timesheetId=" + getTimesheetId() +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", message='" + getMessage() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", resolvedBy=" + getResolvedBy() +
            "}";
    }
}
