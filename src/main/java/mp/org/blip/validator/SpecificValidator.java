package mp.org.blip.validator;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.validator.task.OperatorValidator;
import mp.org.blip.validator.trigger.TriggerValidator;
import org.springframework.stereotype.Component;

@Component
public class SpecificValidator {
    // hold all the children validators and use when needed

    private final TriggerValidator triggerValidator;
    private final OperatorValidator operatorValidator;

    public SpecificValidator(TriggerValidator triggerValidator, OperatorValidator operatorValidator) {
        this.triggerValidator = triggerValidator;
        this.operatorValidator = operatorValidator;
    }

    public void validate(ValidationContext validationContext){

        //validate the trigger
        triggerValidator.validate(validationContext, "trigger.");

        // go through each task and validate them according to their rules
        for(int i = 0;i< validationContext.getJobDefinition().getTasks().size();i++){
            this.operatorValidator.validate(validationContext, i, "tasks[" + i + "].");
        }
    }
}
