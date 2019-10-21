package by.danilov.cs.crypto.util;

import static by.danilov.cs.crypto.util.StringUtil.isLetter;
import static by.danilov.cs.crypto.util.StringUtil.isUpperCase;

public class CryptoUtil {

    private CryptoUtil() {

    }

    public static String getTransformedSymbol(String symbol,
                                              Integer offset,
                                              boolean encrypt) {
        if (!isLetter(symbol)) {
            return symbol;
        }

        symbol = symbol.toUpperCase();

        int characterCode = symbol.codePointAt(0);

        //if (isUpperCase(symbol)) {
        return transformUppercase(characterCode, offset, encrypt);
        //} else {
        //    return transformLowercase(characterCode, offset, encrypt);
        //}
    }

    public static Integer getOffset(String keyword, Integer index) {
        return (keyword.codePointAt(index % keyword.length()) - 97) % 26;
    }

    public static String transformUppercase(Integer characterCode, Integer offset, boolean encrypt) {
        if (encrypt) {
            return String.valueOf((char) (((characterCode + offset - 65) % 26) + 65));
        } else {
            return String.valueOf((char) (((characterCode - offset - 90) % 26) + 90));
        }
    }

    public static String transformLowercase(Integer characterCode, Integer offset, boolean encrypt) {
        if (encrypt) {
            return String.valueOf((char) (((characterCode + offset - 97) % 26) + 97));
        } else {
            return String.valueOf((char) (((characterCode - offset - 122) % 26) + 122));
        }
    }
}
