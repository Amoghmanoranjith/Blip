package mp.org.blip.exception;

import lombok.Getter;

// this gives property name
// location and holds the message code which we will convert to message in handler

@Getter
public class YamlParseException extends RuntimeException {
    private final Integer lineNumber;
    private final Integer columnNumber;
    public YamlParseException(Integer lineNumber, Integer columnNumber, String message) {
        super(message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
}
