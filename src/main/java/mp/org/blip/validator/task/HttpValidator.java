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
import mp.org.blip.service.ObjectMapperService;
import mp.org.blip.service.RegisterService;
import mp.org.blip.util.ParameterExtractor;
import mp.org.blip.validator.ParameterValidator;
import mp.org.blip.validator.onerror.OnErrorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ObjectMapperService objectMapperService;
    private final Validator validator;
    private final OnErrorValidator onErrorValidator;
    Logger logger = LoggerFactory.getLogger(HttpValidator.class);

    public HttpValidator(ObjectMapperService objectMapperService, Validator validator, OnErrorValidator onErrorValidator) {
        this.objectMapperService = objectMapperService;
        this.validator = validator;
        this.onErrorValidator = onErrorValidator;
    }

    // tasks[0].
    public void validate(ValidationContext validationContext, TaskDefinition taskDefinition, String parentProperty) {

        try {
            HttpConfigDefinition definition = this.objectMapperService.convertValue(taskDefinition.getConfig(), HttpConfigDefinition.class, parentProperty+"config.", validationContext);
            if (definition == null) {
                return;
            }
            // structural validation
            if (!HttpMethodTypes.isValid(definition.getMethod())) {
                validationContext.addError(new ValidationError(parentProperty + "config.method", "Method not valid"));
            } else {
                HttpMethodTypes httpMethodType = HttpMethodTypes.from(definition.getMethod());
                if (httpMethodType == HttpMethodTypes.GET && definition.getBody() != null) {
                    validationContext.addError(new ValidationError(parentProperty + "config.body", "Body is not allowed for get"));
                }
            }
            if (!HttpResponseTypes.isValid(definition.getResponseType())) {
                validationContext.addError(new ValidationError(parentProperty + "config.response_type", "Response type not valid"));
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
                        validationContext.addError(new ValidationError(parentProperty + "config." + key, param + " not found"));
                    }
                }
            });
            // map the output to the task producing it and have a full dependency list
            validationContext.mergeDependencies(taskDefinition.getId(), params.get("url"));
            validationContext.mergeDependencies(taskDefinition.getId(), params.get("body"));
            validationContext.mergeDependencies(taskDefinition.getId(), params.get("headers"));

            this.onErrorValidator.validate(validationContext, taskDefinition, parentProperty + "on_error.");
        } catch (IllegalArgumentException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
            logger.info(e.getMessage());
            String field = e.getMessage().split("\"")[1];
            validationContext.addError(new ValidationError(parentProperty + field, "Invalid field" ));
        }

    }
}

