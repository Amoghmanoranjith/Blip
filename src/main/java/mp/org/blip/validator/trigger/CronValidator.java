package mp.org.blip.validator.trigger;

import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.trigger.CronConfigDefinition;
import mp.org.blip.service.ObjectMapperService;
import org.springframework.stereotype.Component;


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
        this.objectMapperService.convertValue(validationContext.getJobDefinition().getTrigger().getConfig(), CronConfigDefinition.class, parentProperty, validationContext);
    }
}
