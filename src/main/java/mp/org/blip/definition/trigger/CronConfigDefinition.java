package mp.org.blip.definition.trigger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.annotation.ValidCron;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CronConfigDefinition {
    @NotBlank(message = "{cron.schedule.required}")
    @ValidCron(message = "{cron.schedule.invalid_expression}")
    private String schedule;
}
