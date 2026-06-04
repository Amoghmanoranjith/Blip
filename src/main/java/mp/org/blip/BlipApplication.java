package mp.org.blip;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class BlipApplication implements CommandLineRunner {

    private final CommandLine commandLine;

    public BlipApplication(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlipApplication.class, args);
    }

    @Override
    public void run(String... args) {
        int exitCode = this.commandLine.execute(args);
        System.exit(exitCode);
    }
}