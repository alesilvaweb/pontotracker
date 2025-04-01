package com.timesheetsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompanyAllowance.
 */
@Entity
@Table(name = "company_allowance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyAllowance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "presential_allowance_value", nullable = false)
    private Double presentialAllowanceValue;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "remote_allowance_value", nullable = false)
    private Double remoteAllowanceValue;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "full_time_threshold_hours", nullable = false)
    private Double fullTimeThresholdHours;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "part_time_allowance_percentage", nullable = false)
    private Integer partTimeAllowancePercentage;

    @NotNull
    @Column(name = "consider_work_distance", nullable = false)
    private Boolean considerWorkDistance;

    @Min(value = 0)
    @Column(name = "minimum_distance_to_receive")
    private Integer minimumDistanceToReceive;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @JsonIgnoreProperties(value = { "companyAllowance" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompanyAllowance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPresentialAllowanceValue() {
        return this.presentialAllowanceValue;
    }

    public CompanyAllowance presentialAllowanceValue(Double presentialAllowanceValue) {
        this.setPresentialAllowanceValue(presentialAllowanceValue);
        return this;
    }

    public void setPresentialAllowanceValue(Double presentialAllowanceValue) {
        this.presentialAllowanceValue = presentialAllowanceValue;
    }

    public Double getRemoteAllowanceValue() {
        return this.remoteAllowanceValue;
    }

    public CompanyAllowance remoteAllowanceValue(Double remoteAllowanceValue) {
        this.setRemoteAllowanceValue(remoteAllowanceValue);
        return this;
    }

    public void setRemoteAllowanceValue(Double remoteAllowanceValue) {
        this.remoteAllowanceValue = remoteAllowanceValue;
    }

    public Double getFullTimeThresholdHours() {
        return this.fullTimeThresholdHours;
    }

    public CompanyAllowance fullTimeThresholdHours(Double fullTimeThresholdHours) {
        this.setFullTimeThresholdHours(fullTimeThresholdHours);
        return this;
    }

    public void setFullTimeThresholdHours(Double fullTimeThresholdHours) {
        this.fullTimeThresholdHours = fullTimeThresholdHours;
    }

    public Integer getPartTimeAllowancePercentage() {
        return this.partTimeAllowancePercentage;
    }

    public CompanyAllowance partTimeAllowancePercentage(Integer partTimeAllowancePercentage) {
        this.setPartTimeAllowancePercentage(partTimeAllowancePercentage);
        return this;
    }

    public void setPartTimeAllowancePercentage(Integer partTimeAllowancePercentage) {
        this.partTimeAllowancePercentage = partTimeAllowancePercentage;
    }

    public Boolean getConsiderWorkDistance() {
        return this.considerWorkDistance;
    }

    public CompanyAllowance considerWorkDistance(Boolean considerWorkDistance) {
        this.setConsiderWorkDistance(considerWorkDistance);
        return this;
    }

    public void setConsiderWorkDistance(Boolean considerWorkDistance) {
        this.considerWorkDistance = considerWorkDistance;
    }

    public Integer getMinimumDistanceToReceive() {
        return this.minimumDistanceToReceive;
    }

    public CompanyAllowance minimumDistanceToReceive(Integer minimumDistanceToReceive) {
        this.setMinimumDistanceToReceive(minimumDistanceToReceive);
        return this;
    }

    public void setMinimumDistanceToReceive(Integer minimumDistanceToReceive) {
        this.minimumDistanceToReceive = minimumDistanceToReceive;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CompanyAllowance active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public CompanyAllowance lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanyAllowance company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyAllowance)) {
            return false;
        }
        return getId() != null && getId().equals(((CompanyAllowance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyAllowance{" +
            "id=" + getId() +
            ", presentialAllowanceValue=" + getPresentialAllowanceValue() +
            ", remoteAllowanceValue=" + getRemoteAllowanceValue() +
            ", fullTimeThresholdHours=" + getFullTimeThresholdHours() +
            ", partTimeAllowancePercentage=" + getPartTimeAllowancePercentage() +
            ", considerWorkDistance='" + getConsiderWorkDistance() + "'" +
            ", minimumDistanceToReceive=" + getMinimumDistanceToReceive() +
            ", active='" + getActive() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
