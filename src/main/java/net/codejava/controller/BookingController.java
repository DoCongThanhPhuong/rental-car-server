package net.codejava.controller;

import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.codejava.constant.Endpoint;
import net.codejava.domain.dto.booking.*;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;
import net.codejava.service.BookingService;
import net.codejava.utility.AuthUtil;
import net.codejava.utility.JwtTokenUtil;

@Tag(name = "Booking Controller", description = "APIs related to Booking operations")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping(Endpoint.V1.Booking.GET_LIST_FOR_USER)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getListBookingForUser(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO metaRequestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getListBookingForUser(metaRequestDTO, userId));
    }

    @GetMapping(Endpoint.V1.Booking.GET_LIST_BY_CAR)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>>> getListBookingByCarId(
            @ParameterObject MetaRequestDTO metaRequestDTO, @PathVariable(name = "carId") Integer carId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getListBookingByCarId(
                        metaRequestDTO, carId, AuthUtil.getRequestedUser().getId()));
    }

    @GetMapping(Endpoint.V1.Booking.GET_DETAIL)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getDetailBooking(
            @PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getDetailBooking(bookingId));
    }

    @PostMapping(Endpoint.V1.Booking.ADD_BOOKING)
    public ResponseEntity<Response<BookingDetailResponseDTO>> addBooking(
            HttpServletRequest servletRequest, @RequestBody @Valid AddBookingRequestDTO requestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.addBooking(AuthUtil.getRequestedUser().getId(), requestDTO));
    }

    @PutMapping(Endpoint.V1.Booking.UPDATE)
    public ResponseEntity<Response<BookingDetailResponseDTO>> updateBooking(
            @PathVariable("id") Integer bookingId, @RequestBody @Valid UpdBookingRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingId, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_DEPOSIT)
    public ResponseEntity<Response<String>> confirmDeposit(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmDeposit(
                        bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PICK_UP)
    public ResponseEntity<Response<String>> confirmPickUp(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.confirmPickUp(bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PAYMENT)
    public ResponseEntity<Response<String>> confirmPayment(@PathVariable(name = "id") Integer bookingId
            // ,
            // @RequestParam(name = "payment-method") @Valid @NotBlank(message = "Payment Method is not empty") String
            // paymentMethod
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmPayment(AuthUtil.getRequestedUser().getId(), bookingId));
        //        try {
        //            PaymentMethod method = PaymentMethod.valueOf(paymentMethod);
        //            return ResponseEntity.status(HttpStatus.OK).body(bookingService.confirmPayment(bookingId));
        //        } catch (IllegalArgumentException e) {
        //            throw new AppException("Invalid Payment method");
        //        }
    }

    @PatchMapping(Endpoint.V1.Booking.CANCELLED_BOOKING)
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.cancelBooking(AuthUtil.getRequestedUser().getId(), bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.RETURN_CAR)
    public ResponseEntity<Response<String>> returnCar(@PathVariable(name = "id") Integer bookingId)
            throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.returnCar(AuthUtil.getRequestedUser().getId(), bookingId));
    }
}
