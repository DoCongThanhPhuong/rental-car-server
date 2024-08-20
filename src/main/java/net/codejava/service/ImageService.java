package net.codejava.service;

import net.codejava.domain.dto.image.ImageResponseDTO;
import net.codejava.domain.dto.image.UpdImageRequestDTO;
import net.codejava.responses.Response;

public interface ImageService {
    Response<ImageResponseDTO> updImage(UpdImageRequestDTO requestDTO, String folder);
}
