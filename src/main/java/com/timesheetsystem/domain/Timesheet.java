package com.timesheetsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timesheetsystem.domain.enumeration.WorkModality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Timesheet.
 */
@Entity
@Table(name = "timesheet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Timesheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "modality", nullable = false)
    private WorkModality modality;

    @Column(name = "classification")
    private String classification;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_hours", nullable = false)
    private Double totalHours;

    @DecimalMin(value = "0")
    @Column(name = "overtime_hours")
    private Double overtimeHours;

    @DecimalMin(value = "0")
    @Column(name = "allowance_value")
    private Double allowanceValue;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "timesheet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "timesheet" }, allowSetters = true)
    private Set<TimeEntry> timeEntries = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "companyAllowance" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Timesheet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Timesheet date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkModality getModality() {
        return this.modality;
    }

    public Timesheet modality(WorkModality modality) {
        this.setModality(modality);
        return this;
    }

    public void setModality(WorkModality modality) {
        this.modality = modality;
    }

    public String getClassification() {
        return this.classification;
    }

    public Timesheet classification(String classification) {
        this.setClassification(classification);
        return this;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDescription() {
        return this.description;
    }

    public Timesheet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalHours() {
        return this.totalHours;
    }

    public Timesheet totalHours(Double totalHours) {
        this.setTotalHours(totalHours);
        return this;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getOvertimeHours() {
        return this.overtimeHours;
    }

    public Timesheet overtimeHours(Double overtimeHours) {
        this.setOvertimeHours(overtimeHours);
        return this;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getAllowanceValue() {
        return this.allowanceValue;
    }

    public Timesheet allowanceValue(Double allowanceValue) {
        this.setAllowanceValue(allowanceValue);
        return this;
    }

    public void setAllowanceValue(Double allowanceValue) {
        this.allowanceValue = allowanceValue;
    }

    public String getStatus() {
        return this.status;
    }

    public Timesheet status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Timesheet createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Timesheet updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public Timesheet approvedAt(ZonedDateTime approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public Timesheet approvedBy(String approvedBy) {
        this.setApprovedBy(approvedBy);
        return this;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Set<TimeEntry> getTimeEntries() {
        return this.timeEntries;
    }

    public void setTimeEntries(Set<TimeEntry> timeEntries) {
        if (this.timeEntries != null) {
            this.timeEntries.forEach(i -> i.setTimesheet(null));
        }
        if (timeEntries != null) {
            timeEntries.forEach(i -> i.setTimesheet(this));
        }
        this.timeEntries = timeEntries;
    }

    public Timesheet timeEntries(Set<TimeEntry> timeEntries) {
        this.setTimeEntries(timeEntries);
        return this;
    }

    public Timesheet addTimeEntry(TimeEntry timeEntry) {
        this.timeEntries.add(timeEntry);
        timeEntry.setTimesheet(this);
        return this;
    }

    public Timesheet removeTimeEntry(TimeEntry timeEntry) {
        this.timeEntries.remove(timeEntry);
        timeEntry.setTimesheet(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timesheet user(User user) {
        this.setUser(user);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Timesheet company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timesheet)) {
            return false;
        }
        return getId() != null && getId().equals(((Timesheet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Timesheet{" +
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
            "}";
    }
}
