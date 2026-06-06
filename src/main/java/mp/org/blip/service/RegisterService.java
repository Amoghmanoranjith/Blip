package mp.org.blip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.validator.SemanticValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

@Service
public class RegisterService {
    private final SemanticValidator semanticValidator;
    private final ObjectMapper objectMapper;
    private final FileParserService fileParserService;
    Logger logger = LoggerFactory.getLogger(RegisterService.class);
    public RegisterService(SemanticValidator semanticValidator, ObjectMapper objectMapper, FileParserService fileParserService) {
        this.semanticValidator = semanticValidator;
        this.objectMapper = objectMapper;
        this.fileParserService = fileParserService;
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

        logger.info("passed");
        // perform semantic validation for these specifics
    }
}
