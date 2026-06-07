package mp.org.blip.validator.trigger;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.enumeration.TriggerTypes;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class TriggerValidator {
    private final CronValidator cronValidator;

    public TriggerValidator(CronValidator cronValidator) {
        this.cronValidator = cronValidator;
    }

    public void validate(ValidationContext validationContext, String parentPath) {
        String triggerTypeString = validationContext.getJobDefinition().getTrigger().getType();
        if(!TriggerTypes.isValid(triggerTypeString)){
            // make this use a message template instead and interpolate here
            validationContext.addError(new ValidationError(parentPath+"type", "Invalid trigger type"));
            return;
        }

        TriggerTypes triggerType = TriggerTypes.from(triggerTypeString);
        switch (triggerType) {
            case TriggerTypes.CRON:{
                this.cronValidator.validate(validationContext);
            }
        }
    }
}
