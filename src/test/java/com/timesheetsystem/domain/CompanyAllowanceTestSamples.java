package com.timesheetsystem.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyAllowanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CompanyAllowance getCompanyAllowanceSample1() {
        return new CompanyAllowance().id(1L).partTimeAllowancePercentage(1).minimumDistanceToReceive(1);
    }

    public static CompanyAllowance getCompanyAllowanceSample2() {
        return new CompanyAllowance().id(2L).partTimeAllowancePercentage(2).minimumDistanceToReceive(2);
    }

    public static CompanyAllowance getCompanyAllowanceRandomSampleGenerator() {
        return new CompanyAllowance()
            .id(longCount.incrementAndGet())
            .partTimeAllowancePercentage(intCount.incrementAndGet())
            .minimumDistanceToReceive(intCount.incrementAndGet());
    }
}
