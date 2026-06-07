package mp.org.blip.service;

import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationError;
import org.springframework.stereotype.Component;

import java.util.*;

// give this a JobDefinition and empty task map, reference map and errors list
// we populate the last three and return
// it will validate according to Day1.txt
// each validation should not stop execution in case of failure
// we want to give the user each reason of why their yaml has semantic flaws
// ig we can store the semantic errors with fields and messages
// e.g tasks.<some-task-name> has duplicate name
@Component
public class SemanticValidationService {
    // requires task map
    // go through the map and check for counts more than 1
    private void validateDuplicateTaskIds(
            Set<ValidationError> errors,
            Map<String, Integer> taskMap) {

        taskMap.forEach((taskId, count) -> {
            if (count > 1) {
                errors.add(
                        new ValidationError(
                                "tasks." + taskId,
                                "Duplicate task id"
                        )
                );
            }
        });
    }

    // requires task map
    // go through the task ids and check for existance in map
    private void validateDependencyExistence(
            JobDefinition jobDefinition,
            Set<ValidationError> errors,
            Map<String, Integer> taskMap) {

        jobDefinition.getTasks().forEach(task -> {

            if (task.getDependencies() == null) {
                return;
            }

            task.getDependencies().forEach(dep -> {

                if (!taskMap.containsKey(dep)) {
                    errors.add(
                            new ValidationError(
                                    "tasks." + task.getId() + ".dependencies",
                                    "Dependency not found: " + dep
                            )
                    );
                }

            });
        });
    }

    // simple sweep check if task id is present in its own dependency
    private void validateSelfDependencies(
            JobDefinition jobDefinition,
            Set<ValidationError> errors) {

        jobDefinition.getTasks().forEach(task -> {

            if (task.getDependencies() == null) {
                return;
            }

            if (task.getDependencies().contains(task.getId())) {
                errors.add(
                        new ValidationError(
                                "tasks." + task.getId(),
                                "Task depends on itself"
                        )
                );
            }

        });
    }

    // for each task create a set and check for duplicate
    private void validateDuplicateDependencies(
            JobDefinition jobDefinition,
            Set<ValidationError> errors) {

        jobDefinition.getTasks().forEach(task -> {

            if (task.getDependencies() == null) {
                return;
            }

            Set<String> seen = new HashSet<>();

            task.getDependencies().forEach(dep -> {

                if (!seen.add(dep)) {
                    errors.add(
                            new ValidationError(
                                    "tasks." + task.getId() + ".dependencies",
                                    "Duplicate dependency: " + dep
                            )
                    );
                }

            });
        });
    }

    // output name has to be unique if present
    // use reference map
    private void validateOutputUniqueness(
            Set<ValidationError> errors,
            Map<String, Integer> referenceMap) {

        referenceMap.forEach((output, count) -> {

            if (count > 1) {
                errors.add(
                        new ValidationError(
                                "output." + output,
                                "Duplicate output name"
                        )
                );
            }

        });
    }


    // create a dag and check for cycles
    // it would be smarter to skip this part and return if any previous validations fail such as first and second
    // using the tasks create map<String, List<String>> graph
    // each value for a node is the list of its children
    // keep a priority queue count of ingress for each node
    // for each node with ingress of 0 meaning it is an independent node
    // each child node we reduce ingress by 1
    // then we look for the next independent node
    // at any point if the queue is not empty and we have not found an independent node then this means we have a cycle

    private void validateCycles(JobDefinition jobDefinition, Set<ValidationError> errors) {

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        jobDefinition.getTasks().forEach(task -> {
            graph.putIfAbsent(task.getId(), new ArrayList<>());
            indegree.putIfAbsent(task.getId(), 0);
        });

        jobDefinition.getTasks().forEach(task -> {
            if (task.getDependencies() == null) {
                return;
            }
            task.getDependencies().forEach(dep -> {
                graph.get(dep).add(task.getId());
                indegree.put(
                        task.getId(),
                        indegree.get(task.getId()) + 1
                );
            });
        });

        Queue<String> queue = new LinkedList<>();
        indegree.forEach((taskId, degree) -> {
            if (degree == 0) {
                queue.offer(taskId);
            }
        });

        int processed = 0;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            processed++;
            for (String child : graph.get(current)) {
                indegree.put(
                        child,
                        indegree.get(child) - 1
                );
                if (indegree.get(child) == 0) {
                    queue.offer(child);
                }
            }
        }

        if (processed != graph.size()) {
            errors.add(
                    new ValidationError(
                            "workflow",
                            "Cycle detected in task dependency graph"
                    )
            );
        }
    }

    public void validate(ValidationContext validationContext) {
        // task map
        // populate a map with key as task id and value as count
        // reference map
        // populate another map with key as output name and value as count
        Map<String, Integer> taskCountMap = validationContext.getTaskCountMap();
        Map<String, Integer> referenceCountMap = validationContext.getReferenceCountMap();
        Map<String, TaskDefinition> taskMap = validationContext.getTaskMap();
        Set<ValidationError> errors = validationContext.getErrors();
        JobDefinition jobDefinition = validationContext.getJobDefinition();

        validateDuplicateTaskIds(
                errors,
                taskCountMap
        );

        validateDependencyExistence(
                jobDefinition,
                errors,
                taskCountMap
        );

        validateSelfDependencies(
                jobDefinition,
                errors
        );

        validateDuplicateDependencies(
                jobDefinition,
                errors
        );

        validateOutputUniqueness(
                errors,
                referenceCountMap
        );

        boolean graphSafe =
                errors.stream().noneMatch(error ->
                        error.message().contains("Duplicate task id")
                                || error.message().contains("Dependency not found")
                );
        if (graphSafe) {
            validateCycles(
                    jobDefinition,
                    errors
            );
        }
        validationContext.setErrors(errors);
        validationContext.setTaskCountMap(taskCountMap);
        validationContext.setReferenceCountMap(referenceCountMap);
        validationContext.setTaskMap(taskMap);
    }
}

