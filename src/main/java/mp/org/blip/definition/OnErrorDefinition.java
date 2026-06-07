package mp.org.blip.definition;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//generic.on_error
public class OnErrorDefinition {
    // if on_error is given then these

    @NotBlank(message = "{generic.on_error.action.required}")
    private String action;
//    @NotNull(message = "{generic.on_error.config.required}")
    private Object config;
}
