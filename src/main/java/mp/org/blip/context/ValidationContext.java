package mp.org.blip.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.exception.ValidationError;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationContext {
    private JobDefinition jobDefinition;
    private Set<ValidationError> errors;
    private Map<String, Integer> referenceCountMap;
    private Map<String, Integer> taskCountMap;
    // put string get the task
    private Map<String, TaskDefinition> taskMap;

    //graph members
    // parent with list of children
    private Map<String, List<String>> graph;
    // list of nodes that are independent to run
    private List<String> startingNodes;

    // map output name to task name
    private Map<String, String> outputTaskMapping;
    private Map<String, Set<String>> taskDependenciesMapping;
    public void addError(ValidationError validationError) {
        errors.add(validationError);
    }
    public void mergeDependencies(String id, Set<String> dependencies) {
        this.taskDependenciesMapping.computeIfAbsent(id, k -> new HashSet<>()).addAll(dependencies);
    }
    public Set<String> convertOutputToDependency(Set<String> outputs){
        return outputs.stream().map(s -> outputTaskMapping.get(s)).collect(Collectors.toSet());
    }
    public void mergeDependency(String id, String dependency){
        this.taskDependenciesMapping.get(id).add(dependency);
    }
}
