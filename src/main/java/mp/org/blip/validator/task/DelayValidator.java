package mp.org.blip.validator.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.task.DelayConfigDefinition;
import mp.org.blip.definition.task.HttpConfigDefinition;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.service.ObjectMapperService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DelayValidator {
    // id
    // type
    // config has to have delay
    // dependencies
    // output is null
    // on error is null

    private final ObjectMapperService objectMapperService;
    private final Validator validator;

    public DelayValidator(ObjectMapperService objectMapperService, Validator validator) {
        this.objectMapperService = objectMapperService;
        this.validator = validator;
    }

    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        this.objectMapperService.convertValue(taskDefinition.getConfig(), DelayConfigDefinition.class, parentProperty, validationContext);
        if(taskDefinition.getOutput() != null){
            validationContext.addError(new ValidationError(parentProperty + "output", "This type of task cannot have an output"));
        }

    }
}
