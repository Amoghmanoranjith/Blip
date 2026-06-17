package mp.org.blip.validator.annotationvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mp.org.blip.annotation.ValidCron;
import org.springframework.scheduling.support.CronExpression;

public class CronValidator implements ConstraintValidator<ValidCron, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return CronExpression.isValidExpression(value);
    }
}
