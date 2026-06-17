package mp.org.blip.repository;

import mp.org.blip.entity.CronJob;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CronJobRepository extends ReactiveCrudRepository<CronJob, Integer> {
}
