package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.TimesheetReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetReportDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private String userName;

    @NotNull
    private Long companyId;

    @NotNull
    private String companyName;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalRegularHours;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalOvertimeHours;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalAllowance;

    @NotNull
    private String status;

    @NotNull
    private ZonedDateTime generatedAt;

    private ZonedDateTime approvedAt;

    @Size(max = 1000)
    private String comments;

    @NotNull
    private UserDTO generatedBy;

    private UserDTO approvedBy;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalRegularHours() {
        return totalRegularHours;
    }

    public void setTotalRegularHours(Double totalRegularHours) {
        this.totalRegularHours = totalRegularHours;
    }

    public Double getTotalOvertimeHours() {
        return totalOvertimeHours;
    }

    public void setTotalOvertimeHours(Double totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }

    public Double getTotalAllowance() {
        return totalAllowance;
    }

    public void setTotalAllowance(Double totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(ZonedDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UserDTO getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(UserDTO generatedBy) {
        this.generatedBy = generatedBy;
    }

    public UserDTO getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UserDTO approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimesheetReportDTO)) {
            return false;
        }

        TimesheetReportDTO timesheetReportDTO = (TimesheetReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timesheetReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetReportDTO{" +
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
            ", generatedBy=" + getGeneratedBy() +
            ", approvedBy=" + getApprovedBy() +
            "}";
    }
}
