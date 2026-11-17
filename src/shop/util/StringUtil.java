package shop.util;

public class StringUtil {

    public static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        if (value.isEmpty()) {
            return true;
        }
        return false;
    }
}
