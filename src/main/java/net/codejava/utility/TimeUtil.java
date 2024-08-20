package net.codejava.utility;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.codejava.constant.TimeFormatConstant;

public class TimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TimeFormatConstant.DATETIME_FORMAT);

    public static LocalDateTime convertToDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    public static String formatToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static int getHoursDifference(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        System.out.println(duration.toMinutes());
        Double hours = duration.toMinutes() / 60.0;
        return (int) Math.round(hours);
    }
}
