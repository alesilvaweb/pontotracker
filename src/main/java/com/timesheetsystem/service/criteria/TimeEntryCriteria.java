package com.timesheetsystem.service.criteria;

import com.timesheetsystem.domain.enumeration.EntryType;
import com.timesheetsystem.domain.enumeration.OvertimeCategory;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.timesheetsystem.domain.TimeEntry} entity. This class is used
 * in {@link com.timesheetsystem.web.rest.TimeEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /time-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeEntryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EntryType
     */
    public static class EntryTypeFilter extends Filter<EntryType> {

        public EntryTypeFilter() {}

        public EntryTypeFilter(EntryTypeFilter filter) {
            super(filter);
        }

        @Override
        public EntryTypeFilter copy() {
            return new EntryTypeFilter(this);
        }
    }

    /**
     * Class for filtering OvertimeCategory
     */
    public static class OvertimeCategoryFilter extends Filter<OvertimeCategory> {

        public OvertimeCategoryFilter() {}

        public OvertimeCategoryFilter(OvertimeCategoryFilter filter) {
            super(filter);
        }

        @Override
        public OvertimeCategoryFilter copy() {
            return new OvertimeCategoryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter endTime;

    private EntryTypeFilter type;

    private OvertimeCategoryFilter overtimeCategory;

    private StringFilter description;

    private DoubleFilter hoursWorked;

    private LongFilter timesheetId;

    private Boolean distinct;

    public TimeEntryCriteria() {}

    public TimeEntryCriteria(TimeEntryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.type = other.optionalType().map(EntryTypeFilter::copy).orElse(null);
        this.overtimeCategory = other.optionalOvertimeCategory().map(OvertimeCategoryFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.hoursWorked = other.optionalHoursWorked().map(DoubleFilter::copy).orElse(null);
        this.timesheetId = other.optionalTimesheetId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TimeEntryCriteria copy() {
        return new TimeEntryCriteria(this);
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

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public Optional<ZonedDateTimeFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            setStartTime(new ZonedDateTimeFilter());
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getEndTime() {
        return endTime;
    }

    public Optional<ZonedDateTimeFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public ZonedDateTimeFilter endTime() {
        if (endTime == null) {
            setEndTime(new ZonedDateTimeFilter());
        }
        return endTime;
    }

    public void setEndTime(ZonedDateTimeFilter endTime) {
        this.endTime = endTime;
    }

    public EntryTypeFilter getType() {
        return type;
    }

    public Optional<EntryTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public EntryTypeFilter type() {
        if (type == null) {
            setType(new EntryTypeFilter());
        }
        return type;
    }

    public void setType(EntryTypeFilter type) {
        this.type = type;
    }

    public OvertimeCategoryFilter getOvertimeCategory() {
        return overtimeCategory;
    }

    public Optional<OvertimeCategoryFilter> optionalOvertimeCategory() {
        return Optional.ofNullable(overtimeCategory);
    }

    public OvertimeCategoryFilter overtimeCategory() {
        if (overtimeCategory == null) {
            setOvertimeCategory(new OvertimeCategoryFilter());
        }
        return overtimeCategory;
    }

    public void setOvertimeCategory(OvertimeCategoryFilter overtimeCategory) {
        this.overtimeCategory = overtimeCategory;
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

    public DoubleFilter getHoursWorked() {
        return hoursWorked;
    }

    public Optional<DoubleFilter> optionalHoursWorked() {
        return Optional.ofNullable(hoursWorked);
    }

    public DoubleFilter hoursWorked() {
        if (hoursWorked == null) {
            setHoursWorked(new DoubleFilter());
        }
        return hoursWorked;
    }

    public void setHoursWorked(DoubleFilter hoursWorked) {
        this.hoursWorked = hoursWorked;
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
        final TimeEntryCriteria that = (TimeEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(type, that.type) &&
            Objects.equals(overtimeCategory, that.overtimeCategory) &&
            Objects.equals(description, that.description) &&
            Objects.equals(hoursWorked, that.hoursWorked) &&
            Objects.equals(timesheetId, that.timesheetId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, type, overtimeCategory, description, hoursWorked, timesheetId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeEntryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalOvertimeCategory().map(f -> "overtimeCategory=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalHoursWorked().map(f -> "hoursWorked=" + f + ", ").orElse("") +
            optionalTimesheetId().map(f -> "timesheetId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
