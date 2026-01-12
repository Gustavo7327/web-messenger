package br.com.web.messenger.util;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static String joinFieldErrors(BindingResult bindingResult) {
        if (bindingResult == null || !bindingResult.hasErrors()) {
            return "";
        }
        return bindingResult.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

}
