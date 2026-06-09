package mp.org.blip.validator.trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.trigger.CronConfigDefinition;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.service.ObjectMapperService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CronValidator {
    private final ObjectMapperService objectMapperService;
    private final Validator validator;

    public CronValidator(ObjectMapperService objectMapperService, Validator validator) {
        this.objectMapperService = objectMapperService;
        this.validator = validator;
    }
    // give it validation context and don't worry
    public void validate(ValidationContext validationContext) {
        String parentProperty = "trigger.config.";
        // converts to CronConfigDefinition
        try {
            this.objectMapperService.convertValue(validationContext.getJobDefinition().getTrigger(), CronConfigDefinition.class, parentProperty, validationContext);
        } catch (IllegalArgumentException e) {
            String field = e.getMessage().split("\"")[1];
            validationContext.addError(new ValidationError(parentProperty+field, "Unrecognized field"));
        }
    }
}
