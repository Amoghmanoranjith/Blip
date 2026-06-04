package mp.org.blip.exception;

public record ValidationError(
        String field,
        String message
) {}