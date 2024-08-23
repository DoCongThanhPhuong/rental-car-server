package net.codejava.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CarTransimission {
    AUTOMATIVE("Automative"),
    MANUAL("Manual");
    private final String title;
}
