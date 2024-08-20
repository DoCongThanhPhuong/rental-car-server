package net.codejava.domain.dto.booking;

import lombok.Builder;
import net.codejava.domain.entity.UserInfor;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.domain.enums.PaymentMethod;

@Builder
public record BookingResponseForOwnerDTO(
        Integer id,
        UserInfor renterInfor,
        UserInfor driverInfor,
        String startDateTime,
        String endDateTime,
        PaymentMethod paymentMethod,
        BookingStatus status) {}
