package net.codejava.domain.dto.car;

import net.codejava.domain.dto.image.UpdImageRequestDTO;

public record UpdCarRequestDTO(
        Integer mileage,
        Float fuelConsumption,
        String address,
        String description,
        String additionalFunctions,
        UpdImageRequestDTO[] images,
        Double basePrice,
        Double deposit,
        String termsOfUse) {}
