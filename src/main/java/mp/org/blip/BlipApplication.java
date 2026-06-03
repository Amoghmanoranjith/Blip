package mp.org.blip;

import mp.org.blip.cli.BlipCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class BlipApplication implements CommandLineRunner {

    private final BlipCommand blipCommand;

    public BlipApplication(BlipCommand blipCommand) {
        this.blipCommand = blipCommand;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlipApplication.class, args);
    }

    @Override
    public void run(String... args) {

        int exitCode = new CommandLine(blipCommand)
                .execute(args);

        System.exit(exitCode);
    }
}