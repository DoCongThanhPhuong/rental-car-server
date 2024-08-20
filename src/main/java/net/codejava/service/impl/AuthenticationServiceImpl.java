package net.codejava.service.impl;

import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.MailTemplate;
import net.codejava.domain.dto.auth.ChangePasswordRequestDTO;
import net.codejava.domain.dto.auth.LoginRequestDTO;
import net.codejava.domain.dto.auth.LoginResponseDTO;
import net.codejava.domain.entity.User;
import net.codejava.domain.mapper.UserMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.UserRepository;
import net.codejava.responses.Response;
import net.codejava.service.AuthenticationService;
import net.codejava.utility.JwtTokenUtil;
import net.codejava.utility.MailSenderUtil;
import net.codejava.utility.RandomPasswordUtil;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final MailSenderUtil mailSenderUtil;

    @Override
    public Response<LoginResponseDTO> login(LoginRequestDTO requestDTO) {
        Optional<User> userResult = userRepo.findByEmail(requestDTO.email());

        if (userResult.isEmpty())
            throw new AppException("Either email address or password is incorrect. Please try again");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Boolean authenticated =
                passwordEncoder.matches(requestDTO.password(), userResult.get().getPassword());
        if (authenticated == false)
            throw new AppException("Either email address or password is incorrect. Please try again");
        String token = jwtTokenUtil.generateToken(userResult.get());

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .authenticated(true)
                .token(token)
                .infor(userMapper.toUserDetailResponseDTO(userResult.get()))
                .build();
        return Response.successfulResponse("Login successful", responseDTO);
    }

    @Override
    public Response<String> changePassword(Integer userId, ChangePasswordRequestDTO requestDTO) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not exsited");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Boolean authenticated =
                passwordEncoder.matches(requestDTO.oldPassword(), findUser.get().getPassword());
        if (authenticated == false) throw new AppException("Current password is incorrect");

        findUser.get().setPassword(passwordEncoder.encode(requestDTO.newPassword()));
        try {
            User saveUser = userRepo.save(findUser.get());
            return Response.successfulResponse("Change password successful");
        } catch (Exception e) {
            throw new AppException("Change password fail");
        }
    }

    @Override
    @Transactional
    public Response<String> forgetPassword(String email) throws MessagingException {
        Optional<User> findUser = userRepo.findByEmail(email);
        if (findUser.isEmpty()) throw new AppException("This email is not existed");
        User user = findUser.get();
        // Reset Password
        String randomPassword_plain = RandomPasswordUtil.generateCommonLangPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String randomPassword_cipher = passwordEncoder.encode(randomPassword_plain);
        user.setPassword(randomPassword_cipher);
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new AppException("Reset password fail");
        }
        // Send Mail
        String toMail = user.getEmail();
        String subject = MailTemplate.CHANGE_PASSWORD.CHANGE_PASSWORD_SUBJECT;
        String template = MailTemplate.CHANGE_PASSWORD.CHANGE_PASSWORD_TEMPLATE;
        Map<String, Object> variable = Map.of("userName", user.getUsername(), "newPassword", randomPassword_plain);
        mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);
        return Response.successfulResponse("Reset password successful. New password sent via email");
    }
}
