package com.christian.rossi.progetto_tiw_2023.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeHandler {

    public static List<Integer> getTimeDifference (Timestamp time, long loginTime) {
        final long day, hour, minute;
        List<Integer> remaining = new ArrayList<>();
        long sec = time.getTime();
        long milliseconds = sec - loginTime;
        if (milliseconds > 0) {
            day = TimeUnit.MILLISECONDS.toDays(milliseconds);
            hour = TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
            minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        }
        else {
            day = 0;
            hour = 0;
            minute = 0;
        }
        remaining.add((int) day);
        remaining.add((int) hour);
        remaining.add((int) minute);
        return remaining;
    }
}