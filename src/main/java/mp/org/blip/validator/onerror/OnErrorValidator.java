package mp.org.blip.validator.onerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.enumeration.OnErrorTypes;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class OnErrorValidator {

    private final FailValidator failValidator;
    private final ContinueValidator continueValidator;
    private final RetryValidator retryValidator;
    private final FallbackValidator fallbackValidator;

    public OnErrorValidator(FailValidator failValidator, ContinueValidator continueValidator, RetryValidator retryValidator, FallbackValidator fallbackValidator) {
        this.failValidator = failValidator;
        this.continueValidator = continueValidator;
        this.retryValidator = retryValidator;
        this.fallbackValidator = fallbackValidator;
    }

    // task[0].on_error.
    public void validate(ValidationContext validationContext, OnErrorDefinition onErrorDefinition, String parentProperty) {
        // is the type of onerror provided valid?
        String onErrorTypeString = onErrorDefinition.getAction();
        if(!OnErrorTypes.isValid(onErrorTypeString)) {
            validationContext.addError(new ValidationError(parentProperty + "action", "Action is not valid"));
            return; // cant move to switch
        }
        switch (OnErrorTypes.valueOf(onErrorTypeString)) {
            case OnErrorTypes.FAIL -> {
                this.failValidator.validate(validationContext, onErrorDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.CONTINUE -> {
                this.continueValidator.validate(validationContext, onErrorDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.RETRY -> {
                this.retryValidator.validate(validationContext, onErrorDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.FALLBACK -> {
                this.fallbackValidator.validate(validationContext, onErrorDefinition, parentProperty + "config.");
            }
        }
    }
}
