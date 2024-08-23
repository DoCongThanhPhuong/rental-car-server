package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FuelType {
    PETRO("Petro"),
    DIESEL("Diesel");
    private final String title;
}
