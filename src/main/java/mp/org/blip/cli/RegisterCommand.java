package mp.org.blip.cli;

import mp.org.blip.service.RegisterService;
import org.springframework.stereotype.Component;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.io.IOException;

@Component
@Command(
        name = "register",
        description = "Register a job YAML file"
)
public class RegisterCommand implements Runnable {

    private final RegisterService registerService;

    public RegisterCommand(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Parameters(
            index = "0",
            description = "Path to YAML file"
    )
    private String yamlPath;

    @Override
    public void run() {
        this.registerService.register(this.yamlPath);
    }
}
