package net.codejava.domain.mapper.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import net.codejava.domain.dto.feedback.AddFeedbackRequestDTO;
import net.codejava.domain.dto.feedback.FeedbackResponseDTO;
import net.codejava.domain.entity.Booking;
import net.codejava.domain.entity.Car;
import net.codejava.domain.entity.Feedback;
import net.codejava.domain.entity.User;
import net.codejava.domain.mapper.FeedbackMapper;
import net.codejava.utility.TimeUtil;

@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Override
    public FeedbackResponseDTO toFeedbackResponseDto(Feedback entity) {
        // Booking
        Booking booking = entity.getBooking();
        // Customer
        User customer = entity.getBooking().getUser();
        // Car
        Car car = entity.getBooking().getCar();
        List<String> carImage =
                car.getImages().stream().map(temp -> temp.getImageUrl()).toList();
        return FeedbackResponseDTO.builder()
                .avatar(customer.getAvatar().getImageUrl())
                .username(customer.getUsername())
                .content(entity.getContent())
                .rating(entity.getRating())
                .createdAt(entity.getCreatedAt())
                .carImage(carImage)
                .carName(car.getName())
                .startDateTime(TimeUtil.formatToString(booking.getStartDateTime()))
                .endDateTime(TimeUtil.formatToString(booking.getEndDateTime()))
                .build();
    }

    @Override
    public Feedback addFeedbackRequestToEntity(AddFeedbackRequestDTO requestDTO) {
        return Feedback.builder()
                .rating(requestDTO.rating())
                .createdAt(new Date())
                .content(requestDTO.content())
                .build();
    }
}
