package io.samancore.validation;

import static io.samancore.util.GeneralConstant.PATTERN_LETTER_NUMBER;

public class ValidationComponent extends Validation {

    public static void validateIfNameContainAnySymbol(String name) {
        if (PATTERN_LETTER_NUMBER.matcher(name).find()) {
            throw new RuntimeException("There is a component with name with a character not allowed. Name=".concat(name));
        }
    }

    public static void validateIfNameBeginWithLowerCase(String name) {
        if (!Character.isLowerCase(name.charAt(0))) {
            throw new RuntimeException("There is a component with name begin without a lower case letter. Name=".concat(name));
        }
    }
}
