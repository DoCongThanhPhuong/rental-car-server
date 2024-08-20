package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionType {
    TOP_UP("Top up"),
    WITHDRAW("Withdraw"),
    PAY_DEPOSIT("Pay deposit"),
    RECEIVE_DEPOSIT("Receive deposit"),
    REFUND_DEPOSIT("Refund deposit"),
    OFFSET_FINAL_PAYMENT("Offset final paymetn");

    private final String title;
}
