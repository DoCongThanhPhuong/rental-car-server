package net.codejava.domain.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.codejava.domain.dto.booking.*;
import net.codejava.domain.entity.Booking;
import net.codejava.domain.entity.UserInfor;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.domain.enums.UserInforType;
import net.codejava.domain.mapper.BookingMapper;
import net.codejava.domain.mapper.CarMapper;
import net.codejava.utility.TimeUtil;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {
    private final CarMapper carMapper;
    // private final RentalCalculateUtil rentalCalculateUtil;
    @Override
    public BookingResponseDTO toBookingResponseDto(Booking entity) {
        List<String> images = entity.getCar().getImages().stream()
                .map(temp -> temp.getImageUrl())
                .toList();
        return BookingResponseDTO.builder()
                .bookingId(entity.getId())
                .carid(entity.getCar().getId())
                .carName(entity.getCar().getName())
                .numberOfHour(entity.getNumberOfHour())
                .basePrice(entity.getCar().getBasePrice())
                .total(entity.getTotal())
                .deposit(entity.getCar().getDeposit())
                .bookingStatus(entity.getStatus())
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .images(images)
                .build();
    }

    @Override
    public BookingDetailResponseDTO toBookingDetailResponseDto(Booking entity) {
        UserInfor renter = new UserInfor();
        UserInfor driver = new UserInfor();
        for (UserInfor userInfor : entity.getUserInfors()) {
            if (userInfor.getUserInforType().equals(UserInforType.RENTER)) renter = userInfor;
            if (userInfor.getUserInforType().equals(UserInforType.DRIVER)) driver = userInfor;
        }
        return BookingDetailResponseDTO.builder()
                .id(entity.getId())
                .car(carMapper.toCarDetailResponseDTO(entity.getCar()))
                .renterInfor(renter)
                .driverInfor(driver)
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .total(entity.getTotal())
                .numberOfHour(entity.getNumberOfHour())
                .build();
    }

    @Override
    public BookingResponseForOwnerDTO toBookingResponseForOwnerDto(Booking entity) {
        UserInfor renter = new UserInfor();
        UserInfor driver = new UserInfor();
        for (UserInfor userInfor : entity.getUserInfors()) {
            if (userInfor.getUserInforType().equals(UserInforType.RENTER)) renter = userInfor;
            if (userInfor.getUserInforType().equals(UserInforType.DRIVER)) driver = userInfor;
        }
        return BookingResponseForOwnerDTO.builder()
                .id(entity.getId())
                .renterInfor(renter)
                .driverInfor(driver)
                .startDateTime(TimeUtil.formatToString(entity.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(entity.getEndDateTime()))
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Booking addBookingRequestToBookingEntity(AddBookingRequestDTO requestDTO) {
        return Booking.builder()
                .startDateTime(TimeUtil.convertToDateTime(requestDTO.startDateTime()))
                .endDateTime(TimeUtil.convertToDateTime(requestDTO.endDateTime()))
                .paymentMethod(requestDTO.paymentMethod())
                .status(BookingStatus.PENDING_DEPOSIT)
                .userInfors(new ArrayList<>())
                .feedback(null)
                .build();
    }

    @Override
    public Booking updBookingRequestToBookingEntity(Booking oldBooking, UpdBookingRequestDTO requestDTO) {
        UserInfor renter = requestDTO.renterInfor();
        renter.setId(oldBooking.getUserInfors().getFirst().getId());
        UserInfor driver = requestDTO.driverInfor();
        if (driver == null) {
            driver = renter;
            driver.setUserInforType(UserInforType.DRIVER);
        }
        driver.setId(oldBooking.getUserInfors().getLast().getId());
        oldBooking.getUserInfors().clear();
        oldBooking.addUserInfor(renter);
        oldBooking.addUserInfor(driver);
        return oldBooking;
    }
}
