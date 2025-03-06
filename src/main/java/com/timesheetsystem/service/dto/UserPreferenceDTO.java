package com.timesheetsystem.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.timesheetsystem.domain.UserPreference} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreferenceDTO implements Serializable {

    private Long id;

    @NotNull
    private Long defaultCompanyId;

    @NotNull
    private Boolean emailNotifications;

    @NotNull
    private Boolean smsNotifications;

    @NotNull
    private Boolean pushNotifications;

    private String reportFrequency;

    @Min(value = 0)
    @Max(value = 6)
    private Integer weekStartDay;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefaultCompanyId() {
        return defaultCompanyId;
    }

    public void setDefaultCompanyId(Long defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public String getReportFrequency() {
        return reportFrequency;
    }

    public void setReportFrequency(String reportFrequency) {
        this.reportFrequency = reportFrequency;
    }

    public Integer getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(Integer weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreferenceDTO)) {
            return false;
        }

        UserPreferenceDTO userPreferenceDTO = (UserPreferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userPreferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferenceDTO{" +
            "id=" + getId() +
            ", defaultCompanyId=" + getDefaultCompanyId() +
            ", emailNotifications='" + getEmailNotifications() + "'" +
            ", smsNotifications='" + getSmsNotifications() + "'" +
            ", pushNotifications='" + getPushNotifications() + "'" +
            ", reportFrequency='" + getReportFrequency() + "'" +
            ", weekStartDay=" + getWeekStartDay() +
            ", user=" + getUser() +
            "}";
    }
}
