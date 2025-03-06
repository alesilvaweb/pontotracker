package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimeEntryAllPropertiesEquals(TimeEntry expected, TimeEntry actual) {
        assertTimeEntryAutoGeneratedPropertiesEquals(expected, actual);
        assertTimeEntryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimeEntryAllUpdatablePropertiesEquals(TimeEntry expected, TimeEntry actual) {
        assertTimeEntryUpdatableFieldsEquals(expected, actual);
        assertTimeEntryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimeEntryAutoGeneratedPropertiesEquals(TimeEntry expected, TimeEntry actual) {
        assertThat(expected)
            .as("Verify TimeEntry auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimeEntryUpdatableFieldsEquals(TimeEntry expected, TimeEntry actual) {
        assertThat(expected)
            .as("Verify TimeEntry relevant properties")
            .satisfies(
                e ->
                    assertThat(e.getStartTime())
                        .as("check startTime")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getStartTime())
            )
            .satisfies(
                e -> assertThat(e.getEndTime()).as("check endTime").usingComparator(zonedDataTimeSameInstant).isEqualTo(actual.getEndTime())
            )
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getOvertimeCategory()).as("check overtimeCategory").isEqualTo(actual.getOvertimeCategory()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getHoursWorked()).as("check hoursWorked").isEqualTo(actual.getHoursWorked()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTimeEntryUpdatableRelationshipsEquals(TimeEntry expected, TimeEntry actual) {
        assertThat(expected)
            .as("Verify TimeEntry relationships")
            .satisfies(e -> assertThat(e.getTimesheet()).as("check timesheet").isEqualTo(actual.getTimesheet()));
    }
}
