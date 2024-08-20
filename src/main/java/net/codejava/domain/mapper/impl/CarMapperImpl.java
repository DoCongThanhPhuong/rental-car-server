package net.codejava.domain.mapper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.codejava.domain.dto.car.*;
import net.codejava.domain.dto.image.ImageResponseDTO;
import net.codejava.domain.entity.Car;
import net.codejava.domain.mapper.CarMapper;
import net.codejava.domain.mapper.ImageMapper;
import net.codejava.domain.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {
    private final UserMapper userMapper;
    private final ImageMapper imageMapper;

    @Override
    public CarResponseDTO toCarResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(image -> imageMapper.toImageResponseDTO(image))
                .collect(Collectors.toList());
        return CarResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .model(entity.getBrand())
                .basePrice(entity.getBasePrice())
                .rating(entity.getRating())
                .address(entity.getAddress())
                .isStopped(entity.getIsStopped())
                .isAvailable(entity.getIsAvailable())
                .isActive(entity.getIsActive())
                .images(imageList)
                .build();
    }

    @Override
    public CarDetailResponseDTO toCarDetailResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(image -> imageMapper.toImageResponseDTO(image))
                .collect(Collectors.toList());

        return CarDetailResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .color(entity.getColor())
                .model(entity.getModel())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmissionType(entity.getCarTransimission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .basePrice(entity.getBasePrice())
                .deposit(entity.getDeposit())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .termsOfUse(entity.getTermsOfUse())
                .additionalFunctions(entity.getAdditionalFunctions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .rating(entity.getRating())
                .isStopped(entity.getIsStopped())
                .isAvailable(entity.getIsAvailable())
                .images(imageList)
                .build();
    }

    @Override
    public CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity) {
        List<String> imageList =
                entity.getImages().stream().map(image -> image.getImageUrl()).collect(Collectors.toList());

        return CarDetailResponseForOwnerDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .color(entity.getColor())
                .model(entity.getModel())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmissionType(entity.getCarTransimission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .basePrice(entity.getBasePrice())
                .deposit(entity.getDeposit())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .termsOfUse(entity.getTermsOfUse())
                .additionalFunctions(entity.getAdditionalFunctions())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .rating(entity.getRating())
                .isAvailable(entity.getIsAvailable())
                .owner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .images(imageList)
                .build();
    }

    @Override
    public Car addCarRequestToCarEntity(AddCarRequestDTO requestDTO) {
        return Car.builder()
                .name(requestDTO.name())
                .licensePlate(requestDTO.licensePlate())
                .brand(requestDTO.brand())
                .color(requestDTO.color())
                .model(requestDTO.model())
                .numberOfSeats(requestDTO.numberOfSeats())
                .productionYear(requestDTO.productionYear())
                .carTransimission(requestDTO.carTransimission())
                .fuelType(requestDTO.fuelType())
                .mileage(requestDTO.mileage())
                .fuelConsumption(requestDTO.fuelConsumption())
                .address(requestDTO.address())
                .description(requestDTO.description())
                .additionalFunctions(requestDTO.additionalFunctions())
                .basePrice(requestDTO.basePrice())
                .deposit(requestDTO.deposit())
                .termsOfUse(requestDTO.termsOfUse())
                .createdAt(new Date())
                .updatedAt(null)
                .rating(null)
                .images(new ArrayList<>())
                .isAvailable(true)
                .isStopped(false)
                .isActive(true)
                .build();
    }

    @Override
    public Car updCarRequestToCarEntity(Car oldEntity, UpdCarRequestDTO requestDTO) {
        oldEntity.setMileage(requestDTO.mileage());
        oldEntity.setFuelConsumption(requestDTO.fuelConsumption());
        oldEntity.setAddress(requestDTO.address());
        oldEntity.setDescription(requestDTO.description());
        oldEntity.setAdditionalFunctions(requestDTO.additionalFunctions());
        oldEntity.setBasePrice(requestDTO.basePrice());
        oldEntity.setDeposit(requestDTO.deposit());
        oldEntity.setTermsOfUse(requestDTO.termsOfUse());
        return oldEntity;
    }
}
