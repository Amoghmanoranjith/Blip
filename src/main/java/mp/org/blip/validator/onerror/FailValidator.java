package mp.org.blip.validator.onerror;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class FailValidator {
    // no config
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        if(taskDefinition.getOnError().getConfig() != null){
            validationContext.addError(new ValidationError(parentProperty + "config", "Fail action takes no arguments"));
        }
    }
}
