package mp.org.blip.validator.onerror;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.onerror.RetryConfigDefinition;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.service.ObjectMapperService;
import org.springframework.stereotype.Component;

import java.util.Set;

// optionally has number of retries and a delay between retries setting
@Component
public class RetryValidator {
    private final Validator validator;
    private final ObjectMapperService objectMapperService;
    public RetryValidator(Validator validator, ObjectMapperService objectMapperService) {
        this.validator = validator;
        this.objectMapperService = objectMapperService;
    }
    // property will be like task[0].on_error.config.
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        this.objectMapperService.convertValue(taskDefinition.getOnError().getConfig(), RetryConfigDefinition.class, parentProperty, validationContext);
    }
}
