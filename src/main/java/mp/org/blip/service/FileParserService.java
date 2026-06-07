package mp.org.blip.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.exception.FileException;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.exception.YamlParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileParserService {
    private final ObjectMapper objectMapper;
    private final Validator validator;
    Logger logger = LoggerFactory.getLogger(FileParserService.class);

    public FileParserService(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    // validate file path and return
    private Path getFilePath(String path) {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new FileException(path, "File not found");
        }
        if (!Files.isRegularFile(filePath)) {
            throw new FileException(path, "Path is not a file");
        }
        if (!Files.isReadable(filePath)) {
            throw new FileException(path, "File is not readable");
        }
        return filePath;
    }

    // this is where actual parsing happens
    public void parseFile(ValidationContext validationContext, String path) {
        Path filePath = getFilePath(path);
        try {
            JobDefinition jobDefinition = objectMapper.readValue(
                    filePath.toFile(),
                    JobDefinition.class
            );
            Set<ValidationError> errorSet = validationContext.getErrors();
            validationContext.setJobDefinition(jobDefinition);
            Set<ConstraintViolation<JobDefinition>> violations = validator.validate(jobDefinition);

            if (!violations.isEmpty()) {
                violations.stream()
                        .forEach(v -> errorSet.add(new ValidationError(v.getPropertyPath().toString(), v.getMessage())));
            }
            validationContext.setErrors(errorSet);
        } catch (UnrecognizedPropertyException e) { // this is schema
            throw new YamlParseException(e.getLocation().getLineNr(), e.getLocation().getColumnNr(), "yaml.field.unrecognised");
        } catch (JacksonYAMLParseException e) { // this is syntax
            throw new YamlParseException(e.getLocation().getLineNr(), e.getLocation().getColumnNr(), "yaml.syntax.invalid");
        } catch (MismatchedInputException e) { // this is schem
            throw new YamlParseException(e.getLocation().getLineNr(), e.getLocation().getColumnNr(), "yaml.field.type_mismatch");
        } catch (Exception e) { // default
            throw new FileException(
                    path,
                    "Unable to read file"
            );
        }
    }
}
