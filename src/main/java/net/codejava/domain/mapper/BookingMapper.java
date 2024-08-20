package net.codejava.domain.mapper;

import net.codejava.domain.dto.booking.*;
import net.codejava.domain.entity.Booking;

public interface BookingMapper {
    BookingResponseDTO toBookingResponseDto(Booking entity);

    BookingDetailResponseDTO toBookingDetailResponseDto(Booking entity);

    BookingResponseForOwnerDTO toBookingResponseForOwnerDto(Booking entity);

    Booking addBookingRequestToBookingEntity(AddBookingRequestDTO requestDTO);

    Booking updBookingRequestToBookingEntity(Booking oldBooking, UpdBookingRequestDTO requestDTO);
}
