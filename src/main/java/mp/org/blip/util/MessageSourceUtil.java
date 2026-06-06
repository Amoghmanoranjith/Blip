package mp.org.blip.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtil {
    private final MessageSource messageSource;

    // Spring injects the bean into the constructor, and we set the static field
    public MessageSourceUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object... args) {
        if (messageSource == null) {
            // Fallback in case context isn't loaded yet (e.g., unit tests)
            return "Validation failed for: " + (args.length > 0 ? args[0] : "");
        }
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
