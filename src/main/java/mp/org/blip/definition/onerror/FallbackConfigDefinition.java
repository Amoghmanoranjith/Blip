package mp.org.blip.definition.onerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FallbackConfigDefinition {
    @JsonProperty("fallback")
    @NotBlank(message = "{fallback.fallback_task.required}")
    private String fallbackTask;
}
