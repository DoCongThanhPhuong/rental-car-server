package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {
    CUSTOMER("Customer"),
    OWNER("Car owner");
    private final String title;
}
