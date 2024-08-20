import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import net.codejava.utility.RentalCalculateUtil;

public class RentalCalculateUtilTest {

    @InjectMocks
    private RentalCalculateUtil rentalCalculateUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateMoney_FullDay() throws ParseException {
        // Date startDateTime = new Date();
        // Date endDateTime = new Date(startDateTime.getTime() + TimeUnit.DAYS.toMillis(1)); // 24 hours later
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date startDateTime = formatter.parse("2024-07-29 17:41:55.1");
        Date endDateTime = formatter.parse("2024-07-31 17:42:55.1");
        Double basicPrice = 2.4;

        Double result = rentalCalculateUtil.calculateMoney(startDateTime, endDateTime, basicPrice);
        System.out.println(result);
        assertEquals(4.8, result); // 24 hours * (240.0 / 24) = 10.0
    }

    @Test
    public void testCalculateMoney_HalfDay() {
        Date startDateTime = new Date();
        Date endDateTime = new Date(startDateTime.getTime() + TimeUnit.HOURS.toMillis(12)); // 12 hours later
        Double basicPrice = 240.0;

        Double result = rentalCalculateUtil.calculateMoney(startDateTime, endDateTime, basicPrice);

        assertEquals(5.0, result); // 12 hours * (240.0 / 24) = 5.0
    }

    @Test
    public void testCalculateMoney_QuarterDay() {
        Date startDateTime = new Date();
        Date endDateTime = new Date(startDateTime.getTime() + TimeUnit.HOURS.toMillis(6)); // 6 hours later
        Double basicPrice = 240.0;

        Double result = rentalCalculateUtil.calculateMoney(startDateTime, endDateTime, basicPrice);

        assertEquals(2.5, result); // 6 hours * (240.0 / 24) = 2.5
    }

    @Test
    public void testCalculateMoney_NoTimeDifference() {
        Date startDateTime = new Date();
        Date endDateTime = new Date(startDateTime.getTime()); // same time
        Double basicPrice = 240.0;

        Double result = rentalCalculateUtil.calculateMoney(startDateTime, endDateTime, basicPrice);

        assertEquals(0.0, result); // 0 hours * (240.0 / 24) = 0.0
    }
}
