package mp.org.blip.validator.onerror;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationError;

public class ContinueValidator {
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {
        if(taskDefinition.getOnError().getConfig() != null){
            validationContext.addError(new ValidationError(parentProperty + "config", "Continue action takes no arguments"));
        }
    }
}
