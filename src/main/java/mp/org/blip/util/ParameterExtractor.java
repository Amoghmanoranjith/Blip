package mp.org.blip.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ParameterExtractor {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{([^}]*)}");
    public static Set<String> extract(String input) {
        Set<String> params = new HashSet<>();
        if (input == null || input.isBlank()) {
            return params;
        }
        Matcher matcher = PARAM_PATTERN.matcher(input);
        while (matcher.find()) {
            String expression = matcher.group(1);
            String outputName = expression.split("\\.")[0];

            params.add(outputName);
        }
        return params;
    }
}
