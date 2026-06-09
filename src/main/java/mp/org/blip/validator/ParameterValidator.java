package mp.org.blip.validator;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.exception.ValidationError;

import java.util.regex.Pattern;

public final class ParameterValidator {

    private static final Pattern PARAM_NAME_PATTERN =
            Pattern.compile(
                    "[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*"
            );
    public static void validate(String input, ValidationContext validationContext, String parentProperty) {
        if (input == null || input.isBlank()) {
            return;
        }
        int index = 0;
        while ((index = input.indexOf("${", index)) != -1) {
            int end = input.indexOf("}", index);
            if (end == -1) {
                validationContext.addError(new ValidationError(parentProperty, "Unclosed parameter exception"));
                return;
            }
            String param = input.substring(index + 2, end);
            if (!PARAM_NAME_PATTERN.matcher(param).matches()) {
                validationContext.addError(new ValidationError(parentProperty, "Invalid output variable syntax: " + param + ""));
            }
            index = end + 1;
        }
    }
}
