package net.codejava.service;

import jakarta.mail.MessagingException;

import net.codejava.domain.dto.auth.ChangePasswordRequestDTO;
import net.codejava.domain.dto.auth.LoginRequestDTO;
import net.codejava.domain.dto.auth.LoginResponseDTO;
import net.codejava.responses.Response;

public interface AuthenticationService {
    Response<LoginResponseDTO> login(LoginRequestDTO requestDTO);

    Response<String> changePassword(Integer userId, ChangePasswordRequestDTO requestDTO);

    Response<String> forgetPassword(String email) throws MessagingException;
}
