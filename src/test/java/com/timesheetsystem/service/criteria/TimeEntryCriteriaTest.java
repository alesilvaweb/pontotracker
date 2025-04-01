package com.timesheetsystem.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TimeEntryCriteriaTest {

    @Test
    void newTimeEntryCriteriaHasAllFiltersNullTest() {
        var timeEntryCriteria = new TimeEntryCriteria();
        assertThat(timeEntryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void timeEntryCriteriaFluentMethodsCreatesFiltersTest() {
        var timeEntryCriteria = new TimeEntryCriteria();

        setAllFilters(timeEntryCriteria);

        assertThat(timeEntryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void timeEntryCriteriaCopyCreatesNullFilterTest() {
        var timeEntryCriteria = new TimeEntryCriteria();
        var copy = timeEntryCriteria.copy();

        assertThat(timeEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(timeEntryCriteria)
        );
    }

    @Test
    void timeEntryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var timeEntryCriteria = new TimeEntryCriteria();
        setAllFilters(timeEntryCriteria);

        var copy = timeEntryCriteria.copy();

        assertThat(timeEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(timeEntryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var timeEntryCriteria = new TimeEntryCriteria();

        assertThat(timeEntryCriteria).hasToString("TimeEntryCriteria{}");
    }

    private static void setAllFilters(TimeEntryCriteria timeEntryCriteria) {
        timeEntryCriteria.id();
        timeEntryCriteria.startTime();
        timeEntryCriteria.endTime();
        timeEntryCriteria.type();
        timeEntryCriteria.overtimeCategory();
        timeEntryCriteria.description();
        timeEntryCriteria.hoursWorked();
        timeEntryCriteria.timesheetId();
        timeEntryCriteria.distinct();
    }

    private static Condition<TimeEntryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getOvertimeCategory()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getHoursWorked()) &&
                condition.apply(criteria.getTimesheetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TimeEntryCriteria> copyFiltersAre(TimeEntryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getOvertimeCategory(), copy.getOvertimeCategory()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getHoursWorked(), copy.getHoursWorked()) &&
                condition.apply(criteria.getTimesheetId(), copy.getTimesheetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
