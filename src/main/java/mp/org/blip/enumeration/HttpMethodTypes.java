package mp.org.blip.enumeration;

import jakarta.validation.ValidationException;
import mp.org.blip.util.MessageSourceUtil;

public enum HttpMethodTypes {
    GET,
    PUT,
    POST,
    DELETE;

    public static HttpMethodTypes from(String value) {
            return mp.org.blip.enumeration.HttpMethodTypes.valueOf(value.toUpperCase());
    }

    public static boolean isValid(String value) {
        try {
            HttpMethodTypes.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
