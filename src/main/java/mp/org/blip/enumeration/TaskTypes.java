package mp.org.blip.enumeration;


public enum TaskTypes {
    HTTP,
    DELAY;
    public static TaskTypes from(String value) {
        return TaskTypes.valueOf(value.toUpperCase());
    }

    public static boolean isValid(String value) {
        try {
            TaskTypes.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
