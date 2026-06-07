package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.exception.ValidationException;
import mp.org.blip.validator.SemanticValidator;
import mp.org.blip.validator.SpecificValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class RegisterService {
    private final SemanticValidator semanticValidator;
    private final FileParserService fileParserService;
    private final SpecificValidator specificValidator;
    Logger logger = LoggerFactory.getLogger(RegisterService.class);
    public RegisterService(SemanticValidator semanticValidator, FileParserService fileParserService, SpecificValidator specificValidator) {
        this.semanticValidator = semanticValidator;
        this.fileParserService = fileParserService;
        this.specificValidator = specificValidator;
    }
    public void register(String path) {
        // build a context
        ValidationContext validationContext = ValidationContext
                .builder()
                .jobDefinition(null)
                .errors(new HashSet<>())
                .referenceCountMap(null)
                .taskCountMap(null)
                .taskMap(null)
                .build();

        this.fileParserService.parseFile(validationContext, path);
        // performs semantic validator
        this.semanticValidator.validate(validationContext);
        // perform specific pojo validation
        this.specificValidator.validate(validationContext);
        if(!validationContext.getErrors().isEmpty()){
            throw new ValidationException(validationContext.getErrors());
        }
        logger.info("passed");
        // perform semantic validation for these specifics
    }
}
