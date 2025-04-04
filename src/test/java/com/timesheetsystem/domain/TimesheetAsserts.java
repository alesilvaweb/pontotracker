package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesheetAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAllPropertiesEquals(Timesheet expected, Timesheet actual) {
        assertTimesheetAutoGeneratedPropertiesEquals(expected, actual);
        assertTimesheetAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAllUpdatablePropertiesEquals(Timesheet expected, Timesheet actual) {
        assertTimesheetUpdatableFieldsEquals(expected, actual);
        assertTimesheetUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetAutoGeneratedPropertiesEquals(Timesheet expected, Timesheet actual) {
        assertThat(expected)
            .as("Verify Timesheet auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetUpdatableFieldsEquals(Timesheet expected, Timesheet actual) {
        assertThat(expected)
            .as("Verify Timesheet relevant properties")
            .satisfies(e -> assertThat(e.getDate()).as("check date").isEqualTo(actual.getDate()))
            .satisfies(e -> assertThat(e.getModality()).as("check modality").isEqualTo(actual.getModality()))
            .satisfies(e -> assertThat(e.getClassification()).as("check classification").isEqualTo(actual.getClassification()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getTotalHours()).as("check totalHours").isEqualTo(actual.getTotalHours()))
            .satisfies(e -> assertThat(e.getOvertimeHours()).as("check overtimeHours").isEqualTo(actual.getOvertimeHours()))
            .satisfies(e -> assertThat(e.getAllowanceValue()).as("check allowanceValue").isEqualTo(actual.getAllowanceValue()))
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
                    assertThat(e.getUpdatedAt())
                        .as("check updatedAt")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getUpdatedAt())
            )
            .satisfies(
                e ->
                    assertThat(e.getApprovedAt())
                        .as("check approvedAt")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getApprovedAt())
            )
            .satisfies(e -> assertThat(e.getApprovedBy()).as("check approvedBy").isEqualTo(actual.getApprovedBy()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimesheetUpdatableRelationshipsEquals(Timesheet expected, Timesheet actual) {
        assertThat(expected)
            .as("Verify Timesheet relationships")
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()));
    }
}
