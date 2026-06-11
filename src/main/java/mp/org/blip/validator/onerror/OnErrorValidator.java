package mp.org.blip.validator.onerror;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
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
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        // is the type of onerror provided valid?
        // if no on_error then return
        if(taskDefinition.getOnError() == null)
            return;
        String onErrorTypeString = taskDefinition.getOnError().getAction();
        if(!OnErrorTypes.isValid(onErrorTypeString)) {
            validationContext.addError(new ValidationError(parentProperty + "action", "Action is not valid"));
            return; // cant move to switch
        }
        switch (OnErrorTypes.from(onErrorTypeString)) {
            case OnErrorTypes.FAIL -> {
                this.failValidator.validate(validationContext, taskDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.CONTINUE -> {
                this.continueValidator.validate(validationContext, taskDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.RETRY -> {
                this.retryValidator.validate(validationContext, taskDefinition, parentProperty + "config.");
            }
            case OnErrorTypes.FALLBACK -> {
                this.fallbackValidator.validate(validationContext, taskDefinition, parentProperty + "config.");
            }
        }
    }
}
