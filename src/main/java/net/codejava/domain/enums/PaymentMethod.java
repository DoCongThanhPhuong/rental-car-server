package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentMethod {
    MY_WALLET("My wallet"),
    CASH("Cash"),
    BANK("Bank Transfer");
    private final String title;
}
