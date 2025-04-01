package com.timesheetsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimesheetAlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimesheetAlert getTimesheetAlertSample1() {
        return new TimesheetAlert()
            .id(1L)
            .userId(1L)
            .timesheetId(1L)
            .type("type1")
            .message("message1")
            .severity("severity1")
            .status("status1")
            .resolution("resolution1");
    }

    public static TimesheetAlert getTimesheetAlertSample2() {
        return new TimesheetAlert()
            .id(2L)
            .userId(2L)
            .timesheetId(2L)
            .type("type2")
            .message("message2")
            .severity("severity2")
            .status("status2")
            .resolution("resolution2");
    }

    public static TimesheetAlert getTimesheetAlertRandomSampleGenerator() {
        return new TimesheetAlert()
            .id(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .timesheetId(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .severity(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .resolution(UUID.randomUUID().toString());
    }
}
