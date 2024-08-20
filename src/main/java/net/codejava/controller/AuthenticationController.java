package net.codejava.controller;

import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;
import net.codejava.domain.dto.auth.*;
import net.codejava.domain.dto.user.AddUserRequestDTO;
import net.codejava.domain.entity.User;
import net.codejava.exceptions.AppException;
import net.codejava.responses.Response;
import net.codejava.service.AuthenticationService;
import net.codejava.service.UserService;

@Tag(name = "Authentication Controller", description = "APIs related to Authentication operations")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "UC01: Log in", description = "This API allows users to log in.")
    @PostMapping(Endpoint.V1.Auth.LOGIN)
    public ResponseEntity<Response<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(requestDTO));
    }

    @Operation(summary = "UC04: Register an account", description = "This API allows users to register an account")
    @PostMapping(Endpoint.V1.Auth.REGISTER)
    public ResponseEntity<Response<LoginResponseDTO>> register(
            // @ModelAttribute
            @RequestBody @Valid AddUserRequestDTO requestDTO) throws IOException {
        if (requestDTO.acceptTermAndCondition() != true) throw new AppException("Let verify term and condition");
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(requestDTO));
    }

    @Operation(
            summary = "UC05: Edit Profile => Change Password",
            description = "This API allows users to change password")
    @PatchMapping(Endpoint.V1.Auth.CHANGE_PASSWORD)
    public ResponseEntity<Response<String>> changePassword(@RequestBody @Valid ChangePasswordRequestDTO requestDTO) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.changePassword(user.getId(), requestDTO));
    }

    @PostMapping(Endpoint.V1.Auth.FORGET_PASSWORD)
    public ResponseEntity<Response<String>> forgetPassword(@RequestBody ForgetPasswordRequestDTO requestDTO)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.forgetPassword(requestDTO.email()));
    }
}
