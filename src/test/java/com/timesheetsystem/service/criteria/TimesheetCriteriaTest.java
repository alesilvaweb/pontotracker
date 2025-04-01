package com.timesheetsystem.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TimesheetCriteriaTest {

    @Test
    void newTimesheetCriteriaHasAllFiltersNullTest() {
        var timesheetCriteria = new TimesheetCriteria();
        assertThat(timesheetCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void timesheetCriteriaFluentMethodsCreatesFiltersTest() {
        var timesheetCriteria = new TimesheetCriteria();

        setAllFilters(timesheetCriteria);

        assertThat(timesheetCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void timesheetCriteriaCopyCreatesNullFilterTest() {
        var timesheetCriteria = new TimesheetCriteria();
        var copy = timesheetCriteria.copy();

        assertThat(timesheetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetCriteria)
        );
    }

    @Test
    void timesheetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var timesheetCriteria = new TimesheetCriteria();
        setAllFilters(timesheetCriteria);

        var copy = timesheetCriteria.copy();

        assertThat(timesheetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var timesheetCriteria = new TimesheetCriteria();

        assertThat(timesheetCriteria).hasToString("TimesheetCriteria{}");
    }

    private static void setAllFilters(TimesheetCriteria timesheetCriteria) {
        timesheetCriteria.id();
        timesheetCriteria.date();
        timesheetCriteria.modality();
        timesheetCriteria.classification();
        timesheetCriteria.description();
        timesheetCriteria.totalHours();
        timesheetCriteria.overtimeHours();
        timesheetCriteria.allowanceValue();
        timesheetCriteria.status();
        timesheetCriteria.createdAt();
        timesheetCriteria.updatedAt();
        timesheetCriteria.approvedAt();
        timesheetCriteria.approvedBy();
        timesheetCriteria.timeEntryId();
        timesheetCriteria.userId();
        timesheetCriteria.companyId();
        timesheetCriteria.distinct();
    }

    private static Condition<TimesheetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getModality()) &&
                condition.apply(criteria.getClassification()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTotalHours()) &&
                condition.apply(criteria.getOvertimeHours()) &&
                condition.apply(criteria.getAllowanceValue()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getApprovedAt()) &&
                condition.apply(criteria.getApprovedBy()) &&
                condition.apply(criteria.getTimeEntryId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getCompanyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TimesheetCriteria> copyFiltersAre(TimesheetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getModality(), copy.getModality()) &&
                condition.apply(criteria.getClassification(), copy.getClassification()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTotalHours(), copy.getTotalHours()) &&
                condition.apply(criteria.getOvertimeHours(), copy.getOvertimeHours()) &&
                condition.apply(criteria.getAllowanceValue(), copy.getAllowanceValue()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getApprovedAt(), copy.getApprovedAt()) &&
                condition.apply(criteria.getApprovedBy(), copy.getApprovedBy()) &&
                condition.apply(criteria.getTimeEntryId(), copy.getTimeEntryId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getCompanyId(), copy.getCompanyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
