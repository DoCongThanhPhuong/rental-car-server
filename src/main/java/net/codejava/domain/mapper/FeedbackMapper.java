package net.codejava.domain.mapper;

import net.codejava.domain.dto.feedback.AddFeedbackRequestDTO;
import net.codejava.domain.dto.feedback.FeedbackResponseDTO;
import net.codejava.domain.entity.Feedback;

public interface FeedbackMapper {
    FeedbackResponseDTO toFeedbackResponseDto(Feedback entity);

    Feedback addFeedbackRequestToEntity(AddFeedbackRequestDTO requestDTO);
}
