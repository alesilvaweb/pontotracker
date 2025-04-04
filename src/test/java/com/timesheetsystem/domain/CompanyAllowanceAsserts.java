package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyAllowanceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllowanceAllPropertiesEquals(CompanyAllowance expected, CompanyAllowance actual) {
        assertCompanyAllowanceAutoGeneratedPropertiesEquals(expected, actual);
        assertCompanyAllowanceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllowanceAllUpdatablePropertiesEquals(CompanyAllowance expected, CompanyAllowance actual) {
        assertCompanyAllowanceUpdatableFieldsEquals(expected, actual);
        assertCompanyAllowanceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllowanceAutoGeneratedPropertiesEquals(CompanyAllowance expected, CompanyAllowance actual) {
        assertThat(expected)
            .as("Verify CompanyAllowance auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllowanceUpdatableFieldsEquals(CompanyAllowance expected, CompanyAllowance actual) {
        assertThat(expected)
            .as("Verify CompanyAllowance relevant properties")
            .satisfies(
                e ->
                    assertThat(e.getPresentialAllowanceValue())
                        .as("check presentialAllowanceValue")
                        .isEqualTo(actual.getPresentialAllowanceValue())
            )
            .satisfies(
                e -> assertThat(e.getRemoteAllowanceValue()).as("check remoteAllowanceValue").isEqualTo(actual.getRemoteAllowanceValue())
            )
            .satisfies(
                e ->
                    assertThat(e.getFullTimeThresholdHours())
                        .as("check fullTimeThresholdHours")
                        .isEqualTo(actual.getFullTimeThresholdHours())
            )
            .satisfies(
                e ->
                    assertThat(e.getPartTimeAllowancePercentage())
                        .as("check partTimeAllowancePercentage")
                        .isEqualTo(actual.getPartTimeAllowancePercentage())
            )
            .satisfies(
                e -> assertThat(e.getConsiderWorkDistance()).as("check considerWorkDistance").isEqualTo(actual.getConsiderWorkDistance())
            )
            .satisfies(
                e ->
                    assertThat(e.getMinimumDistanceToReceive())
                        .as("check minimumDistanceToReceive")
                        .isEqualTo(actual.getMinimumDistanceToReceive())
            )
            .satisfies(e -> assertThat(e.getActive()).as("check active").isEqualTo(actual.getActive()))
            .satisfies(
                e ->
                    assertThat(e.getLastUpdated())
                        .as("check lastUpdated")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getLastUpdated())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompanyAllowanceUpdatableRelationshipsEquals(CompanyAllowance expected, CompanyAllowance actual) {
        assertThat(expected)
            .as("Verify CompanyAllowance relationships")
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()));
    }
}
