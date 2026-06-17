package mp.org.blip.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mp.org.blip.enumeration.JobRunStatusTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("job_run")
public class JobRun {
    @Id
    private Integer jobRunId;

    @Column("job_id")
    private Integer jobId;

    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;

    private JobRunStatusTypes status;
    private Integer workerId;
}
