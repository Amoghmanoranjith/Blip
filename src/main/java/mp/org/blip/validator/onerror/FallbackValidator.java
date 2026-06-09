package mp.org.blip.validator.onerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.onerror.FallbackConfigDefinition;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.service.ObjectMapperService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FallbackValidator {
    private final ObjectMapperService objectMapperService;
    private final Validator validator;

    public FallbackValidator(ObjectMapperService objectMapperService, Validator validator) {
        this.objectMapperService = objectMapperService;
        this.validator = validator;
    }

    // property will be like task[0].on_error.config.
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        FallbackConfigDefinition definition = this.objectMapperService.convertValue(taskDefinition.getOnError().getConfig(), FallbackConfigDefinition.class,  parentProperty, validationContext);
        if(definition == null) {
            return;
        }
        // check if task is present or not
        if(!validationContext.getTaskCountMap().containsKey(definition.getFallbackTask())){
            validationContext.addError(new ValidationError(parentProperty + "fallback", "Fallback does not exist."));
        }
        // add this as a dependency
        validationContext.mergeDependency(taskDefinition.getId(), definition.getFallbackTask());

    }
}
