package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.TimesheetAudit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetAuditDTO implements Serializable {

    private Long id;

    @NotNull
    private String entityType;

    @NotNull
    private Long entityId;

    @NotNull
    private String action;

    @NotNull
    private ZonedDateTime timestamp;

    @NotNull
    private Long userId;

    @Size(max = 10000)
    private String oldValues;

    @Size(max = 10000)
    private String newValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimesheetAuditDTO)) {
            return false;
        }

        TimesheetAuditDTO timesheetAuditDTO = (TimesheetAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timesheetAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetAuditDTO{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityId=" + getEntityId() +
            ", action='" + getAction() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", userId=" + getUserId() +
            ", oldValues='" + getOldValues() + "'" +
            ", newValues='" + getNewValues() + "'" +
            "}";
    }
}
