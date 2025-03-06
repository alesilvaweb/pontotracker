package com.timesheetsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemConfig.
 */
@Entity
@Table(name = "system_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "daily_work_hours", nullable = false)
    private Double dailyWorkHours;

    @NotNull
    @Column(name = "weekly_work_hours", nullable = false)
    private Double weeklyWorkHours;

    @NotNull
    @Column(name = "overtime_normal_rate", nullable = false)
    private Double overtimeNormalRate;

    @NotNull
    @Column(name = "overtime_special_rate", nullable = false)
    private Double overtimeSpecialRate;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDailyWorkHours() {
        return this.dailyWorkHours;
    }

    public SystemConfig dailyWorkHours(Double dailyWorkHours) {
        this.setDailyWorkHours(dailyWorkHours);
        return this;
    }

    public void setDailyWorkHours(Double dailyWorkHours) {
        this.dailyWorkHours = dailyWorkHours;
    }

    public Double getWeeklyWorkHours() {
        return this.weeklyWorkHours;
    }

    public SystemConfig weeklyWorkHours(Double weeklyWorkHours) {
        this.setWeeklyWorkHours(weeklyWorkHours);
        return this;
    }

    public void setWeeklyWorkHours(Double weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public Double getOvertimeNormalRate() {
        return this.overtimeNormalRate;
    }

    public SystemConfig overtimeNormalRate(Double overtimeNormalRate) {
        this.setOvertimeNormalRate(overtimeNormalRate);
        return this;
    }

    public void setOvertimeNormalRate(Double overtimeNormalRate) {
        this.overtimeNormalRate = overtimeNormalRate;
    }

    public Double getOvertimeSpecialRate() {
        return this.overtimeSpecialRate;
    }

    public SystemConfig overtimeSpecialRate(Double overtimeSpecialRate) {
        this.setOvertimeSpecialRate(overtimeSpecialRate);
        return this;
    }

    public void setOvertimeSpecialRate(Double overtimeSpecialRate) {
        this.overtimeSpecialRate = overtimeSpecialRate;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public SystemConfig lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((SystemConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemConfig{" +
            "id=" + getId() +
            ", dailyWorkHours=" + getDailyWorkHours() +
            ", weeklyWorkHours=" + getWeeklyWorkHours() +
            ", overtimeNormalRate=" + getOvertimeNormalRate() +
            ", overtimeSpecialRate=" + getOvertimeSpecialRate() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
