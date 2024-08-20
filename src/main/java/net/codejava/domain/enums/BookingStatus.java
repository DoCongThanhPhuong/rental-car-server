package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookingStatus {
    PENDING_DEPOSIT("Pedning deposit"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    PICK_UP("Pick up"),
    IN_PROGRESS("In progress"),
    PENDING_PAYMENT("Pending payment"),
    COMPLETED("Completed");
    private final String title;
}
