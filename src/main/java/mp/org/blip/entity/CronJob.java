package mp.org.blip.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("cron_job")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronJob {
    @Id
    private Integer jobId;
    private String jobName;
    private String fileLocation;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDateTime scheduledTime;
}
