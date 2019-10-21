package by.danilov.cs.crypto.attack;

import by.danilov.cs.crypto.algorithm.vigenere.VigenereCryptography;
import by.danilov.cs.crypto.util.StringUtil;
import by.danilov.cs.data.KeyValuePair;
import by.danilov.cs.dataprovider.JsonArgumentsProvider;
import by.danilov.cs.enumeration.Language;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.*;

public class KasiskiAttackTest {

    private KasiskiAttack kasiskiAttack = new KasiskiAttack();

    private VigenereCryptography vigenereCryptography = new VigenereCryptography();

    private static List<KeyValuePair> results = new ArrayList<>();

    @BeforeAll
    public static void beforeClass() throws FileNotFoundException {
        System.setOut(new PrintStream(new File("src\\test\\resources\\result.txt")));
    }

    @ParameterizedTest
    @ArgumentsSource(JsonArgumentsProvider.class)
    public void testKasiskiAttack(String text, String keyword) {
        String cryptoText = vigenereCryptography.encrypt(text, keyword, Language.EN);
        Integer expected = keyword.length();
        Integer actual = kasiskiAttack.perform(cryptoText);

        System.out.println(MessageFormat.format("{0}    {1}    {2}", StringUtil.reduceNonLetter(text).length(), expected, actual));
        /*results.add(new KeyValuePair(expected, actual));
        System.out.println(MessageFormat.format("Text length: {0}", text.length()));
        System.out.println(MessageFormat.format("Keyword: {0}", keyword));
        System.out.println(MessageFormat.format("Expected keyword length: {0}", expected));
        System.out.println(MessageFormat.format("Actual keyword length: {0}", actual));
        System.out.println();*/
    }

    @Test
    @Ignore
    public void name() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("src\\test\\resources\\testData.txt"))) {
            String initialText = scanner.useDelimiter("\\Z").next();
            String keyword = "mother";
            System.out.println("Real keyword length: " + keyword.length());
            String cryptoText = vigenereCryptography.encrypt(initialText, keyword, Language.EN);
        }
    }
}