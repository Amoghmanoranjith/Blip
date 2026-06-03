package mp.org.blip.error;

public record ValidationError(
        String field,
        String message
) {}