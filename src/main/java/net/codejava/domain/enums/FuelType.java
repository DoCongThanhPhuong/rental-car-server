package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FuelType {
    PETRO("Xăng"),
    DIESEL("Dầu Disel");
    private final String title;
}
