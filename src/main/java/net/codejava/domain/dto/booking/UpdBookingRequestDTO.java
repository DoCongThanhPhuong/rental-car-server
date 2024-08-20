package net.codejava.domain.dto.booking;

import net.codejava.domain.entity.UserInfor;

public record UpdBookingRequestDTO(UserInfor renterInfor, UserInfor driverInfor) {}
