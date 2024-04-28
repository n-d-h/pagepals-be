package com.pagepal.capstone.utils;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateUtils {
    private static final TimeZone VIETNAM_TIME_ZONE = TimeZone.getTimeZone("GMT+7");

    public Date getCurrentVietnamDate() {
        Calendar calendar = Calendar.getInstance(VIETNAM_TIME_ZONE);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        return calendar.getTime();
    }

    public Date formatDate(Date date) {
        Calendar calendar = Calendar.getInstance(VIETNAM_TIME_ZONE);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        return calendar.getTime();
    }
}
