package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationError;
import mp.org.blip.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegisterService {
    private final SemanticValidationService semanticValidationService;
    private final FileParserService fileParserService;
    private final SpecificValidationService specificValidationService;
    private final GraphGeneratorService graphGeneratorService;
    Logger logger = LoggerFactory.getLogger(RegisterService.class);
    public RegisterService(SemanticValidationService semanticValidationService, FileParserService fileParserService, SpecificValidationService specificValidationService, GraphGeneratorService graphGeneratorService) {
        this.semanticValidationService = semanticValidationService;
        this.fileParserService = fileParserService;
        this.specificValidationService = specificValidationService;
        this.graphGeneratorService = graphGeneratorService;
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
                .graph(null)
                .startingNodes(null)
                .outputTaskMapping(new HashMap<>())
                .taskDependenciesMapping(new HashMap<>())
                .build();
        this.populateValidationContext(validationContext);

        this.fileParserService.parseFile(validationContext, path);
        // performs semantic validator
        this.semanticValidationService.validate(validationContext);
        // create the graph and put in validationContext
        this.graphGeneratorService.generate(validationContext);
        // then create pojos in that order
        // perform specific pojo validation
        this.specificValidationService.validate(validationContext);
        if(!validationContext.getErrors().isEmpty()){
            throw new ValidationException(validationContext.getErrors());
        }
        logger.info("passed");
        // perform semantic validation for these specifics
    }

    private void populateValidationContext(ValidationContext validationContext){
        Map<String, Integer> taskCountMap = new HashMap<>();
        Map<String, Integer> referenceCountMap = new HashMap<>();
        Map<String, TaskDefinition> taskMap = new HashMap<>();
        Map<String, String> outputTaskMapping = new HashMap<>();
        Map<String, Set<String>> taskDependenciesMapping = new HashMap<>();
        JobDefinition jobDefinition = validationContext.getJobDefinition();

        jobDefinition.getTasks().forEach(task -> {
            taskCountMap.merge(
                    task.getId(),
                    1,
                    Integer::sum
            );
            if (task.getOutput() != null) {

                referenceCountMap.merge(
                        task.getOutput(),
                        1,
                        Integer::sum
                );
            }
            taskMap.put(task.getId(), task);

            outputTaskMapping.put(task.getOutput(), task.getId());

            taskDependenciesMapping.put(task.getId(), new HashSet<>(task.getDependencies()));
        });
        validationContext.setTaskCountMap(taskCountMap);
        validationContext.setReferenceCountMap(referenceCountMap);
        validationContext.setTaskMap(taskMap);
        validationContext.setOutputTaskMapping(outputTaskMapping);
        validationContext.setTaskDependenciesMapping(taskDependenciesMapping);
    }
}
