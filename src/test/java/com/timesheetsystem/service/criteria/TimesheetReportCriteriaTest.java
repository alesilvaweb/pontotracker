package com.timesheetsystem.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TimesheetReportCriteriaTest {

    @Test
    void newTimesheetReportCriteriaHasAllFiltersNullTest() {
        var timesheetReportCriteria = new TimesheetReportCriteria();
        assertThat(timesheetReportCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void timesheetReportCriteriaFluentMethodsCreatesFiltersTest() {
        var timesheetReportCriteria = new TimesheetReportCriteria();

        setAllFilters(timesheetReportCriteria);

        assertThat(timesheetReportCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void timesheetReportCriteriaCopyCreatesNullFilterTest() {
        var timesheetReportCriteria = new TimesheetReportCriteria();
        var copy = timesheetReportCriteria.copy();

        assertThat(timesheetReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetReportCriteria)
        );
    }

    @Test
    void timesheetReportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var timesheetReportCriteria = new TimesheetReportCriteria();
        setAllFilters(timesheetReportCriteria);

        var copy = timesheetReportCriteria.copy();

        assertThat(timesheetReportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(timesheetReportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var timesheetReportCriteria = new TimesheetReportCriteria();

        assertThat(timesheetReportCriteria).hasToString("TimesheetReportCriteria{}");
    }

    private static void setAllFilters(TimesheetReportCriteria timesheetReportCriteria) {
        timesheetReportCriteria.id();
        timesheetReportCriteria.userId();
        timesheetReportCriteria.userName();
        timesheetReportCriteria.companyId();
        timesheetReportCriteria.companyName();
        timesheetReportCriteria.startDate();
        timesheetReportCriteria.endDate();
        timesheetReportCriteria.totalRegularHours();
        timesheetReportCriteria.totalOvertimeHours();
        timesheetReportCriteria.totalAllowance();
        timesheetReportCriteria.status();
        timesheetReportCriteria.generatedAt();
        timesheetReportCriteria.approvedAt();
        timesheetReportCriteria.comments();
        timesheetReportCriteria.generatedById();
        timesheetReportCriteria.approvedById();
        timesheetReportCriteria.distinct();
    }

    private static Condition<TimesheetReportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getUserName()) &&
                condition.apply(criteria.getCompanyId()) &&
                condition.apply(criteria.getCompanyName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getTotalRegularHours()) &&
                condition.apply(criteria.getTotalOvertimeHours()) &&
                condition.apply(criteria.getTotalAllowance()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getGeneratedAt()) &&
                condition.apply(criteria.getApprovedAt()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getGeneratedById()) &&
                condition.apply(criteria.getApprovedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TimesheetReportCriteria> copyFiltersAre(
        TimesheetReportCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getUserName(), copy.getUserName()) &&
                condition.apply(criteria.getCompanyId(), copy.getCompanyId()) &&
                condition.apply(criteria.getCompanyName(), copy.getCompanyName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getTotalRegularHours(), copy.getTotalRegularHours()) &&
                condition.apply(criteria.getTotalOvertimeHours(), copy.getTotalOvertimeHours()) &&
                condition.apply(criteria.getTotalAllowance(), copy.getTotalAllowance()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getGeneratedAt(), copy.getGeneratedAt()) &&
                condition.apply(criteria.getApprovedAt(), copy.getApprovedAt()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getGeneratedById(), copy.getGeneratedById()) &&
                condition.apply(criteria.getApprovedById(), copy.getApprovedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
