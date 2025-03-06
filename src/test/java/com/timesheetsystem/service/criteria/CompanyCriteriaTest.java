package com.timesheetsystem.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CompanyCriteriaTest {

    @Test
    void newCompanyCriteriaHasAllFiltersNullTest() {
        var companyCriteria = new CompanyCriteria();
        assertThat(companyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void companyCriteriaFluentMethodsCreatesFiltersTest() {
        var companyCriteria = new CompanyCriteria();

        setAllFilters(companyCriteria);

        assertThat(companyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void companyCriteriaCopyCreatesNullFilterTest() {
        var companyCriteria = new CompanyCriteria();
        var copy = companyCriteria.copy();

        assertThat(companyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(companyCriteria)
        );
    }

    @Test
    void companyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var companyCriteria = new CompanyCriteria();
        setAllFilters(companyCriteria);

        var copy = companyCriteria.copy();

        assertThat(companyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(companyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var companyCriteria = new CompanyCriteria();

        assertThat(companyCriteria).hasToString("CompanyCriteria{}");
    }

    private static void setAllFilters(CompanyCriteria companyCriteria) {
        companyCriteria.id();
        companyCriteria.name();
        companyCriteria.cnpj();
        companyCriteria.active();
        companyCriteria.createdAt();
        companyCriteria.updatedAt();
        companyCriteria.companyAllowanceId();
        companyCriteria.distinct();
    }

    private static Condition<CompanyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCnpj()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCompanyAllowanceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CompanyCriteria> copyFiltersAre(CompanyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCnpj(), copy.getCnpj()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCompanyAllowanceId(), copy.getCompanyAllowanceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
