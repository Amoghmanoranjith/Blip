package mp.org.blip.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {
    private final String filePath;
    public FileException(String filePath, String message) {
        super(message);
        this.filePath = filePath;
    }
}
