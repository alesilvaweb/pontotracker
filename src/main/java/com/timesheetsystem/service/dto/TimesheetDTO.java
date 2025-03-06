package com.timesheetsystem.service.dto;

import com.timesheetsystem.domain.enumeration.WorkModality;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.Timesheet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimesheetDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private WorkModality modality;

    private String classification;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalHours;

    @DecimalMin(value = "0")
    private Double overtimeHours;

    @DecimalMin(value = "0")
    private Double allowanceValue;

    @NotNull
    private String status;

    @NotNull
    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime approvedAt;

    private String approvedBy;

    @NotNull
    private UserDTO user;

    @NotNull
    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkModality getModality() {
        return modality;
    }

    public void setModality(WorkModality modality) {
        this.modality = modality;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getAllowanceValue() {
        return allowanceValue;
    }

    public void setAllowanceValue(Double allowanceValue) {
        this.allowanceValue = allowanceValue;
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

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimesheetDTO)) {
            return false;
        }

        TimesheetDTO timesheetDTO = (TimesheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timesheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimesheetDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", modality='" + getModality() + "'" +
            ", classification='" + getClassification() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalHours=" + getTotalHours() +
            ", overtimeHours=" + getOvertimeHours() +
            ", allowanceValue=" + getAllowanceValue() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", approvedBy='" + getApprovedBy() + "'" +
            ", user=" + getUser() +
            ", company=" + getCompany() +
            "}";
    }
}
