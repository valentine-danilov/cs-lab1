package by.danilov.cs.crypto.algorithm.api;

import by.danilov.cs.enumeration.Language;

public interface MonoalphabeticCryptography extends Cryptography {

    default String encrypt(String source, Integer key, Language lang) {
        return getTransformedText(source, key, true);
    }

    default String decrypt(String source, Integer key, Language lang) {
        return getTransformedText(source, key, false);
    }

    String getTransformedText(String text, Integer keyword, boolean encrypt);

}
