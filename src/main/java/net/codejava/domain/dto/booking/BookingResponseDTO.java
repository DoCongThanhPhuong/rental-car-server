package net.codejava.domain.dto.booking;

import java.util.List;

import lombok.Builder;
import net.codejava.domain.enums.BookingStatus;

@Builder
public record BookingResponseDTO(
        Integer bookingId,
        Integer carid,
        String carName,
        Long numberOfHour,
        Double basePrice,
        Double total,
        Double deposit,
        BookingStatus bookingStatus,
        String startDateTime,
        String endDateTime,
        List<String> images) {}
