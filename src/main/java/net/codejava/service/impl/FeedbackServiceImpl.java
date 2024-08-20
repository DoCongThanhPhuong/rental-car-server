package net.codejava.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.MetaConstant;
import net.codejava.domain.dto.feedback.AddFeedbackRequestDTO;
import net.codejava.domain.dto.feedback.FeedbackResponseDTO;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.meta.SortingDTO;
import net.codejava.domain.entity.Booking;
import net.codejava.domain.entity.Car;
import net.codejava.domain.entity.Feedback;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.domain.mapper.FeedbackMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.CarRepository;
import net.codejava.repository.FeedbackRepository;
import net.codejava.repository.UserRepository;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;
import net.codejava.service.BookingService;
import net.codejava.service.FeedbackService;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepo;
    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final BookingService bookingService;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public Feedback addFeedback(Integer userId, Integer bookingId, AddFeedbackRequestDTO requestDTO) {
        Booking booking = bookingService.verifyBookingCustomer(userId, bookingId);
        // Check status of booking
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT && booking.getStatus() != BookingStatus.COMPLETED)
            throw new AppException("The status of this booking don't allow feedback");
        Feedback newFeedback = feedbackMapper.addFeedbackRequestToEntity(requestDTO);
        newFeedback.setBooking(booking);
        Feedback saveFeedback = feedbackRepo.save(newFeedback);
        // update rating for car
        Double ratingCar = feedbackRepo.getRatingByCarId(booking.getCar().getId());
        Car car = booking.getCar();
        car.setRating(ratingCar);
        carRepo.save(car);

        return saveFeedback;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByOwner(
            Integer ownerId, Integer rating, MetaRequestDTO metaRequestDTO) {
        Optional findOwner = userRepo.findById(ownerId);
        if (findOwner.isEmpty()) throw new AppException("This user is not existed");

        String sortField = metaRequestDTO.sortField();
        if (sortField.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) sortField = "feedback_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);

        Page<Feedback> page = feedbackRepo.getListByOwnerId(ownerId, rating, pageable);

        List<FeedbackResponseDTO> liRes = page.getContent().stream()
                .map(temp -> feedbackMapper.toFeedbackResponseDto(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Get list feedbacks success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                liRes);
    }

    @Override
    public Response<Map<String, String>> getRating(Integer userId) {
        Double rating = feedbackRepo.getRatingByOwner(userId);
        Map<String, String> res = Map.ofEntries(Map.entry("rating", rating.toString()));
        return Response.successfulResponse("Get rating for owner success", res);
    }
}
