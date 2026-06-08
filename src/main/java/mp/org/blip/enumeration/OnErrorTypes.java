package mp.org.blip.enumeration;

public enum OnErrorTypes {
    FAIL,
    RETRY,
    CONTINUE,
    FALLBACK;

    public static OnErrorTypes from(String value) {
        return OnErrorTypes.valueOf(value.toUpperCase());
    }

    public static boolean isValid(String value) {
        try {
            OnErrorTypes.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
