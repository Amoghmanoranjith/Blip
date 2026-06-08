package mp.org.blip.validator.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.task.DelayConfigDefinition;
import mp.org.blip.definition.task.HttpConfigDefinition;
import mp.org.blip.exception.ValidationError;

import java.util.Set;

public class DelayValidator {
    // id
    // type
    // config has to have delay
    // dependencies
    // output is null
    // on error is null

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public DelayValidator(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        DelayConfigDefinition definition = this.objectMapper.convertValue(taskDefinition.getConfig(), DelayConfigDefinition.class);
        Set<ConstraintViolation<DelayConfigDefinition>> violations = this.validator.validate(definition);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<DelayConfigDefinition> violation : violations) {
                validationContext.addError(new ValidationError(parentProperty + "config." + violation.getPropertyPath().toString(), violation.getMessage()));
            }
        }

        if(taskDefinition.getOutput() != null){
            validationContext.addError(new ValidationError(parentProperty + "output", "This type of task cannot have an output"));
        }

    }
}
