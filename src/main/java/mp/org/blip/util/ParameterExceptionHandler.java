package mp.org.blip.util;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class ParameterExceptionHandler implements CommandLine.IParameterExceptionHandler{
    @Override
    public int handleParseException(CommandLine.ParameterException e, String[] strings) throws Exception {
        // implement wrong parameter messages here
        return 2;
    }
}
