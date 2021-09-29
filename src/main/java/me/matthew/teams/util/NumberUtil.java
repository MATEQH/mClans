package me.matthew.teams.util;

public class NumberUtil {

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }

    public static boolean isInt(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }
}
