package io.samancore.type;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
public enum SensitiveDataMaskType {
    NONE("none"),
    SHOW_LAST4("showLast4"),
    SHOW_FIRST2_LAST2("showFirst2Last2"),
    SHOW_FIRST3_LAST3("showFirst3Last3");

    private String description;

    SensitiveDataMaskType(String description) {
        this.description = description;
    }

    public static SensitiveDataMaskType getByDescription(String value) {
        return Arrays.stream(values()).filter(sensitiveDataMaskType -> (sensitiveDataMaskType.getDescription().equalsIgnoreCase(value))).findAny().orElse(NONE);
    }

    public String getDescriptionCapitalize() {
        return StringUtils.capitalize(description);
    }
}
