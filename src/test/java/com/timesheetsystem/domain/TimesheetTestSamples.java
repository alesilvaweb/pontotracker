package com.timesheetsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimesheetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Timesheet getTimesheetSample1() {
        return new Timesheet()
            .id(1L)
            .classification("classification1")
            .description("description1")
            .status("status1")
            .approvedBy("approvedBy1");
    }

    public static Timesheet getTimesheetSample2() {
        return new Timesheet()
            .id(2L)
            .classification("classification2")
            .description("description2")
            .status("status2")
            .approvedBy("approvedBy2");
    }

    public static Timesheet getTimesheetRandomSampleGenerator() {
        return new Timesheet()
            .id(longCount.incrementAndGet())
            .classification(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString());
    }
}
