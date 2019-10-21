package by.danilov.cs.crypto.algorithm.api;

import by.danilov.cs.enumeration.Language;

public interface PolyalphabeticCryptography {

    default String encrypt(String source, String key, Language lang) {
        return getTransformedText(source, key, true);
    }

    default String decrypt(String source, String key, Language lang) {
        return getTransformedText(source, key, false);
    }

    String getTransformedText(String text, String keyword, boolean encrypt);
}
