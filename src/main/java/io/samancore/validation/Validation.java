package io.samancore.validation;

import java.util.Locale;

import static io.samancore.util.GeneralConstant.MAX_LENGTH_NAME_ALLOWED;
import static io.samancore.util.GeneralConstant.RESERVED_WORDS;

public class Validation {

    public void validateIfNameIsAReservedWord(String name) {
        if (RESERVED_WORDS.contains(name.toLowerCase(Locale.ROOT))) {
            throw new RuntimeException("There is a element with name not allowed. Name=".concat(name));
        }
    }

    public void validateLengthName(String name, String message) {
        if (name != null && name.length() > MAX_LENGTH_NAME_ALLOWED) {
            throw new RuntimeException(message);
        }
    }
}
