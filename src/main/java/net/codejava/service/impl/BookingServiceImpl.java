package net.codejava.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.MailTemplate;
import net.codejava.constant.MetaConstant;
import net.codejava.domain.dto.booking.*;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.meta.SortingDTO;
import net.codejava.domain.dto.transaction.AddSystemTransactionRequestDTO;
import net.codejava.domain.entity.Booking;
import net.codejava.domain.entity.Car;
import net.codejava.domain.entity.User;
import net.codejava.domain.entity.UserInfor;
import net.codejava.domain.enums.BookingStatus;
import net.codejava.domain.enums.PaymentMethod;
import net.codejava.domain.enums.TransactionType;
import net.codejava.domain.enums.UserInforType;
import net.codejava.domain.mapper.BookingMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.BookingRepository;
import net.codejava.repository.CarRepository;
import net.codejava.repository.UserRepository;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;
import net.codejava.service.BookingService;
import net.codejava.service.CarService;
import net.codejava.service.TransactionService;
import net.codejava.utility.MailSenderUtil;
import net.codejava.utility.TimeUtil;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class BookingServiceImpl implements BookingService {
    private final CarRepository carRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final TransactionService transactionService;
    private final CarService carService;
    private final BookingMapper bookingMapper;
    private final MailSenderUtil mailSenderUtil;

    @Override
    public Booking verifyBookingOwner(Integer ownerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getCar().getCarOwner().getId() != ownerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public Booking verifyBookingCustomer(Integer customerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getUser().getId() != customerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getListBookingForUser(
            MetaRequestDTO metaRequestDTO, Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = metaRequestDTO.keyword() == null
                ? bookingRepo.getListBookingByUserId(userId, pageable)
                : bookingRepo.getListBookingByUserIdWithKeyword(userId, metaRequestDTO.keyword(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseDTO> li = page.getContent().stream()
                .map(temp -> bookingMapper.toBookingResponseDto(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Get list booking success",
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
                li);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getListBookingByCarId(
            MetaRequestDTO metaRequestDTO, Integer carId, Integer userId) {
        // Car
        Car car = carService.verifyCarOwner(userId, carId);
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = metaRequestDTO.keyword() == null
                ? bookingRepo.getListBookingByCarId(carId, pageable)
                : bookingRepo.getListBookingByUserId(carId, pageable);
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseForOwnerDTO> li = page.getContent().stream()
                .map(temp -> bookingMapper.toBookingResponseForOwnerDto(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Get list booking success",
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
                li);
    }

    @Override
    public Response<BookingDetailResponseDTO> getDetailBooking(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");

        return Response.successfulResponse(
                "Get detail booking successful", bookingMapper.toBookingDetailResponseDto(findBooking.get()));
    }

    @Override
    @Transactional
    public Response<BookingDetailResponseDTO> addBooking(Integer customerId, AddBookingRequestDTO requestDTO) {
        // Car
        Optional<Car> findCar = carRepo.findById(requestDTO.carId());
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();
        // Check the condition of car
        if (!car.getIsActive()) throw new AppException("This car is not active");
        if (car.getIsStopped()) throw new AppException("This car is stopped. Contact with the owner of this car");
        // Check schedule to rent Car
        Optional<Car> checkScheduleCar =
                carRepo.checkScheduleCar(requestDTO.carId(), requestDTO.startDateTime(), requestDTO.endDateTime());
        if (checkScheduleCar.isEmpty()) throw new AppException("Invalid booking schedule. Please try again.");
        // Customer
        Optional<User> findCustomer = userRepo.findById(customerId);
        if (findCustomer.isEmpty()) throw new AppException("This user is not existed");
        User customer = findCustomer.get();
        // Case: Payment Method is My Wallet
        if (requestDTO.paymentMethod() == PaymentMethod.MY_WALLET) {
            if (customer.getWallet() < car.getDeposit()) throw new AppException("Let check your wallet");
            customer.setWallet(customer.getWallet() - car.getDeposit());
            userRepo.save(customer);
        }
        // Owner
        User owner = car.getCarOwner();
        owner.setWallet(owner.getWallet() + car.getDeposit());
        userRepo.save(owner);
        // Booking
        Booking newBooking = bookingMapper.addBookingRequestToBookingEntity(requestDTO);
        newBooking.setCar(car);
        newBooking.setUser(customer);
        // Add Renter and Driver
        UserInfor renter = requestDTO.userInfors()[0];
        newBooking.addUserInfor(renter);
        UserInfor driver;
        if (requestDTO.userInfors().length == 1) {
            driver = UserInfor.builder()
                    .username(renter.getUsername())
                    .email(renter.getEmail())
                    .phoneNumber(renter.getPhoneNumber())
                    .address(renter.getAddress())
                    .nationalId(renter.getNationalId())
                    .drivingLicense(renter.getDrivingLicense())
                    .userInforType(UserInforType.DRIVER)
                    .birthDay(renter.getBirthDay())
                    .booking(renter.getBooking())
                    .build();
        } else driver = requestDTO.userInfors()[1];
        newBooking.addUserInfor(driver);
        try {
            Booking saveBooking = bookingRepo.save(newBooking);
            // Transaction
            // Customer
            AddSystemTransactionRequestDTO customerTran = AddSystemTransactionRequestDTO.builder()
                    .amount(-car.getDeposit())
                    .transactionType(TransactionType.PAY_DEPOSIT)
                    .bookingId(saveBooking.getId())
                    .carName(car.getName())
                    .user(customer)
                    .build();
            transactionService.addTransaction(customerTran);
            // Owner
            AddSystemTransactionRequestDTO ownerTran = AddSystemTransactionRequestDTO.builder()
                    .amount(car.getDeposit())
                    .transactionType(TransactionType.RECEIVE_DEPOSIT)
                    .bookingId(saveBooking.getId())
                    .carName(car.getName())
                    .user(owner)
                    .build();
            transactionService.addTransaction(ownerTran);
            // Send Mail To Owner
            String toMail = owner.getEmail();
            String subject = MailTemplate.RENT_A_CAR.RENT_A_CAR_SUBJECT;
            String template = MailTemplate.RENT_A_CAR.RENT_A_CAR_TEMPLATE;
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String bookingTime = now.format(formatter).toString();
            Map<String, Object> variable = Map.of(
                    "carName", car.getName(),
                    "bookingTime", bookingTime,
                    "bookingId", saveBooking.getId());
            mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);
            return Response.successfulResponse(
                    "Add new booking successful", bookingMapper.toBookingDetailResponseDto(saveBooking));
        } catch (Exception e) {
            throw new AppException("Add a new booking fail");
        }
    }

    @Override
    public Response<BookingDetailResponseDTO> updateBooking(Integer bookingId, UpdBookingRequestDTO requestDTO) {
        Optional<Booking> oldBooking = bookingRepo.findById(bookingId);
        if (oldBooking.isEmpty()) throw new AppException("This booking is not existed");

        // Check status allow to edit
        if (oldBooking.get().getStatus() == BookingStatus.IN_PROGRESS
                || oldBooking.get().getStatus() == BookingStatus.CANCELLED
                || oldBooking.get().getStatus() == BookingStatus.PENDING_PAYMENT
                || oldBooking.get().getStatus() == BookingStatus.COMPLETED)
            throw new AppException("Don't allow to edit this booking");

        Booking newBooking = bookingMapper.updBookingRequestToBookingEntity(oldBooking.get(), requestDTO);

        try {
            Booking saveBooking = bookingRepo.save(newBooking);
            return Response.successfulResponse(
                    "Update a booking successful", bookingMapper.toBookingDetailResponseDto(saveBooking));
        } catch (Exception e) {
            throw new AppException("Update a booking fail");
        }
    }

    @Override
    @Transactional
    public Response<String> confirmDeposit(Integer bookingId, Integer userId) throws MessagingException {
        Booking booking = this.verifyBookingOwner(userId, bookingId);
        if (booking.getStatus() == BookingStatus.PENDING_DEPOSIT) {
            // Update Booking
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            // Customer
            User customer = booking.getUser();
            // Car
            Car car = booking.getCar();
            // Send Mail To Customer
            String toMail = customer.getEmail();
            String subject = MailTemplate.CONFIRM_DEPOSIT.CONFIRM_DEPOSIT_SUBJECT;
            String template = MailTemplate.CONFIRM_DEPOSIT.CONFIRM_DEPOSIT_TEMPLATE;
            Map<String, Object> variable = Map.of(
                    "carName", car.getName(),
                    "startTime", TimeUtil.formatToString(booking.getStartDateTime()),
                    "endTime", TimeUtil.formatToString(booking.getEndDateTime()));
            mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

            return Response.successfulResponse("Confirm deposit successfully");
        } else throw new AppException("The status of this booking does not allow to confirm deposit");
    }

    @Override
    public Response<String> confirmPickUp(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.IN_PROGRESS);
            bookingRepo.save(booking);
            return Response.successfulResponse("Confirm pick up successfully");
        } else throw new AppException("The status of this booking does not allow to confirm pick up");
    }

    @Override
    @Transactional
    public Response<String> confirmPayment(Integer userId, Integer bookingId) {
        // Booking
        Booking booking = this.verifyBookingOwner(userId, bookingId);

        // Check Car
        //        Optional<Car> findCar = carRepo.findById(booking.getCar().getId());
        //        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        //        if (findCar.get().getIsAvailable() == false) throw new AppException("This car is unavailable");
        //        Car car = findCar.get();

        if (booking.getStatus() == BookingStatus.CONFIRMED) throw new AppException("This booking is completed");
        if (booking.getStatus() == BookingStatus.PENDING_PAYMENT) {
            //            booking.setPaymentMethod(paymentMethod);
            //            // Case: Payment Method is My Wallet
            //            if (paymentMethod == PaymentMethod.MY_WALLET) {
            //                Double debt = booking.getTotal() - car.getDeposit();
            //                if (debt <= 0) throw new AppException("This booking has been paid");
            //
            //                if (user.getWallet() < debt) throw new AppException("Let check your wallet");
            //                user.setWallet(user.getWallet() - car.getDeposit());
            //                userRepo.save(user);
            //            }
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepo.save(booking);
            return Response.successfulResponse("Confirm payment successfully");
        } else throw new AppException("The status of this booking does not allow to confirm payment");
    }

    @Override
    @Transactional
    public Response<String> cancelBooking(Integer userId, Integer bookingId) throws MessagingException {
        Booking booking = this.verifyBookingCustomer(userId, bookingId);
        // Car
        Optional<Car> findCar = carRepo.findById(booking.getCar().getId());
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();
        // Owner
        User owner = car.getCarOwner();
        // Customer
        Optional<User> findCustomer = userRepo.findById(booking.getUser().getId());
        if (findCustomer.isEmpty()) throw new AppException("This user is not existed");
        User customer = findCustomer.get();

        if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.PENDING_DEPOSIT) {
            // Set booking status
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(booking);
            // Return Deposit and Transaction
            // Owner
            if (owner.getWallet() < car.getDeposit())
                throw new AppException("Cancel booking fail. The car owner's wallet has no money");
            owner.setWallet(owner.getWallet() - car.getDeposit());
            User saveOwner = userRepo.save(owner);
            AddSystemTransactionRequestDTO ownerTran = AddSystemTransactionRequestDTO.builder()
                    .amount(-car.getDeposit())
                    .transactionType(TransactionType.REFUND_DEPOSIT)
                    .bookingId(booking.getId())
                    .carName(car.getName())
                    .user(saveOwner)
                    .build();
            transactionService.addTransaction(ownerTran);
            // Customer
            customer.setWallet(customer.getWallet() + car.getDeposit());
            User saveCustomer = userRepo.save(customer);
            AddSystemTransactionRequestDTO customerTran = AddSystemTransactionRequestDTO.builder()
                    .amount(car.getDeposit())
                    .transactionType(TransactionType.REFUND_DEPOSIT)
                    .bookingId(booking.getId())
                    .carName(car.getName())
                    .user(saveCustomer)
                    .build();
            transactionService.addTransaction(customerTran);
            // Send Mail To Owner
            String toMail = owner.getEmail();
            String subject = MailTemplate.CANCEL_BOOKING.CANCEL_BOOKING_SUBJECT;
            String template = MailTemplate.CANCEL_BOOKING.CANCEL_BOOKING_TEMPLATE;
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String cancelTime = now.format(formatter).toString();
            Map<String, Object> variable = Map.of("carName", car.getName(), "cancelTime", cancelTime);
            mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);
            return Response.successfulResponse("Cancel booking successful.");
        } else throw new AppException("The status of this booking does not allow to cancel");
    }

    @Override
    @Transactional
    public Response<String> returnCar(Integer userId, Integer bookingId) throws MessagingException {
        // Booking
        Booking booking = this.verifyBookingCustomer(userId, bookingId);
        // Car
        Car car = booking.getCar();
        // Owner
        User owner = booking.getCar().getCarOwner();
        // Customer
        User customer = booking.getUser();
        // Check status
        if (booking.getStatus() != BookingStatus.IN_PROGRESS)
            throw new AppException("The status of this booking does not allow to return a car");
        // Check Total and Deposit
        Double money = car.getDeposit() - booking.getTotal();
        String message;
        if (money < 0) {
            booking.setStatus(BookingStatus.PENDING_PAYMENT);
            try {
                bookingRepo.save(booking);
            } catch (Exception e) {
                throw new AppException("Return car fail");
            }
            message = "Rent is greater than deposit is " + (-money) + " . Next confirm payment step";
        } else {
            try {
                // Return deposit and pay rent
                // Owner and Transaction
                Double refundMoney = car.getDeposit() - booking.getTotal();
                if (owner.getWallet() < refundMoney)
                    throw new AppException("Return a car fail. The car owner's wallet has no money");
                owner.setWallet(owner.getWallet() - refundMoney);
                User saveOwner = userRepo.save(owner);
                AddSystemTransactionRequestDTO ownerTran = AddSystemTransactionRequestDTO.builder()
                        .amount(-refundMoney)
                        .transactionType(TransactionType.OFFSET_FINAL_PAYMENT)
                        .bookingId(booking.getId())
                        .carName(car.getName())
                        .user(saveOwner)
                        .build();
                transactionService.addTransaction(ownerTran);
                // Customer and Transaction
                customer.setWallet(customer.getWallet() + refundMoney);
                User saveCustomer = userRepo.save(customer);
                AddSystemTransactionRequestDTO customerTran = AddSystemTransactionRequestDTO.builder()
                        .amount(refundMoney)
                        .transactionType(TransactionType.OFFSET_FINAL_PAYMENT)
                        .bookingId(booking.getId())
                        .carName(car.getName())
                        .user(saveCustomer)
                        .build();
                transactionService.addTransaction(customerTran);
                // Set Status for Booking
                booking.setStatus(BookingStatus.COMPLETED);
                bookingRepo.save(booking);
            } catch (Exception e) {
                throw new AppException("Return car fail");
            }
            message = "Please confirm to return the car. The exceeding amount of XXXXX " + money
                    + " VND will be returned to your wallet.";
        }
        // Send Mail For Owner
        String toMail = owner.getEmail();
        String subject = MailTemplate.RETURN_A_CAR.RETURN_A_CAR_SUBJECT;
        String template = MailTemplate.RETURN_A_CAR.RETURN_A_CAR_TEMPLATE;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String returnTime = now.format(formatter).toString();
        Map<String, Object> variable = Map.of("carName", car.getName(), "returnTime", returnTime);
        mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

        return Response.successfulResponse(message);
    }
}
