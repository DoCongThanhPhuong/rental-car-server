package net.codejava.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
import net.codejava.constant.TimeFormatConstant;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.domain.enums.PaymentMethod;
import net.codejava.utility.TimeUtil;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int id;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime endDateTime;

    @Transient
    @Setter(AccessLevel.NONE)
    private Double total;

    public Double getTotal() {
        Integer hours = TimeUtil.getHoursDifference(startDateTime, endDateTime);
        return hours * (car.getBasePrice() / 24);
    }

    @Transient
    @Setter(AccessLevel.NONE)
    private Long numberOfHour;

    public Long getNumberOfHour() {
        return (long) TimeUtil.getHoursDifference(startDateTime, endDateTime);
    }

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private Car car;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "booking",
            targetEntity = UserInfor.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<UserInfor> userInfors = new ArrayList<>();

    public void addUserInfor(UserInfor userInfor) {
        userInfor.setBooking(this);
        this.userInfors.add(userInfor);
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "booking", orphanRemoval = true)
    private Feedback feedback;
}
