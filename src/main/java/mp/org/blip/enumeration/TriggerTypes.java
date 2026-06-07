package mp.org.blip.enumeration;

public enum TriggerTypes {
    CRON;
    public static TriggerTypes from(String value) {
        return TriggerTypes.valueOf(value.toUpperCase());
    }

    public static boolean isValid(String value) {
        try {
            TriggerTypes.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
