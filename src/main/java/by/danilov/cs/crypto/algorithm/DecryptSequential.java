package by.danilov.cs.crypto.algorithm;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

public class DecryptSequential {

    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static Integer getPositionInAlphabet(Character symbol) {
        return alphabet.indexOf(symbol);
    }

    /*public String decrypt(String cryptoText, Integer keywordLength) {




    }*/

    public static void main(String[] args) {
        System.out.println(getPositionInAlphabet('W'));
    }

}
