package net.codejava.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.DefaultAvatar;
import net.codejava.domain.dto.auth.LoginResponseDTO;
import net.codejava.domain.dto.user.AddUserRequestDTO;
import net.codejava.domain.dto.user.UpdUserRequestDTO;
import net.codejava.domain.dto.user.UserDetailResponseDTO;
import net.codejava.domain.entity.Image;
import net.codejava.domain.entity.User;
import net.codejava.domain.mapper.UserMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.ImageRepository;
import net.codejava.repository.UserRepository;
import net.codejava.responses.Response;
import net.codejava.service.CloudinaryService;
import net.codejava.service.UserService;
import net.codejava.utility.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final ImageRepository imageRepo;
    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final CloudinaryService cloudinaryService;

    @Value("${cloudinary.folder.avatar}")
    private String folderAvatar;

    @Override
    public Response<UserDetailResponseDTO> getDetailUser(Integer id) {
        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        return Response.successfulResponse(
                "Get detail user successful", userMapper.toUserDetailResponseDTO(findUser.get()));
    }

    @Override
    public Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) throws IOException {
        Optional<User> findUser = userRepo.findByEmail(requestDTO.email());
        if (!findUser.isEmpty()) throw new AppException("Email already existed. Please try another email.");

        User newUser = userMapper.addUserRequestDTOtoUserEntity(requestDTO);
        // Set password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(requestDTO.password()));
        // Set Image
        Image avatarUpload;
        // if (requestDTO.avatar() == null || requestDTO.avatar().isEmpty()) {
        // Image defaultImage = imageRepo.findById(1).get();
        avatarUpload = Image.builder()
                .name(DefaultAvatar.avatarName)
                .imageUrl(DefaultAvatar.avatarUrl)
                .imagePublicId(DefaultAvatar.publicIdAvatar)
                .whenCreated(new Date())
                .build();
        //        } else {
        //            Map uploadImage = cloudinaryService.uploadFile(requestDTO.avatar(), folderAvatar);
        //            avatarUpload = Image.builder()
        //                    .name((String) uploadImage.get("original_filename"))
        //                    .imageUrl((String) uploadImage.get("url"))
        //                    .imagePublicId((String) uploadImage.get("public_id"))
        //                    .whenCreated(new Date())
        //                    .build();
        //        }
        newUser.setAvatar(avatarUpload);

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse(
                    "Register successful",
                    LoginResponseDTO.builder()
                            .authenticated(true)
                            .token(jwtTokenUtil.generateToken(saveUser))
                            .infor(userMapper.toUserDetailResponseDTO(saveUser))
                            .build());
        } catch (Exception e) {
            throw new AppException("Register fail");
        }
    }

    @Override
    public Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO) {
        Optional<User> oldUser = userRepo.findById(id);
        if (oldUser.isEmpty()) throw new AppException("This user is not existed");

        User newUser = userMapper.updUserToUserEntity(oldUser.get(), requestDTO);
        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse("Update User Successfull", userMapper.toUserDetailResponseDTO(saveUser));
        } catch (Exception e) {
            throw new AppException("Update User fail");
        }
    }

    @Override
    public Response<Map<String, String>> getMyWallet(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        Map<String, String> res =
                Map.ofEntries(Map.entry("wallet", findUser.get().getWallet().toString()));
        return Response.successfulResponse("Get your wallet successful", res);
    }
}
