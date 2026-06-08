package mp.org.blip.validator.task;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.enumeration.TaskTypes;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class TaskValidator {
    private final HttpValidator httpValidator;
    private final DelayValidator delayValidator;
    public TaskValidator(HttpValidator httpValidator, DelayValidator delayValidator) {
        this.httpValidator = httpValidator;
        this.delayValidator = delayValidator;
    }
    // tasks[0].
    public void validate(ValidationContext validationContext, Integer index, String parentProperty) {
        String taskTypeString = validationContext.getJobDefinition().getTasks().get(index).getType();
        if(!TaskTypes.isValid(taskTypeString)){
            // make this use a message template instead and interpolate here
            validationContext.addError(new ValidationError(parentProperty+"type", "Invalid task type"));
            return;
        }
        TaskTypes taskType = TaskTypes.from(taskTypeString);
        switch (taskType) {
            case TaskTypes.HTTP -> this.httpValidator.validate(validationContext, validationContext.getJobDefinition().getTasks().get(index), parentProperty);
            case TaskTypes.DELAY -> this.delayValidator.validate(validationContext, validationContext.getJobDefinition().getTasks().get(index), parentProperty);
        }
    }
}
