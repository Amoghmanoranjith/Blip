package mp.org.blip.config;

import mp.org.blip.cli.BlipCommand;
import mp.org.blip.util.ExecutionExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@Configuration
public class PicoCliConfig {
    @Bean
    public CommandLine commandLine(
            BlipCommand blipCommand,
            ExecutionExceptionHandler executionExceptionHandler,
            ApplicationContext context
    ) {
        CommandLine commandLine = new CommandLine(blipCommand, new PicocliSpringFactory(context));
        commandLine.setExecutionExceptionHandler(executionExceptionHandler);
        return commandLine;
    }
}
