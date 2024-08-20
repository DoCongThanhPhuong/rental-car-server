import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.codejava.domain.entity.Booking;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.utility.TimeUtil;

public class CheckRentalCar {
    @Test
    public void checkBooking() {
        List<Booking> bookingList = new ArrayList<>();

        // Booking 1
        Booking b1 = new Booking();
        b1.setId(1);
        b1.setStartDateTime(TimeUtil.convertToDateTime("2024-08-13 07:00"));
        b1.setEndDateTime(TimeUtil.convertToDateTime("2024-08-13 08:00"));
        b1.setStatus(BookingStatus.COMPLETED);
        // Booking 2
        Booking b2 = new Booking();
        b2.setId(2);
        b2.setStartDateTime(TimeUtil.convertToDateTime("2024-08-13 09:00"));
        b2.setEndDateTime(TimeUtil.convertToDateTime("2024-08-13 10:00"));
        b2.setStatus(BookingStatus.CANCELLED);
        // Booking 3
        Booking b3 = new Booking();
        b3.setId(3);
        b3.setStartDateTime(TimeUtil.convertToDateTime("2024-08-15 07:00"));
        b3.setEndDateTime(TimeUtil.convertToDateTime("2024-08-15 09:00"));
        b3.setStatus(BookingStatus.CONFIRMED);
        // Booking 4
        Booking b4 = new Booking();
        b4.setId(4);
        b4.setStartDateTime(TimeUtil.convertToDateTime("2024-08-15 13:00"));
        b4.setEndDateTime(TimeUtil.convertToDateTime("2024-08-15 15:00"));
        b4.setStatus(BookingStatus.CONFIRMED);
        // Booking 5
        Booking b5 = new Booking();
        b5.setId(5);
        b5.setStartDateTime(TimeUtil.convertToDateTime("2024-08-15 20:00"));
        b5.setEndDateTime(TimeUtil.convertToDateTime("2024-08-15 22:00"));
        b5.setStatus(BookingStatus.CONFIRMED);
        // Booking 3.1 (6)
        Booking b6 = new Booking();
        b6.setId(6);
        b6.setStartDateTime(TimeUtil.convertToDateTime("2024-08-15 12:00"));
        b6.setEndDateTime(TimeUtil.convertToDateTime("2024-08-15 14:00"));
        b6.setStatus(BookingStatus.CANCELLED);
        // Booking 5.1 (7)
        Booking b7 = new Booking();
        b7.setId(7);
        b7.setStartDateTime(TimeUtil.convertToDateTime("2024-08-15 16:00"));
        b7.setEndDateTime(TimeUtil.convertToDateTime("2024-08-15 17:00"));
        b7.setStatus(BookingStatus.COMPLETED);

        bookingList.add(b1);
        bookingList.add(b2);
        bookingList.add(b5);
        bookingList.add(b3);
        bookingList.add(b4);
        bookingList.add(b6);
        bookingList.add(b7);

        Boolean res = false;
        String startTime = "2024-08-15 09:10";
        String endTime = "2024-08-15 12:50";
        LocalDateTime st = TimeUtil.convertToDateTime(startTime);
        LocalDateTime et = TimeUtil.convertToDateTime(endTime);

        //        List<Booking> listBookingCompare = bookingList.stream().map(
        ////                temp -> {
        ////                    if ((temp.getStatus() != BookingStatus.COMPLETED || temp.getStatus() !=
        // BookingStatus.CANCELLED)
        ////                            && ((temp.getStartDateTime().isAfter(st))
        ////                                || (temp.getStartDateTime().isBefore(st) &&
        // temp.getEndDateTime().isBefore(st))))
        ////                        return
        ////                }
        //                temp -> {
        //                    if ((temp.getStatus() != BookingStatus.COMPLETED && temp.getStatus() !=
        // BookingStatus.CANCELLED)
        //                        && temp.getEndDateTime().isAfter(st))
        //                        return temp;
        //                }
        //        ).toList();
        // bookingList.forEach(booking -> System.out.println("ID: "+booking.getId()));
        List<Booking> listBookingCompare = bookingList.stream()
                .filter(temp -> (temp.getStatus() != BookingStatus.COMPLETED
                        && temp.getStatus() != BookingStatus.CANCELLED
                        && temp.getEndDateTime().isAfter(st)))
                .toList();

        System.out.println("List Comapre: " + listBookingCompare.size());
        if (listBookingCompare.size() == 0) res = true;
        else {
            listBookingCompare.forEach(booking -> System.out.println("ID: " + booking.getId()));
            List<Booking> listSortedBooking = listBookingCompare.stream()
                    .sorted(Comparator.comparing(Booking::getStartDateTime))
                    .toList();
            listSortedBooking.forEach(booking -> System.out.println("ID before: " + booking.getId()));

            if (listSortedBooking.getFirst().getStartDateTime().isAfter(et)) res = true;
            else {
                Integer n = listSortedBooking.size();
                for (Integer i = 0; i < n - 1; i++) {
                    if (listSortedBooking.get(i).getEndDateTime().isBefore(st)
                            && listSortedBooking.get(i + 1).getStartDateTime().isAfter(et)) {
                        System.out.println("Ok1");
                        res = true;
                        break;
                    }
                }
            }
        }
        System.out.println(res);
        // Assertions.assertEquals(true, res);
    }
}
