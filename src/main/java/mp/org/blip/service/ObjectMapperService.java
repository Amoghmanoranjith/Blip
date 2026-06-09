package mp.org.blip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.task.HttpConfigDefinition;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ObjectMapperService {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public ObjectMapperService(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public <T> T convertValue(Object source, Class<T> targetType, String parentProperty, ValidationContext validationContext) {
        try {
            T value = objectMapper.convertValue(source, targetType);
            Set<ConstraintViolation<T>> violations = this.validator.validate(value);

            if (!violations.isEmpty()) {
                for (ConstraintViolation<T> violation : violations) {
                    validationContext.addError(new ValidationError(parentProperty + "config." + violation.getPropertyPath().toString(), violation.getMessage()));
                }
            }
            return value;
        } catch (IllegalArgumentException e) {
            String field = e.getMessage().split("\"")[1];
            validationContext.addError(new ValidationError(parentProperty + field, "Unrecognized field"));
            return null;
        }
    }
}
