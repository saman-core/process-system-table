package io.samancore.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CaseType {
    NONE("NONE", "NONE"),
    LOWERCASE("L", "LowerCase"),
    UPPERCASE("U", "UpperCase");

    final String description;
    final String initial;

    CaseType(String initial, String description) {
        this.initial = initial;
        this.description = description;
    }

    public static CaseType getByDescription(String description) {
        return Arrays.stream(values()).filter(caseType -> (caseType.getDescription().equalsIgnoreCase(description))).findAny().orElse(NONE);
    }

    public static CaseType getByInitialOrDescriptionDefaultLowerCase(String value) {
        return Arrays.stream(values()).filter(caseType -> (caseType.getDescription().equalsIgnoreCase(value) || caseType.getInitial().equalsIgnoreCase(value))).findAny().orElse(LOWERCASE);
    }
}
