package net.codejava.domain.entity;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.codejava.domain.enums.UserInforType;

@Entity
@Table(name = "user_infors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfor {
    @Id
    @Column(name = "user_infor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationalId;
    private String drivingLicense;

    @Enumerated(EnumType.STRING)
    private UserInforType userInforType;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date birthDay;

    @JsonIgnore
    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public UserInfor(UserInfor userInfor) {
        this.username = userInfor.username;
        this.email = userInfor.email;
        this.phoneNumber = userInfor.phoneNumber;
        this.address = userInfor.address;
        this.nationalId = userInfor.nationalId;
        this.drivingLicense = userInfor.drivingLicense;
        this.userInforType = userInfor.userInforType;
        this.birthDay = userInfor.birthDay;
        this.booking = userInfor.booking;
    }
}
