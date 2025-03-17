package io.samancore.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EncryptType {
    NONE("false", null, null, null, null),
    TRUE("true", 470, 512, "encrypt", "decrypt"),
    SYMMETRIC("symmetric", 4096, 4096, "encrypt", "decrypt"),
    ASYMMETRIC("asymmetric", 470, 512, "encrypt", "decrypt");

    private String description;
    private Integer modelMaxLength;
    private Integer dbMaxLength;
    private String encryptMethod;
    private String decryptMethod;

    EncryptType(String description, Integer modelMaxLength, Integer dbMaxLength, String encryptMethod, String decryptMethod) {
        this.description = description;
        this.modelMaxLength = modelMaxLength;
        this.dbMaxLength = dbMaxLength;
        this.encryptMethod = encryptMethod;
        this.decryptMethod = decryptMethod;
    }

    public static EncryptType getByDescription(String value) {
        return Arrays.stream(values()).filter(encryptType -> (encryptType.getDescription().equalsIgnoreCase(value))).findAny().orElse(NONE);
    }
}
