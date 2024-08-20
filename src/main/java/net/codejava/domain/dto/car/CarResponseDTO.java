package net.codejava.domain.dto.car;

import java.util.List;

import lombok.Builder;
import net.codejava.domain.dto.image.ImageResponseDTO;

@Builder
public record CarResponseDTO(
        Integer id,
        String name,
        String brand,
        Double basePrice,
        String model,
        Double rating,
        String address,
        Boolean isStopped,
        Boolean isAvailable,
        Boolean isActive,
        List<ImageResponseDTO> images) {}
