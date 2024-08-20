package net.codejava.domain.dto.auth;

import net.codejava.annotation.PasswordMatching;
import net.codejava.annotation.StrongPassword;

@PasswordMatching(
        password = "newPassword",
        confirmPassword = "confirmNewPassword",
        message = "Password and Confirm password " + "don’t match. Please try again.")
public record ChangePasswordRequestDTO(
        @StrongPassword String newPassword, String confirmNewPassword, String oldPassword) {}
