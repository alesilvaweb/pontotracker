package com.timesheetsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimesheetAuditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimesheetAudit getTimesheetAuditSample1() {
        return new TimesheetAudit()
            .id(1L)
            .entityType("entityType1")
            .entityId(1L)
            .action("action1")
            .userId(1L)
            .oldValues("oldValues1")
            .newValues("newValues1");
    }

    public static TimesheetAudit getTimesheetAuditSample2() {
        return new TimesheetAudit()
            .id(2L)
            .entityType("entityType2")
            .entityId(2L)
            .action("action2")
            .userId(2L)
            .oldValues("oldValues2")
            .newValues("newValues2");
    }

    public static TimesheetAudit getTimesheetAuditRandomSampleGenerator() {
        return new TimesheetAudit()
            .id(longCount.incrementAndGet())
            .entityType(UUID.randomUUID().toString())
            .entityId(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .userId(longCount.incrementAndGet())
            .oldValues(UUID.randomUUID().toString())
            .newValues(UUID.randomUUID().toString());
    }
}
