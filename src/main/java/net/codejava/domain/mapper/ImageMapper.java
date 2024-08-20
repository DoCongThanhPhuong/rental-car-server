package net.codejava.domain.mapper;

import net.codejava.domain.dto.image.ImageResponseDTO;
import net.codejava.domain.entity.Image;

public interface ImageMapper {
    ImageResponseDTO toImageResponseDTO(Image entity);
}
