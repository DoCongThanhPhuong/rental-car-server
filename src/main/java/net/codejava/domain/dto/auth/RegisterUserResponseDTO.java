package net.codejava.domain.dto.auth;

import lombok.Builder;
import net.codejava.domain.dto.user.UserDetailResponseDTO;

@Builder
public record RegisterUserResponseDTO(String accessToken, UserDetailResponseDTO userDetail) {}
