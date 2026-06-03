package mp.org.blip.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(
        name = "blip",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Workflow scheduling engine",
        subcommands = {
                RegisterCommand.class
        }
)
public class BlipCommand {
}