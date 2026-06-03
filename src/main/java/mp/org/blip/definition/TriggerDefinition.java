package mp.org.blip.definition;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.enumeration.TriggerType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// generic.trigger
public class TriggerDefinition {
    @NotBlank(message = "{generic.trigger.type.required}")
    private String type;
    // make sure this is not empty
    @NotBlank(message = "{generic.trigger.config.required}")
    private Object config;
}
