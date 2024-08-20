package net.codejava.domain.dto.user;

import net.codejava.annotation.EnumValid;
import net.codejava.annotation.PasswordMatching;
import net.codejava.annotation.StrongPassword;
import net.codejava.domain.enums.UserType;

@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword",
        message = "Password and Confirm password " + "don’t match. Please try again.")
public record AddUserRequestDTO(
        @StrongPassword String password,
        String confirmPassword,
        String username,
        String email,
        String phoneNumber,
        @EnumValid(name = "userType", enumClass = UserType.class) String userType,
        // MultipartFile avatar,
        Boolean acceptTermAndCondition) {}
