package mp.org.blip.definition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// generic.trigger
public class TriggerDefinition {
    @NotBlank(message = "{generic.trigger.type.required}")
    private String type;
    // make sure this is not empty
    @NotNull(message = "{generic.trigger.config.required}")
    private Object config;
}
