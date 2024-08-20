package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserInforType {
    RENTER("Renter"),
    DRIVER("Driver");
    private final String title;
}
