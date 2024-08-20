package net.codejava.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;
import net.codejava.domain.dto.user.UpdUserRequestDTO;
import net.codejava.domain.dto.user.UserDetailResponseDTO;
import net.codejava.domain.entity.User;
import net.codejava.responses.Response;
import net.codejava.service.UserService;
import net.codejava.utility.AuthUtil;

@Tag(name = "User Controller", description = "APIs related to User operations")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get Detail User For User", description = "This API allows users to get detail user.")
    @GetMapping(Endpoint.V1.User.GET_DETAIL)
    public ResponseEntity<Response<UserDetailResponseDTO>> getDetailUser() {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetailUser(user.getId()));
    }

    @Operation(summary = "Update User For User", description = "This API allows users to update user.")
    @PutMapping(Endpoint.V1.User.UPDATE)
    public ResponseEntity<Response<UserDetailResponseDTO>> updateUser(
            @RequestBody @Valid UpdUserRequestDTO requestDTO) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user.getId(), requestDTO));
    }

    @Operation(summary = "Get wallet User For User", description = "This API allows users to get my wallet.")
    @GetMapping(Endpoint.V1.User.GET_MONEY_IN_WALLET)
    public ResponseEntity<Response<Map<String, String>>> getMyWallet() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getMyWallet(AuthUtil.getRequestedUser().getId()));
    }
}
