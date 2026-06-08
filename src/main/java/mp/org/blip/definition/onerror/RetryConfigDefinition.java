package mp.org.blip.definition.onerror;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryConfigDefinition {
    @JsonProperty("max_retries")
    @NotNull(message = "{retry.max_retries.required}")
    private Integer maxRetries;
    @NotNull(message = "{retry.delay.required}")
    @Min(value = 1, message = "{retry.delay.minimum}")
    private Integer delay; // this is in milli seconds
}
