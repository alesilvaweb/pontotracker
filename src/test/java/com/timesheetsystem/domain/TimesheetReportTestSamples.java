package com.timesheetsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimesheetReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimesheetReport getTimesheetReportSample1() {
        return new TimesheetReport()
            .id(1L)
            .userId(1L)
            .userName("userName1")
            .companyId(1L)
            .companyName("companyName1")
            .status("status1")
            .comments("comments1");
    }

    public static TimesheetReport getTimesheetReportSample2() {
        return new TimesheetReport()
            .id(2L)
            .userId(2L)
            .userName("userName2")
            .companyId(2L)
            .companyName("companyName2")
            .status("status2")
            .comments("comments2");
    }

    public static TimesheetReport getTimesheetReportRandomSampleGenerator() {
        return new TimesheetReport()
            .id(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .companyId(longCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString());
    }
}
