package net.codejava.utility;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RentalCalculateUtil {
    public static Long calculateHour(Date startDateTime, Date endDateTime) {
        Long differenceInMillis = endDateTime.getTime() - startDateTime.getTime();
        return TimeUnit.MILLISECONDS.toHours(differenceInMillis);
    }

    public static Double calculateMoney(Date startDateTime, Date endDateTime, Double basicPrice) {
        Long differenceInHour = calculateHour(startDateTime, endDateTime);
        return differenceInHour * (basicPrice / 24);
    }
}
