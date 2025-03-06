package com.timesheetsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.timesheetsystem.domain.TimesheetAlert} entity. This class is used
 * in {@link com.timesheetsystem.web.rest.TimesheetAlertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /timesheet-alerts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetAlertCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private LongFilter timesheetId;

    private LocalDateFilter date;

    private StringFilter type;

    private StringFilter message;

    private StringFilter severity;

    private StringFilter status;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter resolvedAt;

    private StringFilter resolution;

    private LongFilter resolvedById;

    private Boolean distinct;

    public TimesheetAlertCriteria() {}

    public TimesheetAlertCriteria(TimesheetAlertCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.timesheetId = other.optionalTimesheetId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.severity = other.optionalSeverity().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.resolvedAt = other.optionalResolvedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.resolution = other.optionalResolution().map(StringFilter::copy).orElse(null);
        this.resolvedById = other.optionalResolvedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TimesheetAlertCriteria copy() {
        return new TimesheetAlertCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTimesheetId() {
        return timesheetId;
    }

    public Optional<LongFilter> optionalTimesheetId() {
        return Optional.ofNullable(timesheetId);
    }

    public LongFilter timesheetId() {
        if (timesheetId == null) {
            setTimesheetId(new LongFilter());
        }
        return timesheetId;
    }

    public void setTimesheetId(LongFilter timesheetId) {
        this.timesheetId = timesheetId;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getSeverity() {
        return severity;
    }

    public Optional<StringFilter> optionalSeverity() {
        return Optional.ofNullable(severity);
    }

    public StringFilter severity() {
        if (severity == null) {
            setSeverity(new StringFilter());
        }
        return severity;
    }

    public void setSeverity(StringFilter severity) {
        this.severity = severity;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getResolvedAt() {
        return resolvedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalResolvedAt() {
        return Optional.ofNullable(resolvedAt);
    }

    public ZonedDateTimeFilter resolvedAt() {
        if (resolvedAt == null) {
            setResolvedAt(new ZonedDateTimeFilter());
        }
        return resolvedAt;
    }

    public void setResolvedAt(ZonedDateTimeFilter resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public StringFilter getResolution() {
        return resolution;
    }

    public Optional<StringFilter> optionalResolution() {
        return Optional.ofNullable(resolution);
    }

    public StringFilter resolution() {
        if (resolution == null) {
            setResolution(new StringFilter());
        }
        return resolution;
    }

    public void setResolution(StringFilter resolution) {
        this.resolution = resolution;
    }

    public LongFilter getResolvedById() {
        return resolvedById;
    }

    public Optional<LongFilter> optionalResolvedById() {
        return Optional.ofNullable(resolvedById);
    }

    public LongFilter resolvedById() {
        if (resolvedById == null) {
            setResolvedById(new LongFilter());
        }
        return resolvedById;
    }

    public void setResolvedById(LongFilter resolvedById) {
        this.resolvedById = resolvedById;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TimesheetAlertCriteria that = (TimesheetAlertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(timesheetId, that.timesheetId) &&
            Objects.equals(date, that.date) &&
            Objects.equals(type, that.type) &&
            Objects.equals(message, that.message) &&
            Objects.equals(severity, that.severity) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(resolvedAt, that.resolvedAt) &&
            Objects.equals(resolution, that.resolution) &&
            Objects.equals(resolvedById, that.resolvedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            timesheetId,
            date,
            type,
            message,
            severity,
            status,
            createdAt,
            resolvedAt,
            resolution,
            resolvedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetAlertCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalTimesheetId().map(f -> "timesheetId=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalSeverity().map(f -> "severity=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalResolvedAt().map(f -> "resolvedAt=" + f + ", ").orElse("") +
            optionalResolution().map(f -> "resolution=" + f + ", ").orElse("") +
            optionalResolvedById().map(f -> "resolvedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
