package mp.org.blip.service.persistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import mp.org.blip.definition.JobDefinition;
import mp.org.blip.entity.JobFile;
import mp.org.blip.exception.FileException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class FilePersistanceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Path saveBlocking(JobDefinition jobDefinition) {
        String fileName = UUID.randomUUID()
                .toString()
                .replace("-", "");
        // convert this to binary
        JobFile jobFile = JobFile.builder().jobDefinition(jobDefinition).build();
        // store under <User-Name>/blip/filename.blip
        Path blipDir = Paths.get(
                System.getProperty("user.home"),
                "blip"
        );
        try {
            Files.createDirectories(blipDir);
            byte[] payload = objectMapper.writeValueAsBytes(jobFile);
            Path filePath = blipDir.resolve(
                    fileName + ".blip"
            );
            Files.write(
                    filePath,
                    payload,
                    StandardOpenOption.CREATE_NEW
            );
            return filePath;
        } catch (IOException e) {
            throw new FileException(fileName, "Error while saving file");
        }
    }

    public Mono<Path> save(JobDefinition jobDefinition) {
        return Mono
                .fromCallable(() -> this.saveBlocking(jobDefinition))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public void deleteBlocking(Path filePath) {
        try {
            System.out.println(filePath.getFileName().toString() + " deleting ");
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // if possible dont show
            throw new FileException(filePath.getFileName().toString(), "Error while deleting file");
        }
    }

    public Mono<Void> delete(Path filePath) {
        // deletes file async
        // if error occurs then throw file exception
        // how do i do this reactively
        return Mono.fromRunnable(() -> {
            this.deleteBlocking(filePath);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
