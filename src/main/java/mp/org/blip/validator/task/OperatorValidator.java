package mp.org.blip.validator.task;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.enumeration.TaskTypes;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class OperatorValidator {
    private final HttpValidator httpValidator;

    public OperatorValidator(HttpValidator httpValidator) {
        this.httpValidator = httpValidator;
    }

    public void validate(ValidationContext validationContext, Integer index, String parentPath) {
        String taskTypeString = validationContext.getJobDefinition().getTasks().get(index).getType();
        if(!TaskTypes.isValid(taskTypeString)){
            // make this use a message template instead and interpolate here
            validationContext.addError(new ValidationError(parentPath+"type", "Invalid task type"));
            return;
        }
        TaskTypes taskType = TaskTypes.from(taskTypeString);
        switch (taskType) {
            case TaskTypes.HTTP -> this.httpValidator.validate(validationContext, validationContext.getJobDefinition().getTasks().get(index), parentPath);
        }
    }
}
