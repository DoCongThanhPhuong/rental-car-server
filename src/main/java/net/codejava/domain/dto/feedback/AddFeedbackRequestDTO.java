package net.codejava.domain.dto.feedback;

import lombok.Builder;

@Builder
public record AddFeedbackRequestDTO(Integer rating, String content) {}
