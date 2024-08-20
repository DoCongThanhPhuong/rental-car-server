package net.codejava.domain.mapper;

import net.codejava.domain.dto.car.*;
import net.codejava.domain.entity.Car;

public interface CarMapper {
    CarResponseDTO toCarResponseDTO(Car entity);

    CarDetailResponseDTO toCarDetailResponseDTO(Car entity);

    CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity);

    Car addCarRequestToCarEntity(AddCarRequestDTO requestDTO);

    Car updCarRequestToCarEntity(Car oldEntity, UpdCarRequestDTO requestDTO);
}
