package by.danilov.cs.crypto.algorithm.vigenere;

import by.danilov.cs.crypto.algorithm.api.PolyalphabeticCryptography;
import by.danilov.cs.crypto.util.CryptoUtil;
import by.danilov.cs.crypto.util.StringUtil;
import by.danilov.cs.enumeration.Language;

public class VigenereCryptography implements PolyalphabeticCryptography {

    @Override
    public String getTransformedText(String text,
                                     String keyword,
                                     boolean encrypt) {

        StringBuilder decryptedSource = new StringBuilder();

        keyword = keyword.toLowerCase();
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            String symbol = "" + text.charAt(i);

            if (!StringUtil.isLetter(symbol)) {
                decryptedSource.append(symbol);
                continue;
            }
            int offset = CryptoUtil.getOffset(keyword, index);
            decryptedSource.append(CryptoUtil.getTransformedSymbol(symbol, offset, encrypt));
            index++;
        }

        return decryptedSource.toString();
    }

    public static void main(String[] args) {
        VigenereCryptography vigenereCryptography = new VigenereCryptography();
        String a = "asd asd1asd";
        String key = "ewr";
        String encr = vigenereCryptography.encrypt(a, key, Language.EN);
        System.out.println(encr);
        System.out.println(vigenereCryptography.decrypt(encr, key, Language.EN));
    }
}
