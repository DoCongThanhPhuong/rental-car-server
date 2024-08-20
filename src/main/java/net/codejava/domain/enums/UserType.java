package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {
    CUSTOMER("Khách hàng"),
    OWNER("Chủ xe");
    private final String title;
}
