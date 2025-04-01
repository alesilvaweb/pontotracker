package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesheetAlertAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAlertAllPropertiesEquals(TimesheetAlert expected, TimesheetAlert actual) {
        assertTimesheetAlertAutoGeneratedPropertiesEquals(expected, actual);
        assertTimesheetAlertAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAlertAllUpdatablePropertiesEquals(TimesheetAlert expected, TimesheetAlert actual) {
        assertTimesheetAlertUpdatableFieldsEquals(expected, actual);
        assertTimesheetAlertUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAlertAutoGeneratedPropertiesEquals(TimesheetAlert expected, TimesheetAlert actual) {
        assertThat(expected)
            .as("Verify TimesheetAlert auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAlertUpdatableFieldsEquals(TimesheetAlert expected, TimesheetAlert actual) {
        assertThat(expected)
            .as("Verify TimesheetAlert relevant properties")
            .satisfies(e -> assertThat(e.getUserId()).as("check userId").isEqualTo(actual.getUserId()))
            .satisfies(e -> assertThat(e.getTimesheetId()).as("check timesheetId").isEqualTo(actual.getTimesheetId()))
            .satisfies(e -> assertThat(e.getDate()).as("check date").isEqualTo(actual.getDate()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getMessage()).as("check message").isEqualTo(actual.getMessage()))
            .satisfies(e -> assertThat(e.getSeverity()).as("check severity").isEqualTo(actual.getSeverity()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(
                e ->
                    assertThat(e.getCreatedAt())
                        .as("check createdAt")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getCreatedAt())
            )
            .satisfies(
                e ->
                    assertThat(e.getResolvedAt())
                        .as("check resolvedAt")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getResolvedAt())
            )
            .satisfies(e -> assertThat(e.getResolution()).as("check resolution").isEqualTo(actual.getResolution()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAlertUpdatableRelationshipsEquals(TimesheetAlert expected, TimesheetAlert actual) {}
}
