package com.timesheetsystem.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SystemConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemConfig getSystemConfigSample1() {
        return new SystemConfig().id(1L);
    }

    public static SystemConfig getSystemConfigSample2() {
        return new SystemConfig().id(2L);
    }

    public static SystemConfig getSystemConfigRandomSampleGenerator() {
        return new SystemConfig().id(longCount.incrementAndGet());
    }
}
