package mp.org.blip.validator.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.definition.task.HttpConfigDefinition;
import mp.org.blip.enumeration.HttpMethodTypes;
import mp.org.blip.enumeration.HttpResponseTypes;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.util.ParameterExtractor;
import mp.org.blip.validator.ParameterValidator;
import org.springframework.stereotype.Component;

import java.util.*;


// for structural validations are defined in HttpConfigDef
// coming to semantic validations
// we need to make sure that the params in config are validated
// extract these params first put in a list
// we wont know if the fields are correct or not
// we can only check the variable being used exists or not
//
@Component
public class HttpValidator {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public HttpValidator(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {

        HttpConfigDefinition definition = this.objectMapper.convertValue(taskDefinition.getConfig(), HttpConfigDefinition.class);
        Set<ConstraintViolation<HttpConfigDefinition>> violations = this.validator.validate(definition);
        // structural validation
        if (!HttpMethodTypes.isValid(definition.getMethod())) {
            validationContext.addError(new ValidationError(parentProperty + "config.method", "Method not valid"));
        }
        else{
            HttpMethodTypes httpMethodType = HttpMethodTypes.from(definition.getMethod());
            if (httpMethodType == HttpMethodTypes.GET && definition.getBody() == null) {
                validationContext.addError(new ValidationError(parentProperty + "config.body", "Body is required for get"));
            }
        }
        if (!HttpResponseTypes.isValid(definition.getResponseType())) {
            validationContext.addError(new ValidationError(parentProperty + "config.response_type", "Response type not valid"));
        }
        if (!violations.isEmpty()) {
            for (ConstraintViolation<HttpConfigDefinition> violation : violations) {
                validationContext.addError(new ValidationError(parentProperty + "config." + violation.getPropertyPath().toString(), violation.getMessage()));
            }
        }
        // semantic validation
        // validate syntactically data from each field
        ParameterValidator.validate(definition.getUrl(), validationContext, parentProperty + "config.url");
        ParameterValidator.validate(definition.getBody(), validationContext, parentProperty + "config.body");
        ParameterValidator.validate(definition.getHeaders(), validationContext, parentProperty + "config.headers");
        // extract data from each field
        Map<String, Set<String>> params = new HashMap<>();
        params.put("url", ParameterExtractor.extract(definition.getUrl()));
        params.put("body", ParameterExtractor.extract(definition.getBody()));
        params.put("headers", ParameterExtractor.extract(definition.getHeaders()));
        // check if output being used is present or not
        params.forEach((key, value) -> {
            for (String param : value) {
                if (!validationContext.getReferenceCountMap().containsKey(param)) {
                    validationContext.addError(new ValidationError(parentProperty + "config." + key, param));
                }
            }
        });
        // map the output to the task producing it and have a full dependency list
        validationContext.mergeDependencies(taskDefinition.getId(), params.get("url"));
        validationContext.mergeDependencies(taskDefinition.getId(), params.get("body"));
        validationContext.mergeDependencies(taskDefinition.getId(), params.get("headers"));

    }
}

