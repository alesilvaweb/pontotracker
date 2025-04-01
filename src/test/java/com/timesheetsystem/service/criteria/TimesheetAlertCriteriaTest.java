package com.timesheetsystem.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TimesheetAlertCriteriaTest {

    @Test
    void newTimesheetAlertCriteriaHasAllFiltersNullTest() {
        var timesheetAlertCriteria = new TimesheetAlertCriteria();
        assertThat(timesheetAlertCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void timesheetAlertCriteriaFluentMethodsCreatesFiltersTest() {
        var timesheetAlertCriteria = new TimesheetAlertCriteria();

        setAllFilters(timesheetAlertCriteria);

        assertThat(timesheetAlertCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void timesheetAlertCriteriaCopyCreatesNullFilterTest() {
        var timesheetAlertCriteria = new TimesheetAlertCriteria();
        var copy = timesheetAlertCriteria.copy();

        assertThat(timesheetAlertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetAlertCriteria)
        );
    }

    @Test
    void timesheetAlertCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var timesheetAlertCriteria = new TimesheetAlertCriteria();
        setAllFilters(timesheetAlertCriteria);

        var copy = timesheetAlertCriteria.copy();

        assertThat(timesheetAlertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetAlertCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var timesheetAlertCriteria = new TimesheetAlertCriteria();

        assertThat(timesheetAlertCriteria).hasToString("TimesheetAlertCriteria{}");
    }

    private static void setAllFilters(TimesheetAlertCriteria timesheetAlertCriteria) {
        timesheetAlertCriteria.id();
        timesheetAlertCriteria.userId();
        timesheetAlertCriteria.timesheetId();
        timesheetAlertCriteria.date();
        timesheetAlertCriteria.type();
        timesheetAlertCriteria.message();
        timesheetAlertCriteria.severity();
        timesheetAlertCriteria.status();
        timesheetAlertCriteria.createdAt();
        timesheetAlertCriteria.resolvedAt();
        timesheetAlertCriteria.resolution();
        timesheetAlertCriteria.resolvedById();
        timesheetAlertCriteria.distinct();
    }

    private static Condition<TimesheetAlertCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getTimesheetId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getSeverity()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getResolvedAt()) &&
                condition.apply(criteria.getResolution()) &&
                condition.apply(criteria.getResolvedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TimesheetAlertCriteria> copyFiltersAre(
        TimesheetAlertCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getTimesheetId(), copy.getTimesheetId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getSeverity(), copy.getSeverity()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getResolvedAt(), copy.getResolvedAt()) &&
                condition.apply(criteria.getResolution(), copy.getResolution()) &&
                condition.apply(criteria.getResolvedById(), copy.getResolvedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
