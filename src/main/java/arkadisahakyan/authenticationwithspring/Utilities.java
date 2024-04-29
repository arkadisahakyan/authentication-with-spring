package arkadisahakyan.authenticationwithspring;

public class Utilities {
    public static String nullableString(String str) {
        return "".equals(str) ? null : str;
    }
}
