package com.timesheetsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.timesheetsystem.domain.TimesheetReport} entity. This class is used
 * in {@link com.timesheetsystem.web.rest.TimesheetReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /timesheet-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private StringFilter userName;

    private LongFilter companyId;

    private StringFilter companyName;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private DoubleFilter totalRegularHours;

    private DoubleFilter totalOvertimeHours;

    private DoubleFilter totalAllowance;

    private StringFilter status;

    private ZonedDateTimeFilter generatedAt;

    private ZonedDateTimeFilter approvedAt;

    private StringFilter comments;

    private LongFilter generatedById;

    private LongFilter approvedById;

    private Boolean distinct;

    public TimesheetReportCriteria() {}

    public TimesheetReportCriteria(TimesheetReportCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.userName = other.optionalUserName().map(StringFilter::copy).orElse(null);
        this.companyId = other.optionalCompanyId().map(LongFilter::copy).orElse(null);
        this.companyName = other.optionalCompanyName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.totalRegularHours = other.optionalTotalRegularHours().map(DoubleFilter::copy).orElse(null);
        this.totalOvertimeHours = other.optionalTotalOvertimeHours().map(DoubleFilter::copy).orElse(null);
        this.totalAllowance = other.optionalTotalAllowance().map(DoubleFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.generatedAt = other.optionalGeneratedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.approvedAt = other.optionalApprovedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.generatedById = other.optionalGeneratedById().map(LongFilter::copy).orElse(null);
        this.approvedById = other.optionalApprovedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TimesheetReportCriteria copy() {
        return new TimesheetReportCriteria(this);
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

    public StringFilter getUserName() {
        return userName;
    }

    public Optional<StringFilter> optionalUserName() {
        return Optional.ofNullable(userName);
    }

    public StringFilter userName() {
        if (userName == null) {
            setUserName(new StringFilter());
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public Optional<StringFilter> optionalCompanyName() {
        return Optional.ofNullable(companyName);
    }

    public StringFilter companyName() {
        if (companyName == null) {
            setCompanyName(new StringFilter());
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public DoubleFilter getTotalRegularHours() {
        return totalRegularHours;
    }

    public Optional<DoubleFilter> optionalTotalRegularHours() {
        return Optional.ofNullable(totalRegularHours);
    }

    public DoubleFilter totalRegularHours() {
        if (totalRegularHours == null) {
            setTotalRegularHours(new DoubleFilter());
        }
        return totalRegularHours;
    }

    public void setTotalRegularHours(DoubleFilter totalRegularHours) {
        this.totalRegularHours = totalRegularHours;
    }

    public DoubleFilter getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public Optional<DoubleFilter> optionalTotalOvertimeHours() {
        return Optional.ofNullable(totalOvertimeHours);
    }

    public DoubleFilter totalOvertimeHours() {
        if (totalOvertimeHours == null) {
            setTotalOvertimeHours(new DoubleFilter());
        }
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(DoubleFilter totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public DoubleFilter getTotalAllowance() {
        return totalAllowance;
    }

    public Optional<DoubleFilter> optionalTotalAllowance() {
        return Optional.ofNullable(totalAllowance);
    }

    public DoubleFilter totalAllowance() {
        if (totalAllowance == null) {
            setTotalAllowance(new DoubleFilter());
        }
        return totalAllowance;
    }

    public void setTotalAllowance(DoubleFilter totalAllowance) {
        this.totalAllowance = totalAllowance;
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

    public ZonedDateTimeFilter getGeneratedAt() {
        return generatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalGeneratedAt() {
        return Optional.ofNullable(generatedAt);
    }

    public ZonedDateTimeFilter generatedAt() {
        if (generatedAt == null) {
            setGeneratedAt(new ZonedDateTimeFilter());
        }
        return generatedAt;
    }

    public void setGeneratedAt(ZonedDateTimeFilter generatedAt) {
        this.generatedAt = generatedAt;
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

    public StringFilter getComments() {
        return comments;
    }

    public Optional<StringFilter> optionalComments() {
        return Optional.ofNullable(comments);
    }

    public StringFilter comments() {
        if (comments == null) {
            setComments(new StringFilter());
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LongFilter getGeneratedById() {
        return generatedById;
    }

    public Optional<LongFilter> optionalGeneratedById() {
        return Optional.ofNullable(generatedById);
    }

    public LongFilter generatedById() {
        if (generatedById == null) {
            setGeneratedById(new LongFilter());
        }
        return generatedById;
    }

    public void setGeneratedById(LongFilter generatedById) {
        this.generatedById = generatedById;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public Optional<LongFilter> optionalApprovedById() {
        return Optional.ofNullable(approvedById);
    }

    public LongFilter approvedById() {
        if (approvedById == null) {
            setApprovedById(new LongFilter());
        }
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
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
        final TimesheetReportCriteria that = (TimesheetReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(totalRegularHours, that.totalRegularHours) &&
            Objects.equals(totalOvertimeHours, that.totalOvertimeHours) &&
            Objects.equals(totalAllowance, that.totalAllowance) &&
            Objects.equals(status, that.status) &&
            Objects.equals(generatedAt, that.generatedAt) &&
            Objects.equals(approvedAt, that.approvedAt) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(generatedById, that.generatedById) &&
            Objects.equals(approvedById, that.approvedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            userName,
            companyId,
            companyName,
            startDate,
            endDate,
            totalRegularHours,
            totalOvertimeHours,
            totalAllowance,
            status,
            generatedAt,
            approvedAt,
            comments,
            generatedById,
            approvedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetReportCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalUserName().map(f -> "userName=" + f + ", ").orElse("") +
            optionalCompanyId().map(f -> "companyId=" + f + ", ").orElse("") +
            optionalCompanyName().map(f -> "companyName=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalTotalRegularHours().map(f -> "totalRegularHours=" + f + ", ").orElse("") +
            optionalTotalOvertimeHours().map(f -> "totalOvertimeHours=" + f + ", ").orElse("") +
            optionalTotalAllowance().map(f -> "totalAllowance=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalGeneratedAt().map(f -> "generatedAt=" + f + ", ").orElse("") +
            optionalApprovedAt().map(f -> "approvedAt=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalGeneratedById().map(f -> "generatedById=" + f + ", ").orElse("") +
            optionalApprovedById().map(f -> "approvedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
