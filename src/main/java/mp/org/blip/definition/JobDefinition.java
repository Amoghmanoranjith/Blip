package mp.org.blip.definition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// generic.job
public class JobDefinition {

    @NotBlank(message = "{generic.job.name.required}")
    @JsonProperty("job_name")
    private String jobName;

    @NotNull(message = "{generic.job.trigger.required}")
    @Valid
    private TriggerDefinition trigger;

    @NotEmpty(message = "{generic.job.tasks.required}")
    @Valid
    private List<TaskDefinition> tasks;
}
