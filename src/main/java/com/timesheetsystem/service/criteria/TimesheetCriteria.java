package com.timesheetsystem.service.criteria;

import com.timesheetsystem.domain.enumeration.WorkModality;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.timesheetsystem.domain.Timesheet} entity. This class is used
 * in {@link com.timesheetsystem.web.rest.TimesheetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /timesheets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkModality
     */
    public static class WorkModalityFilter extends Filter<WorkModality> {

        public WorkModalityFilter() {}

        public WorkModalityFilter(WorkModalityFilter filter) {
            super(filter);
        }

        @Override
        public WorkModalityFilter copy() {
            return new WorkModalityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private WorkModalityFilter modality;

    private StringFilter classification;

    private StringFilter description;

    private DoubleFilter totalHours;

    private DoubleFilter overtimeHours;

    private DoubleFilter allowanceValue;

    private StringFilter status;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private ZonedDateTimeFilter approvedAt;

    private StringFilter approvedBy;

    private LongFilter timeEntryId;

    private LongFilter userId;

    private LongFilter companyId;

    private Boolean distinct;

    public TimesheetCriteria() {}

    public TimesheetCriteria(TimesheetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.modality = other.optionalModality().map(WorkModalityFilter::copy).orElse(null);
        this.classification = other.optionalClassification().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.totalHours = other.optionalTotalHours().map(DoubleFilter::copy).orElse(null);
        this.overtimeHours = other.optionalOvertimeHours().map(DoubleFilter::copy).orElse(null);
        this.allowanceValue = other.optionalAllowanceValue().map(DoubleFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.approvedAt = other.optionalApprovedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.approvedBy = other.optionalApprovedBy().map(StringFilter::copy).orElse(null);
        this.timeEntryId = other.optionalTimeEntryId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.companyId = other.optionalCompanyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TimesheetCriteria copy() {
        return new TimesheetCriteria(this);
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

    public WorkModalityFilter getModality() {
        return modality;
    }

    public Optional<WorkModalityFilter> optionalModality() {
        return Optional.ofNullable(modality);
    }

    public WorkModalityFilter modality() {
        if (modality == null) {
            setModality(new WorkModalityFilter());
        }
        return modality;
    }

    public void setModality(WorkModalityFilter modality) {
        this.modality = modality;
    }

    public StringFilter getClassification() {
        return classification;
    }

    public Optional<StringFilter> optionalClassification() {
        return Optional.ofNullable(classification);
    }

    public StringFilter classification() {
        if (classification == null) {
            setClassification(new StringFilter());
        }
        return classification;
    }

    public void setClassification(StringFilter classification) {
        this.classification = classification;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getTotalHours() {
        return totalHours;
    }

    public Optional<DoubleFilter> optionalTotalHours() {
        return Optional.ofNullable(totalHours);
    }

    public DoubleFilter totalHours() {
        if (totalHours == null) {
            setTotalHours(new DoubleFilter());
        }
        return totalHours;
    }

    public void setTotalHours(DoubleFilter totalHours) {
        this.totalHours = totalHours;
    }

    public DoubleFilter getOvertimeHours() {
        return overtimeHours;
    }

    public Optional<DoubleFilter> optionalOvertimeHours() {
        return Optional.ofNullable(overtimeHours);
    }

    public DoubleFilter overtimeHours() {
        if (overtimeHours == null) {
            setOvertimeHours(new DoubleFilter());
        }
        return overtimeHours;
    }

    public void setOvertimeHours(DoubleFilter overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public DoubleFilter getAllowanceValue() {
        return allowanceValue;
    }

    public Optional<DoubleFilter> optionalAllowanceValue() {
        return Optional.ofNullable(allowanceValue);
    }

    public DoubleFilter allowanceValue() {
        if (allowanceValue == null) {
            setAllowanceValue(new DoubleFilter());
        }
        return allowanceValue;
    }

    public void setAllowanceValue(DoubleFilter allowanceValue) {
        this.allowanceValue = allowanceValue;
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

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTimeFilter getApprovedAt() {
        return approvedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalApprovedAt() {
        return Optional.ofNullable(approvedAt);
    }

    public ZonedDateTimeFilter approvedAt() {
        if (approvedAt == null) {
            setApprovedAt(new ZonedDateTimeFilter());
        }
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTimeFilter approvedAt) {
        this.approvedAt = approvedAt;
    }

    public StringFilter getApprovedBy() {
        return approvedBy;
    }

    public Optional<StringFilter> optionalApprovedBy() {
        return Optional.ofNullable(approvedBy);
    }

    public StringFilter approvedBy() {
        if (approvedBy == null) {
            setApprovedBy(new StringFilter());
        }
        return approvedBy;
    }

    public void setApprovedBy(StringFilter approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LongFilter getTimeEntryId() {
        return timeEntryId;
    }

    public Optional<LongFilter> optionalTimeEntryId() {
        return Optional.ofNullable(timeEntryId);
    }

    public LongFilter timeEntryId() {
        if (timeEntryId == null) {
            setTimeEntryId(new LongFilter());
        }
        return timeEntryId;
    }

    public void setTimeEntryId(LongFilter timeEntryId) {
        this.timeEntryId = timeEntryId;
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

    public LongFilter getCompanyId() {
        return companyId;
    }

    public Optional<LongFilter> optionalCompanyId() {
        return Optional.ofNullable(companyId);
    }

    public LongFilter companyId() {
        if (companyId == null) {
            setCompanyId(new LongFilter());
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
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
        final TimesheetCriteria that = (TimesheetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(modality, that.modality) &&
            Objects.equals(classification, that.classification) &&
            Objects.equals(description, that.description) &&
            Objects.equals(totalHours, that.totalHours) &&
            Objects.equals(overtimeHours, that.overtimeHours) &&
            Objects.equals(allowanceValue, that.allowanceValue) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(approvedAt, that.approvedAt) &&
            Objects.equals(approvedBy, that.approvedBy) &&
            Objects.equals(timeEntryId, that.timeEntryId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            date,
            modality,
            classification,
            description,
            totalHours,
            overtimeHours,
            allowanceValue,
            status,
            createdAt,
            updatedAt,
            approvedAt,
            approvedBy,
            timeEntryId,
            userId,
            companyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalModality().map(f -> "modality=" + f + ", ").orElse("") +
            optionalClassification().map(f -> "classification=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalTotalHours().map(f -> "totalHours=" + f + ", ").orElse("") +
            optionalOvertimeHours().map(f -> "overtimeHours=" + f + ", ").orElse("") +
            optionalAllowanceValue().map(f -> "allowanceValue=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalApprovedAt().map(f -> "approvedAt=" + f + ", ").orElse("") +
            optionalApprovedBy().map(f -> "approvedBy=" + f + ", ").orElse("") +
            optionalTimeEntryId().map(f -> "timeEntryId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalCompanyId().map(f -> "companyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
