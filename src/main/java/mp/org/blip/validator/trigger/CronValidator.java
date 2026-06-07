package mp.org.blip.validator.trigger;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TriggerDefinition;
import mp.org.blip.definition.trigger.CronConfigDefinition;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

@Component
public class CronValidator {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public CronValidator(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
    // give it validation context and don't worry
    public void validate(ValidationContext validationContext) {

        String parentProperty = "trigger.config.";
        // converts to CronConfigDefinition
        CronConfigDefinition cronConfigDefinition = this.objectMapper.convertValue(validationContext.getJobDefinition().getTrigger().getConfig(), CronConfigDefinition.class);

        // any semantic validations then here

        // check for constraint failures and add to context
        Set<ConstraintViolation<CronConfigDefinition>> violations = validator.validate(cronConfigDefinition);
        if(!violations.isEmpty()){
            violations.forEach(violation -> {
                validationContext.addError(new ValidationError(parentProperty + violation.getPropertyPath().toString(), violation.getMessage()));
            });
        }
    }
}
