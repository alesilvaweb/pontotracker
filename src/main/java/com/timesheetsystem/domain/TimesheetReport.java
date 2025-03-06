package com.timesheetsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimesheetReport.
 */
@Entity
@Table(name = "timesheet_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_regular_hours", nullable = false)
    private Double totalRegularHours;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_overtime_hours", nullable = false)
    private Double totalOvertimeHours;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_allowance", nullable = false)
    private Double totalAllowance;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "generated_at", nullable = false)
    private ZonedDateTime generatedAt;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Size(max = 1000)
    @Column(name = "comments", length = 1000)
    private String comments;

    @ManyToOne(optional = false)
    @NotNull
    private User generatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User approvedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimesheetReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public TimesheetReport userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public TimesheetReport userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public TimesheetReport companyId(Long companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public TimesheetReport companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public TimesheetReport startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public TimesheetReport endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalRegularHours() {
        return this.totalRegularHours;
    }

    public TimesheetReport totalRegularHours(Double totalRegularHours) {
        this.setTotalRegularHours(totalRegularHours);
        return this;
    }

    public void setTotalRegularHours(Double totalRegularHours) {
        this.totalRegularHours = totalRegularHours;
    }

    public Double getTotalOvertimeHours() {
        return this.totalOvertimeHours;
    }

    public TimesheetReport totalOvertimeHours(Double totalOvertimeHours) {
        this.setTotalOvertimeHours(totalOvertimeHours);
        return this;
    }

    public void setTotalOvertimeHours(Double totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public Double getTotalAllowance() {
        return this.totalAllowance;
    }

    public TimesheetReport totalAllowance(Double totalAllowance) {
        this.setTotalAllowance(totalAllowance);
        return this;
    }

    public void setTotalAllowance(Double totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    public String getStatus() {
        return this.status;
    }

    public TimesheetReport status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public TimesheetReport generatedAt(ZonedDateTime generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public void setGeneratedAt(ZonedDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public TimesheetReport approvedAt(ZonedDateTime approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getComments() {
        return this.comments;
    }

    public TimesheetReport comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public User getGeneratedBy() {
        return this.generatedBy;
    }

    public void setGeneratedBy(User user) {
        this.generatedBy = user;
    }

    public TimesheetReport generatedBy(User user) {
        this.setGeneratedBy(user);
        return this;
    }

    public User getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }

    public TimesheetReport approvedBy(User user) {
        this.setApprovedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimesheetReport)) {
            return false;
        }
        return getId() != null && getId().equals(((TimesheetReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetReport{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            ", companyId=" + getCompanyId() +
            ", companyName='" + getCompanyName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", totalRegularHours=" + getTotalRegularHours() +
            ", totalOvertimeHours=" + getTotalOvertimeHours() +
            ", totalAllowance=" + getTotalAllowance() +
            ", status='" + getStatus() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
