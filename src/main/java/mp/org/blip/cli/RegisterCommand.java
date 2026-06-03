package mp.org.blip.cli;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(
        name = "register",
        description = "Register a workflow YAML file"
)
public class RegisterCommand implements Runnable {

    @Parameters(
            index = "0",
            description = "Path to YAML file"
    )
    private String yamlPath;

    @Override
    public void run() {

        System.out.println("Registering workflow:");
        System.out.println(yamlPath);

        // call parser
        // call validator
        // save to DB
    }
}
