package com.timesheetsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserPreferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserPreference getUserPreferenceSample1() {
        return new UserPreference().id(1L).defaultCompanyId(1L).reportFrequency("reportFrequency1").weekStartDay(1);
    }

    public static UserPreference getUserPreferenceSample2() {
        return new UserPreference().id(2L).defaultCompanyId(2L).reportFrequency("reportFrequency2").weekStartDay(2);
    }

    public static UserPreference getUserPreferenceRandomSampleGenerator() {
        return new UserPreference()
            .id(longCount.incrementAndGet())
            .defaultCompanyId(longCount.incrementAndGet())
            .reportFrequency(UUID.randomUUID().toString())
            .weekStartDay(intCount.incrementAndGet());
    }
}
