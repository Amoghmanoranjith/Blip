package mp.org.blip.definition.trigger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CronConfigDefinition {
    @NotBlank(message = "{cron.schedule.required}")
    @Pattern(message = "{cron.schedule.invalid}", regexp = "(\\*(/\\d+)?|\\?|\\b([0-5]?\\d)(-[0-5]?\\d)?(/[0-5]?\\d)?\\b)")
    private String schedule;
}
