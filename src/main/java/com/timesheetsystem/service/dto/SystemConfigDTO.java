package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.SystemConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemConfigDTO implements Serializable {

    private Long id;

    @NotNull
    private Double dailyWorkHours;

    @NotNull
    private Double weeklyWorkHours;

    @NotNull
    private Double overtimeNormalRate;

    @NotNull
    private Double overtimeSpecialRate;

    @NotNull
    private ZonedDateTime lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDailyWorkHours() {
        return dailyWorkHours;
    }

    public void setDailyWorkHours(Double dailyWorkHours) {
        this.dailyWorkHours = dailyWorkHours;
    }

    public Double getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    public void setWeeklyWorkHours(Double weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public Double getOvertimeNormalRate() {
        return overtimeNormalRate;
    }

    public void setOvertimeNormalRate(Double overtimeNormalRate) {
        this.overtimeNormalRate = overtimeNormalRate;
    }

    public Double getOvertimeSpecialRate() {
        return overtimeSpecialRate;
    }

    public void setOvertimeSpecialRate(Double overtimeSpecialRate) {
        this.overtimeSpecialRate = overtimeSpecialRate;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemConfigDTO)) {
            return false;
        }

        SystemConfigDTO systemConfigDTO = (SystemConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemConfigDTO{" +
            "id=" + getId() +
            ", dailyWorkHours=" + getDailyWorkHours() +
            ", weeklyWorkHours=" + getWeeklyWorkHours() +
            ", overtimeNormalRate=" + getOvertimeNormalRate() +
            ", overtimeSpecialRate=" + getOvertimeSpecialRate() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
