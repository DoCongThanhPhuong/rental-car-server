package net.codejava.service;

import java.io.IOException;
import java.util.Map;

import net.codejava.domain.dto.auth.LoginResponseDTO;
import net.codejava.domain.dto.user.AddUserRequestDTO;
import net.codejava.domain.dto.user.UpdUserRequestDTO;
import net.codejava.domain.dto.user.UserDetailResponseDTO;
import net.codejava.responses.Response;

public interface UserService {
    Response<UserDetailResponseDTO> getDetailUser(Integer id);

    Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) throws IOException;

    Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO);

    Response<Map<String, String>> getMyWallet(Integer userId);
}
