package net.codejava.service.impl;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.codejava.constant.MetaConstant;
import net.codejava.domain.dto.car.*;
import net.codejava.domain.dto.image.UpdImageRequestDTO;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.dto.meta.SortingDTO;
import net.codejava.domain.entity.Car;
import net.codejava.domain.entity.Image;
import net.codejava.domain.entity.User;
import net.codejava.domain.enums.UserType;
import net.codejava.domain.mapper.CarMapper;
import net.codejava.exceptions.AppException;
import net.codejava.repository.CarRepository;
import net.codejava.repository.UserRepository;
import net.codejava.repository.criteria.CarSearchCriteria;
import net.codejava.repository.criteria.PageCriteria;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;
import net.codejava.service.CarService;
import net.codejava.service.CloudinaryService;
import net.codejava.service.ImageService;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final CloudinaryService cloudinaryService;
    private final CarMapper carMapper;
    private final ImageService imageService;
    private final CarSearchCriteria carSearchCriteria;

    @Value("${cloudinary.folder.car}")
    private String carFolder;

    @Override
    public Car verifyCarOwner(Integer ownerId, Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();
        if (car.getCarOwner().getId() != ownerId) throw new AppException("Unauthorized");
        return car;
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarByOwner(
            MetaRequestDTO metaRequestDTO, Integer ownerId) {
        Optional<User> findUser = userRepo.findById(ownerId);
        if (findUser.isEmpty()) throw new AppException("This owner is not existed");
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Car> page = metaRequestDTO.keyword() == null
                ? carRepo.getListCarByOwner(ownerId, pageable)
                : carRepo.getListCarByOwnerWithKeyword(ownerId, metaRequestDTO.keyword(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List car is empty");
        List<CarResponseDTO> li = page.getContent().stream()
                .map(temp -> carMapper.toCarResponseDTO(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Get list car success",
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
    public Response<CarDetailResponseDTO> getCarDetail(Integer id) {
        Optional<Car> findCar = carRepo.findById(id);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get detail car successful", carMapper.toCarDetailResponseDTO(findCar.get()));
    }

    @Override
    public Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer id) {
        Optional<Car> findCar = carRepo.findByIdWithOwner(id);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get detail car for owner successful", carMapper.toCarDetailResponseForOwnerDTO(findCar.get()));
    }

    @Override
    @Transactional
    public Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO) {
        Optional<User> findOwner = userRepo.findUserByIdAndUserType(ownerId, UserType.OWNER);
        if (findOwner.isEmpty()) throw new AppException("This owner is not existed");

        Car newCar = carMapper.addCarRequestToCarEntity(requestDTO);
        // Set Owner for Car
        newCar.setCarOwner(findOwner.get());
        // Set Image for Car
        try {
            for (String item : requestDTO.images()) {
                Map resultUpload = cloudinaryService.uploadFileBase64(item, carFolder);
                Image imageUpload = Image.builder()
                        .name((String) resultUpload.get("original_filename"))
                        .imageUrl((String) resultUpload.get("url"))
                        .imagePublicId((String) resultUpload.get("public_id"))
                        .whenCreated(new Date())
                        .build();
                newCar.addImage(imageUpload);
            }
            Car saveCar = carRepo.save(newCar);
            return Response.successfulResponse("Add new car successful", carMapper.toCarDetailResponseDTO(saveCar));
        } catch (IOException e) {
            throw new AppException("Add new car fail");
        }
    }

    @Override
    public Response<CarDetailResponseDTO> updateCar(Integer id, UpdCarRequestDTO requestDTO) {
        Optional<Car> oldCar = carRepo.findById(id);
        if (oldCar.isEmpty()) throw new AppException("This car is not existed");
        Car newCar = carMapper.updCarRequestToCarEntity(oldCar.get(), requestDTO);
        // Update Image For Car
        if (requestDTO.images().length > 0) {
            for (UpdImageRequestDTO item : requestDTO.images()) {
                imageService.updImage(item, carFolder);
            }
        }
        List<Image> newImage = carRepo.findById(id).get().getImages();
        newCar.setImages(newImage);
        Car saveCar = carRepo.save(newCar);
        return Response.successfulResponse("Update a Car successful", carMapper.toCarDetailResponseDTO(saveCar));
    }

    @Override
    public Response<String> stopRentingCar(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();
        String message = "Stop renting a car successful";
        if (car.getIsStopped()) message = "Re-renting a car successful";
        findCar.get().setIsStopped(!findCar.get().getIsStopped());
        carRepo.save(findCar.get());
        return Response.successfulResponse(message);
    }

    @Override
    public PageCriteria<List<CarResponseDTO>> searchCar(MetaRequestDTO requestDTO, String... search) {
        PageCriteria<List<Car>> cars = carSearchCriteria.advanceSearchCar(requestDTO);
        if (cars.item().isEmpty()) throw new AppException("List car is empty");
        List<CarResponseDTO> li = cars.item().stream()
                .map(temp -> carMapper.toCarResponseDTO(temp))
                .toList();
        return PageCriteria.<List<CarResponseDTO>>builder()
                .totalItems(cars.totalItems())
                .totalPages(cars.totalPages())
                .currentPage(cars.currentPage())
                .pageSize(cars.pageSize())
                .sorting(cars.sorting())
                .item(li)
                .build();
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCarV2(
            String address, String startTime, String endTime, MetaRequestDTO metaRequestDTO) {
        String field = metaRequestDTO.sortField();
        if (field.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) field = "car_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Car> page = carRepo.searchCarV2(address, startTime, endTime, pageable);

        if (page.getContent().isEmpty()) throw new AppException("List car is empty");
        List<CarResponseDTO> li = page.getContent().stream()
                .map(temp -> carMapper.toCarResponseDTO(temp))
                .toList();
        return MetaResponse.successfulResponse(
                "Search car success",
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
}
