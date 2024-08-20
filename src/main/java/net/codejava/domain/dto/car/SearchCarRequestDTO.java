package net.codejava.domain.dto.car;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.codejava.annotation.AfterNowTime;
import net.codejava.annotation.RentalTimeMatching;
import net.codejava.constant.TimeFormatConstant;

@RentalTimeMatching(startTime = "startTime", endTime = "endTime")
public record SearchCarRequestDTO(
        @RequestParam(name = "address") String address,
        @RequestParam(name = "startTime")
                @NotBlank(message = "The start rental time is not blank")
                @AfterNowTime(message = "The start rental time is after now")
                @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
                String startTime,
        @RequestParam(name = "endTime")
                @NotBlank(message = "The end rental time is not blank")
                @JsonFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
                String endTime) {}
