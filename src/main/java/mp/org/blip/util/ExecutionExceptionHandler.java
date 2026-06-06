package mp.org.blip.util;

import mp.org.blip.exception.FileException;
import mp.org.blip.exception.ValidationException;
import mp.org.blip.exception.YamlParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {
    private final MessageSourceUtil messageSourceUtil;
    Logger logger = LoggerFactory.getLogger(ExecutionExceptionHandler.class.getName());

    public ExecutionExceptionHandler(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    @Override
    public int handleExecutionException(Exception e, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        // file exceptions
        logger.info(e.getClass().getName());
        switch (e) {
            case FileException fileException ->
                    commandLine.getErr().println(fileException.getFilePath() + " " + e.getMessage());
            case YamlParseException ex -> commandLine.getErr().println(
                    this.messageSourceUtil.getMessage(ex.getMessage())
            );
            case ValidationException ex -> {
                commandLine.getErr().println("\u001B[31m[ERROR] Schema validation failed:\u001B[0m");

                ex.getErrors().forEach(error -> {
                    // Formats output nicely. Example: "  - name: Field cannot be null"
                    commandLine.getErr().printf("  \u001B[33m-\u001B[0m %s: %s%n",
                            error.field(),
                            error.message()
                    );
                });
            }
            case null, default -> commandLine.getErr().println(e.getMessage() + "i got");
        }
        return 1;
    }
}
