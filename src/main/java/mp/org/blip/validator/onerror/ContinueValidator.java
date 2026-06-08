package mp.org.blip.validator.onerror;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.OnErrorDefinition;
import mp.org.blip.exception.ValidationError;

public class ContinueValidator {
    public void validate(ValidationContext validationContext, OnErrorDefinition onErrorDefinition, String parentProperty) {
        if(onErrorDefinition.getConfig() != null){
            validationContext.addError(new ValidationError(parentProperty + "config", "Continue action takes no arguments"));
        }
    }
}
