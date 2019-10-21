package by.danilov.cs.crypto.algorithm.caesar;

import by.danilov.cs.crypto.algorithm.api.MonoalphabeticCryptography;
import by.danilov.cs.crypto.util.CryptoUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Getter
@Setter
public class CaesarCryptography implements MonoalphabeticCryptography {

    private static final Integer DEFAULT_KEY = 4;

    public CaesarCryptography() {
    }

    public String getTransformedText(String text, Integer key, boolean encrypt) {
        return Arrays.stream(text.split(""))
                .map(symbol -> CryptoUtil.getTransformedSymbol(symbol, isNull(key) ? DEFAULT_KEY : key, encrypt))
                .collect(Collectors.joining());
    }
}
