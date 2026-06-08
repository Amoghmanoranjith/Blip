package mp.org.blip.validator.onerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.onerror.FallbackConfigDefinition;
import mp.org.blip.definition.onerror.RetryConfigDefinition;
import mp.org.blip.exception.ValidationError;

import java.util.Set;

public class FallbackValidator {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public FallbackValidator(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    // property will be like task[0].on_error.config.
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        FallbackConfigDefinition definition = this.objectMapper.convertValue(taskDefinition.getOnError().getConfig(), FallbackConfigDefinition.class);
        Set<ConstraintViolation<FallbackConfigDefinition>> violations = this.validator.validate(definition);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> {
                validationContext.addError(new ValidationError(parentProperty + violation.getPropertyPath().toString(), violation.getMessage()));
            });
        }
        // check if task is present or not
        if(!validationContext.getTaskCountMap().containsKey(definition.getFallbackTask())){
            validationContext.addError(new ValidationError(parentProperty + "fallback", "Fallback does not exist."));
        }
        // add this as a dependency
        validationContext.mergeDependency(taskDefinition.getId(), definition.getFallbackTask());

    }
}
