package by.danilov.cs.crypto.util;

import java.util.List;
import java.util.Set;

public class MathUtil {

    private MathUtil() {

    }

    public static Integer getGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return getGCD(b % a, a);
    }

    public static Integer getGCD(List<Integer> integers) {

        if (integers.isEmpty()) {
            throw new IllegalArgumentException("List of passed integers can not be empty");
        }

        int result = integers.get(0);

        for (Integer number : integers) {
            result = getGCD(number, result);
        }

        return result;
    }


}
