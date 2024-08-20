package net.codejava.domain.mapper.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.codejava.domain.dto.auth.RegisterUserResponseDTO;
import net.codejava.domain.dto.user.AddUserRequestDTO;
import net.codejava.domain.dto.user.UpdUserRequestDTO;
import net.codejava.domain.dto.user.UserDetailResponseDTO;
import net.codejava.domain.dto.user.UserResponseDTO;
import net.codejava.domain.entity.User;
import net.codejava.domain.enums.UserType;
import net.codejava.domain.mapper.UserMapper;
import net.codejava.utility.JwtTokenUtil;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public RegisterUserResponseDTO toRegisterUserResponseDto(User entity) {
        return RegisterUserResponseDTO.builder()
                .accessToken(jwtTokenUtil.generateToken(entity))
                .userDetail(toUserDetailResponseDTO(entity))
                .build();
    }

    @Override
    public UserDetailResponseDTO toUserDetailResponseDTO(User entity) {
        return UserDetailResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .birthDay(entity.getBirthDay())
                .nationality(entity.getNationalId())
                .phoneNumber(entity.getPhoneNumber())
                .address(entity.getAddress())
                .drivingLicense(entity.getDrivingLicense())
                .wallet(entity.getWallet())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isActive(entity.isActive())
                .avatar(entity.getAvatar())
                .roles(entity.getRoles())
                .userType(entity.getUserType())
                .userTypeName(entity.getUserType().getTitle())
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .wallet(entity.getWallet())
                .avatar(entity.getAvatar().getImageUrl())
                .userType(entity.getUserType())
                .build();
    }

    @Override
    public User addUserRequestDTOtoUserEntity(AddUserRequestDTO requestDTO) {
        return User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .birthDay(null)
                .nationalId(null)
                .phoneNumber(requestDTO.phoneNumber())
                .address(null)
                .drivingLicense(null)
                .wallet(0.0)
                .createdAt(new Date())
                .updatedAt(null)
                .isActive(true)
                .userType(UserType.valueOf(requestDTO.userType()))
                .build();
    }

    @Override
    public User updUserToUserEntity(User oldUser, UpdUserRequestDTO requestDTO) {
        oldUser.setUsername(requestDTO.username());
        oldUser.setBirthDay(requestDTO.birthDay());
        oldUser.setNationalId(requestDTO.nationalId());
        oldUser.setPhoneNumber(requestDTO.phoneNumber());
        oldUser.setAddress(requestDTO.address());
        oldUser.setDrivingLicense(requestDTO.drivingLicense());
        oldUser.setUpdatedAt(new Date());
        return oldUser;
    }
}
