package arkadisahakyan.authenticationwithspring;

public class Utilities {
    public static String nullableString(String str) {
        return "".equals(str) ? null : str;
    }

    public static String notNullString(String str) {
        return str == null ? "" : str;
    }

    public static Long parseLong(String str) {
        Long number;
        try {
            number = Long.parseLong(str);
        } catch (NumberFormatException e) {
            number = null;
        }
        return number;
    }
}
