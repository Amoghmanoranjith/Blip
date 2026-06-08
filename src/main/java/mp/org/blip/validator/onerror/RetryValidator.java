package mp.org.blip.validator.onerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.onerror.RetryConfigDefinition;
import mp.org.blip.exception.ValidationError;

import java.util.Set;

// optionally has number of retries and a delay between retries setting
public class RetryValidator {
    private final ObjectMapper objectMapper;
    private final Validator validator;
    public RetryValidator(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
    // property will be like task[0].on_error.config.
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        RetryConfigDefinition definition = this.objectMapper.convertValue(taskDefinition.getOnError().getConfig(), RetryConfigDefinition.class);
        Set<ConstraintViolation<RetryConfigDefinition>> violations = this.validator.validate(definition);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> {
                validationContext.addError(new ValidationError(parentProperty + violation.getPropertyPath().toString(), violation.getMessage()));
            });
        }
    }
}
