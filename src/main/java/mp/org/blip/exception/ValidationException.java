package mp.org.blip.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class ValidationException extends RuntimeException {
    private final Set<ValidationError> errors;
    public ValidationException(String message, Set<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }
}
