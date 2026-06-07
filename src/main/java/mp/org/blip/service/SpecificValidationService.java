package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.validator.task.TaskValidator;
import mp.org.blip.validator.trigger.TriggerValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SpecificValidationService {
    // hold all the children validators and use when needed

    private final TriggerValidator triggerValidator;
    private final TaskValidator taskValidator;

    public SpecificValidationService(TriggerValidator triggerValidator, TaskValidator taskValidator) {
        this.triggerValidator = triggerValidator;
        this.taskValidator = taskValidator;
    }

    public void validate(ValidationContext validationContext){

        //validate the trigger
        triggerValidator.validate(validationContext, "trigger.");

        Map<String, Integer> getTaskIndex = new HashMap<>();
        for(int i = 0;i<validationContext.getJobDefinition().getTasks().size();i++){
            this.taskValidator.validate(validationContext, i, "tasks["+i+"].");
            getTaskIndex.put(validationContext.getJobDefinition().getTasks().get(i).getId(), i);
        }
    }
}
