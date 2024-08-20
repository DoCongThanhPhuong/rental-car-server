package net.codejava.domain.dto.user;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import net.codejava.domain.entity.Image;
import net.codejava.domain.entity.Role;
import net.codejava.domain.enums.UserType;

@Builder
public record UserDetailResponseDTO(
        Integer id,
        String username,
        String email,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = "yyyy/MM/dd") Date birthDay,
        String nationality,
        String phoneNumber,
        String address,
        String drivingLicense,
        double wallet,
        Date createdAt,
        Date updatedAt,
        boolean isActive,
        Image avatar,
        Set<Role> roles,
        UserType userType,
        String userTypeName) {}
