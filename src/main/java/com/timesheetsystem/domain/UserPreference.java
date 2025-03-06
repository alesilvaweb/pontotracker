package com.timesheetsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserPreference.
 */
@Entity
@Table(name = "user_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "default_company_id", nullable = false)
    private Long defaultCompanyId;

    @NotNull
    @Column(name = "email_notifications", nullable = false)
    private Boolean emailNotifications;

    @NotNull
    @Column(name = "sms_notifications", nullable = false)
    private Boolean smsNotifications;

    @NotNull
    @Column(name = "push_notifications", nullable = false)
    private Boolean pushNotifications;

    @Column(name = "report_frequency")
    private String reportFrequency;

    @Min(value = 0)
    @Max(value = 6)
    @Column(name = "week_start_day")
    private Integer weekStartDay;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPreference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefaultCompanyId() {
        return this.defaultCompanyId;
    }

    public UserPreference defaultCompanyId(Long defaultCompanyId) {
        this.setDefaultCompanyId(defaultCompanyId);
        return this;
    }

    public void setDefaultCompanyId(Long defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId;
    }

    public Boolean getEmailNotifications() {
        return this.emailNotifications;
    }

    public UserPreference emailNotifications(Boolean emailNotifications) {
        this.setEmailNotifications(emailNotifications);
        return this;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return this.smsNotifications;
    }

    public UserPreference smsNotifications(Boolean smsNotifications) {
        this.setSmsNotifications(smsNotifications);
        return this;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getPushNotifications() {
        return this.pushNotifications;
    }

    public UserPreference pushNotifications(Boolean pushNotifications) {
        this.setPushNotifications(pushNotifications);
        return this;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public String getReportFrequency() {
        return this.reportFrequency;
    }

    public UserPreference reportFrequency(String reportFrequency) {
        this.setReportFrequency(reportFrequency);
        return this;
    }

    public void setReportFrequency(String reportFrequency) {
        this.reportFrequency = reportFrequency;
    }

    public Integer getWeekStartDay() {
        return this.weekStartDay;
    }

    public UserPreference weekStartDay(Integer weekStartDay) {
        this.setWeekStartDay(weekStartDay);
        return this;
    }

    public void setWeekStartDay(Integer weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPreference user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreference)) {
            return false;
        }
        return getId() != null && getId().equals(((UserPreference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreference{" +
            "id=" + getId() +
            ", defaultCompanyId=" + getDefaultCompanyId() +
            ", emailNotifications='" + getEmailNotifications() + "'" +
            ", smsNotifications='" + getSmsNotifications() + "'" +
            ", pushNotifications='" + getPushNotifications() + "'" +
            ", reportFrequency='" + getReportFrequency() + "'" +
            ", weekStartDay=" + getWeekStartDay() +
            "}";
    }
}
