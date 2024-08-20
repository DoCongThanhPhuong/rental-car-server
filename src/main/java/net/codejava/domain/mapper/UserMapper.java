package net.codejava.domain.mapper;

import net.codejava.domain.dto.auth.RegisterUserResponseDTO;
import net.codejava.domain.dto.user.AddUserRequestDTO;
import net.codejava.domain.dto.user.UpdUserRequestDTO;
import net.codejava.domain.dto.user.UserDetailResponseDTO;
import net.codejava.domain.dto.user.UserResponseDTO;
import net.codejava.domain.entity.User;

public interface UserMapper {
    RegisterUserResponseDTO toRegisterUserResponseDto(User entity);

    UserDetailResponseDTO toUserDetailResponseDTO(User entity);

    UserResponseDTO toUserResponseDTO(User entity);

    User addUserRequestDTOtoUserEntity(AddUserRequestDTO requestDTO);

    User updUserToUserEntity(User oldUser, UpdUserRequestDTO requestDTO);
}
