package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RegisterService {
    private final SemanticValidationService semanticValidationService;
    private final FileParserService fileParserService;
    private final SpecificValidationService specificValidationService;
    private final DAGValidationService dagValidationService;
    Logger logger = LoggerFactory.getLogger(RegisterService.class);
    public RegisterService(SemanticValidationService semanticValidationService, FileParserService fileParserService, SpecificValidationService specificValidationService, DAGValidationService dagValidationService) {
        this.semanticValidationService = semanticValidationService;
        this.fileParserService = fileParserService;
        this.specificValidationService = specificValidationService;
        this.dagValidationService = dagValidationService;
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

        // check for general structural errors and get job definition pojo
        //*******************************************************
        this.fileParserService.parseFile(validationContext, path);
//        if(!validationContext.getErrors().isEmpty()){
//            throw new ValidationException(validationContext.getErrors());
//        }
        //*******************************************************

        this.populateValidationContext(validationContext);

        // performs semantic validator
        //*******************************************************
        this.semanticValidationService.validate(validationContext);
//        if(!validationContext.getErrors().isEmpty()){
//            throw new ValidationException(validationContext.getErrors());
//        }
        //*******************************************************

        // perform specific pojo validation
        //*******************************************************
        this.specificValidationService.validate(validationContext);
//        if(!validationContext.getErrors().isEmpty()){
//            throw new ValidationException(validationContext.getErrors());
//        }
        //*******************************************************

        // perform dag validation
        this.dagValidationService.validate(validationContext);
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
            if(task.getDependencies() != null && !task.getDependencies().isEmpty())
                taskDependenciesMapping.put(task.getId(), new HashSet<>(task.getDependencies()));
            else
                taskDependenciesMapping.put(task.getId(), new HashSet<>());
        });
        validationContext.setTaskCountMap(taskCountMap);
        validationContext.setReferenceCountMap(referenceCountMap);
        validationContext.setTaskMap(taskMap);
        validationContext.setOutputTaskMapping(outputTaskMapping);
        validationContext.setTaskDependenciesMapping(taskDependenciesMapping);
    }
}
