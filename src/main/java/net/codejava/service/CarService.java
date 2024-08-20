package net.codejava.service;

import java.util.List;

import net.codejava.domain.dto.car.*;
import net.codejava.domain.dto.meta.MetaRequestDTO;
import net.codejava.domain.dto.meta.MetaResponseDTO;
import net.codejava.domain.entity.Car;
import net.codejava.repository.criteria.PageCriteria;
import net.codejava.responses.MetaResponse;
import net.codejava.responses.Response;

public interface CarService {
    Car verifyCarOwner(Integer ownerId, Integer carId);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarByOwner(
            MetaRequestDTO metaRequestDTO, Integer ownerId);

    Response<CarDetailResponseDTO> getCarDetail(Integer id);

    Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer id);

    Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO);

    Response<CarDetailResponseDTO> updateCar(Integer id, UpdCarRequestDTO requestDTO);

    Response<String> stopRentingCar(Integer carId);

    PageCriteria<List<CarResponseDTO>> searchCar(MetaRequestDTO requestDTO, String... search);

    MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCarV2(
            String address, String startTime, String endTime, MetaRequestDTO metaRequestDTO);
}
