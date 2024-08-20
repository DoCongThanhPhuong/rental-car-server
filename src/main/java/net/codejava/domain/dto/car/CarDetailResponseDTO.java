package net.codejava.domain.dto.car;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import net.codejava.domain.dto.image.ImageResponseDTO;
import net.codejava.domain.enums.CarTransimission;
import net.codejava.domain.enums.FuelType;

@Builder
public record CarDetailResponseDTO(
        Integer id,
        String name,
        String licensePlate,
        String brand,
        String color,
        String model,
        Integer numberOfSeats,
        Integer productionYear,
        CarTransimission transmissionType,
        FuelType fuelType,
        Integer mileage,
        Float fuelConsumption,
        Double basePrice,
        Double deposit,
        String address,
        String description,
        String termsOfUse,
        String additionalFunctions,
        Date createdAt,
        Date updatedAt,
        Double rating,
        Boolean isStopped,
        Boolean isAvailable,
        List<ImageResponseDTO> images) {}
