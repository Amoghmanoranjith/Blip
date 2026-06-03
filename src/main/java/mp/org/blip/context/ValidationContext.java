package mp.org.blip.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.definition.TaskDefinition;
import mp.org.blip.error.ValidationError;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationContext {
    private JobDefinition jobDefinition;
    private List<ValidationError> errors;
    private Map<String, Integer> referenceCountMap;
    private Map<String, Integer> taskCountMap;
    private Map<String, TaskDefinition> taskMap;
}
