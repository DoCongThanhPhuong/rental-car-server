package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CarTransimission {
    AUTOMATIVE("Tự dộng"),
    MANUAL("Thủ công");
    private final String title;
}
