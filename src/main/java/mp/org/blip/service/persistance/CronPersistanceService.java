package mp.org.blip.service.persistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import mp.org.blip.context.ValidationContext;
import mp.org.blip.definition.trigger.CronConfigDefinition;
import mp.org.blip.entity.CronJob;
import mp.org.blip.repository.CronJobRepository;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CronPersistanceService {

    private final CronJobRepository repository;
    private final ObjectMapper objectMapper;
    private final FilePersistanceService filePersistanceService;

    public CronPersistanceService(CronJobRepository jobRepository, ObjectMapper objectMapper, FilePersistanceService filePersistanceService) {
        this.repository = jobRepository;
        this.objectMapper = objectMapper;
        this.filePersistanceService = filePersistanceService;
    }

    public Mono<Void> save(ValidationContext validationContext) {
        // save file
        // save in db
        // if error while storing in db then delete file
        return this.filePersistanceService.save(validationContext.getJobDefinition())
                .flatMap(path -> this.save(validationContext, path.getFileName().toString())
                        .onErrorResume(e -> this.filePersistanceService.delete(path)
                                                        .then(Mono.error(e)))).then();
    }

    public Mono<CronJob> save(ValidationContext validationContext, String fileName) {
        CronConfigDefinition config = this.objectMapper.convertValue(validationContext.getJobDefinition().getTrigger().getConfig(), CronConfigDefinition.class);
        CronExpression cronExpression = CronExpression.parse(config.getSchedule());
        LocalDateTime nextExecution = cronExpression.next(LocalDateTime.now());
        CronJob job = CronJob.builder()
                .jobName(validationContext.getJobDefinition().getJobName())
                .fileLocation(fileName.split("\\.")[0])
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .scheduledTime(nextExecution) // compute and store
                .build();
        return this.repository.save(job);
    }
}
