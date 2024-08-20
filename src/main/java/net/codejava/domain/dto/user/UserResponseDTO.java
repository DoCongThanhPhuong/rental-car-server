package net.codejava.domain.dto.user;

import lombok.Builder;
import net.codejava.domain.enums.UserType;

@Builder
public record UserResponseDTO(
        Integer userId,
        String username,
        String email,
        String phoneNumber,
        Double wallet,
        String avatar,
        UserType userType) {}
