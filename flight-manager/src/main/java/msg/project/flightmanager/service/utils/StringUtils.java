package msg.project.flightmanager.service.utils;

public class StringUtils {

    public static boolean isAsciiPrintable(String string) {
        if (string == null) {
            return false;
        }

        for (int i = 0; i < string.length(); ++i) {
            if (!isAsciiPrintable(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAsciiPrintable(char ch) {
        return ch >= 32 && ch < 127;
    }
}
