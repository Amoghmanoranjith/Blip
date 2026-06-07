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
    @Pattern(message = "{cron.schedule.invalid}",regexp = "^(\\*|\\?|\\b([0-5]?\\d)(-[0-5]?\\d)?(/[0-5]?\\d)?\\b)( (\\*|\\?|\\b([0-5]?\\d)(-[0-5]?\\d)?(/[0-5]?\\d)?\\b)){2}( (\\*|\\?|\\b([01]?\\d|2[0-3])(-([01]?\\d|2[0-3]))?(/([01]?\\d|2[0-3]))?\\b)){3}( (\\*|\\?|\\b([1-9]|[12]\\d|3[01])(-([1-9]|[12]\\d|3[01]))?(/([1-9]|[12]\\d|3[01]))?(L|W)?\\b)){4}( (\\*|\\?|\\b([1-9]|1[0-2])(-([1-9]|1[0-2]))?(/([1-9]|1[0-2]))?\\b|\\b(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\b)){5}( (\\*|\\?|\\b([1-7])(-([1-7]))?(/([1-7]))?(L|#\\d)?\\b|\\b(SUN|MON|TUE|WED|THU|FRI|SAT)\\b))$")
    private String schedule;
}
