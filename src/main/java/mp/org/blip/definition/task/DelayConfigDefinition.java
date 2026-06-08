package mp.org.blip.definition.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelayConfigDefinition {

    @NotNull(message = "{delay.value.required}")
    private Integer delay;
}
