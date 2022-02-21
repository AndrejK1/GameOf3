package com.softkit.annotation;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private final PasswordValidator validator = new PasswordValidator(Arrays.asList(
            // needs at least 8 characters and at most 100 chars
            new LengthRule(8, 100),
            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),
            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1),
            // no whitespace
            new WhitespaceRule()
    ));

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return validator.validate(new PasswordData(password)).isValid();
    }
}
