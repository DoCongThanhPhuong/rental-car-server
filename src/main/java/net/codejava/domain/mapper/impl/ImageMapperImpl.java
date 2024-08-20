package net.codejava.domain.mapper.impl;

import org.springframework.stereotype.Component;

import net.codejava.domain.dto.image.ImageResponseDTO;
import net.codejava.domain.entity.Image;
import net.codejava.domain.mapper.ImageMapper;

@Component
public class ImageMapperImpl implements ImageMapper {
    @Override
    public ImageResponseDTO toImageResponseDTO(Image entity) {
        return ImageResponseDTO.builder()
                .id(entity.getId())
                .imageName(entity.getName())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
