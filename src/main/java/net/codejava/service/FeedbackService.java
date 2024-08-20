package net.codejava.service;

import java.util.List;
import java.util.Map;

import net.codejava.domain.dto.feedback.AddFeedbackRequestDTO;
import net.codejava.domain.dto.feedback.FeedbackResponseDTO;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.entity.Feedback;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;

public interface FeedbackService {
    Feedback addFeedback(Integer userId, Integer bookingId, AddFeedbackRequestDTO requestDTO);

    MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByOwner(
            Integer ownerId, Integer rating, MetaRequestDTO metaRequestDTO);

    Response<Map<String, String>> getRating(Integer userId);
}
