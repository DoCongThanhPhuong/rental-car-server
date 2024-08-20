package net.codejava.domain.dto.car;

import net.codejava.domain.enums.CarTransimission;
import net.codejava.domain.enums.FuelType;

public record AddCarRequestDTO(
        String licensePlate,
        String name,
        String brand,
        String color,
        String model,
        Integer numberOfSeats,
        Integer productionYear,
        CarTransimission carTransimission,
        FuelType fuelType,
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        String[] images,
        Double basePrice,
        Double deposit,
        String termsOfUse) {}
