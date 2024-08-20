package net.codejava.domain.dto.transaction;

import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.codejava.annotation.RentalTimeMatching;
import net.codejava.constant.TimeFormatConstant;

@RentalTimeMatching(startTime = "startTime", endTime = "endTime")
public record FilterTransactionByTimeRequestDTO(
        @RequestParam(name = "startTime", required = false) @JsonFormat(pattern = TimeFormatConstant.DATE_FORMAT)
                String startTime,
        @RequestParam(name = "endTime", required = false) @JsonFormat(pattern = TimeFormatConstant.DATE_FORMAT)
                String endTime) {}
