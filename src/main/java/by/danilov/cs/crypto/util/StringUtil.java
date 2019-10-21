package by.danilov.cs.crypto.util;

public class StringUtil {

    private StringUtil() {

    }

    public static Boolean isUpperCase(String symbol) {
        return Character.isUpperCase(symbol.codePointAt(0));
    }

    public static Boolean isLetter(String symbol) {
        return symbol.matches("[a-zA-Z]");
    }

    public static String reduceNonLetter(String str) {
        return str.replaceAll("[ .,?!\\-_]+", "");
    }
}
