import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import net.codejava.utility.TimeUtil;

public class TimeUtilTest {
    @Test
    public void getHoursDifferent() {
        String time1 = "2023-11-04 14:00";
        String time2 = "2023-11-05 14:29";
        LocalDateTime t1 = TimeUtil.convertToDateTime(time1);
        LocalDateTime t2 = TimeUtil.convertToDateTime(time2);
        int temp = TimeUtil.getHoursDifference(t1, t2);
        System.out.println(t2.isAfter(t1));
    }
}
