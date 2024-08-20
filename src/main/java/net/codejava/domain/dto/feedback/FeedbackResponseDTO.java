package net.codejava.domain.dto.feedback;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;

@Builder
public record FeedbackResponseDTO(
        String avatar,
        String username,
        String content,
        Integer rating,
        @Temporal(TemporalType.TIMESTAMP) @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") Date createdAt,
        List<String> carImage,
        String carName,
        String startDateTime,
        String endDateTime) {}
