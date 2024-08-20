package net.codejava.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;
import net.codejava.domain.dto.test.BodyTestRequestDTO;
import net.codejava.service.CloudinaryService;
import net.codejava.utility.MailSenderUtil;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final MailSenderUtil mailSenderUtil;
    private final CloudinaryService cloudinaryService;

    @PostMapping(Endpoint.V1.Test.TEST01)
    public ResponseEntity<String> test1(@RequestBody BodyTestRequestDTO requestDTO) throws IOException {
        cloudinaryService.uploadFileBase64(requestDTO.base64String(), "RentalCar/Default");
        return ResponseEntity.status(HttpStatus.OK).body("Upload success");
    }

    @GetMapping(Endpoint.V1.Test.TEST02)
    public ResponseEntity<String> test2() {
        mailSenderUtil.sendBasicMail("nguyenthiminh19111964@gmail.com", "Test", "Test01");
        return ResponseEntity.status(HttpStatus.OK).body("Send mail successful");
    }
}
