package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.CompanyAllowance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyAllowanceDTO implements Serializable {

    private Long id;

    @NotNull
    private Double presentialAllowanceValue;

    @NotNull
    @DecimalMin(value = "0")
    private Double remoteAllowanceValue;

    @NotNull
    @DecimalMin(value = "0")
    private Double fullTimeThresholdHours;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer partTimeAllowancePercentage;

    @NotNull
    private Boolean considerWorkDistance;

    @Min(value = 0)
    private Integer minimumDistanceToReceive;

    @NotNull
    private Boolean active;

    @NotNull
    private ZonedDateTime lastUpdated;

    @NotNull
    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPresentialAllowanceValue() {
        return presentialAllowanceValue;
    }

    public void setPresentialAllowanceValue(Double presentialAllowanceValue) {
        this.presentialAllowanceValue = presentialAllowanceValue;
    }

    public Double getRemoteAllowanceValue() {
        return remoteAllowanceValue;
    }

    public void setRemoteAllowanceValue(Double remoteAllowanceValue) {
        this.remoteAllowanceValue = remoteAllowanceValue;
    }

    public Double getFullTimeThresholdHours() {
        return fullTimeThresholdHours;
    }

    public void setFullTimeThresholdHours(Double fullTimeThresholdHours) {
        this.fullTimeThresholdHours = fullTimeThresholdHours;
    }

    public Integer getPartTimeAllowancePercentage() {
        return partTimeAllowancePercentage;
    }

    public void setPartTimeAllowancePercentage(Integer partTimeAllowancePercentage) {
        this.partTimeAllowancePercentage = partTimeAllowancePercentage;
    }

    public Boolean getConsiderWorkDistance() {
        return considerWorkDistance;
    }

    public void setConsiderWorkDistance(Boolean considerWorkDistance) {
        this.considerWorkDistance = considerWorkDistance;
    }

    public Integer getMinimumDistanceToReceive() {
        return minimumDistanceToReceive;
    }

    public void setMinimumDistanceToReceive(Integer minimumDistanceToReceive) {
        this.minimumDistanceToReceive = minimumDistanceToReceive;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
        if (!(o instanceof CompanyAllowanceDTO)) {
            return false;
        }

        CompanyAllowanceDTO companyAllowanceDTO = (CompanyAllowanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyAllowanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyAllowanceDTO{" +
            "id=" + getId() +
            ", presentialAllowanceValue=" + getPresentialAllowanceValue() +
            ", remoteAllowanceValue=" + getRemoteAllowanceValue() +
            ", fullTimeThresholdHours=" + getFullTimeThresholdHours() +
            ", partTimeAllowancePercentage=" + getPartTimeAllowancePercentage() +
            ", considerWorkDistance='" + getConsiderWorkDistance() + "'" +
            ", minimumDistanceToReceive=" + getMinimumDistanceToReceive() +
            ", active='" + getActive() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
