package mp.org.blip.definition;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
//generic.task
public class TaskDefinition {
    // required
    // has to be unique
    @NotBlank(message = "{generic.tasks.id.required}")
    private String id;

    // required
    // enum
    @NotBlank(message = "{generic.tasks.type.required}")
    private String type;

    // required
    @NotNull(message = "{generic.tasks.type.required}")
    private Object config;

    // not required but if given
    // avoid duplicate dependency
    // avoid non existing dependency
    private List<String> dependencies;
    private String output;
    @Valid
    @JsonProperty("on_error")
    private OnErrorDefinition onError;
}
