package net.codejava.domain.dto.user;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

public record UpdUserRequestDTO(
        String username,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = "yyyy/MM/dd") Date birthDay,
        String nationalId,
        String phoneNumber,
        String address,
        String drivingLicense) {}
