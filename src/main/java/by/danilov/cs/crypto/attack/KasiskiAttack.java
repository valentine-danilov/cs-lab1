package by.danilov.cs.crypto.attack;

import by.danilov.cs.crypto.util.MathUtil;
import by.danilov.cs.crypto.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class KasiskiAttack {

    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 30;

    public Integer perform(String cryptoText) {
        return suggestKeywordLength(cryptoText);
    }

    private Integer suggestKeywordLength(String cryptoText) {

        cryptoText = StringUtil.reduceNonLetter(cryptoText);

        Map<String, List<Integer>> substringTable = new LinkedHashMap<>();

        for (int i = MIN_LENGTH; i < MAX_LENGTH; i++) {
            substringTable.putAll(getRepeatableSubstrings(cryptoText, i));
        }

        var distanceTable = getDistances(substringTable);
        var factors = getNODs(distanceTable);
        var factorsAsList = getFactorsAsSequentiallySortedList(factors);

        if (factorsAsList.isEmpty()) {
            return MIN_LENGTH;
        }

        return factorsAsList.get(0);

    }


    private List<Integer> getDistancesBetweenSubstrings(String cryptoText, Integer substringLength) {

        List<Integer> distances = new ArrayList<>();

        for (int i = 0; i < cryptoText.length() - substringLength + 1; i++) {
            int endIndex1 = i + substringLength;
            String substring1 = cryptoText.substring(i, endIndex1);
            for (int j = i + 1; j < cryptoText.length() - substringLength + 1; j++) {
                int endIndex2 = j + substringLength;
                String substring2 = cryptoText.substring(j, endIndex2);
                if (Objects.equals(substring1, substring2)) {
                    distances.add(j - i);
                }
            }
        }

        return distances;
    }

    private List<Integer> getDistancesBetweenSubstrings(String cryptoText) {

        List<Integer> distances = new ArrayList<>();

        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            distances.addAll(getDistancesBetweenSubstrings(cryptoText, i));
        }

        return distances;
    }


    private List<Integer> getAllDistancesForSubstring(String cryptoText,
                                                      String substring) {

        int substringLength = substring.length();
        List<Integer> distances = new ArrayList<>();
        int currentIndex = cryptoText.indexOf(substring) + substringLength;
        int previousIndex = cryptoText.indexOf(substring);

        while (currentIndex != -1) {
            currentIndex = cryptoText.indexOf(substring, currentIndex);

            if (currentIndex != -1) {
                int length = currentIndex - previousIndex;
                distances.add(length);
                previousIndex = currentIndex;
                currentIndex += substringLength;
            }
        }

        return distances;

    }

    private Map<String, List<Integer>> getRepeatableSubstrings(String cryptoText, Integer length) {

/*        Set<String> repeatableSubstrings = new HashSet<>();

        for (int j = 0; j < cryptoText.length() - length; j++) {

            int end = j + length;
            String substring = cryptoText.substring(j, end);

            int currentIndex = cryptoText.indexOf(substring) + substring.length();

            currentIndex = cryptoText.indexOf(substring, currentIndex);
            currentIndex = cryptoText.indexOf(substring, currentIndex);

            if (currentIndex != -1) {
                repeatableSubstrings.add(substring);
            }
        }*/

        Map<String, List<Integer>> substringTable = new LinkedHashMap<>();

        for (int i = 0; i < cryptoText.length() - length + 1; i++) {
            String nextSubstring = cryptoText.substring(i, i + length);
            if (!substringTable.containsKey(nextSubstring)) {
                List<Integer> indices = new LinkedList<>();
                indices.add(i);
                substringTable.put(nextSubstring, indices);
            } else {
                substringTable.get(nextSubstring).add(i);
            }
        }

        return filterSubstrings(substringTable);
    }

    private Map<String, List<Integer>> filterSubstrings(Map<String, List<Integer>> substringTable) {
        return substringTable.entrySet().stream()
                .filter(list -> list.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private Map<String, List<Integer>> getDistances(Map<String, List<Integer>> substringTable) {
        return substringTable.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getDistances(entry.getValue())
                ));
    }

    private List<Integer> getDistances(List<Integer> indices) {

        List<Integer> distances = new ArrayList<>();

        for (int i = 0; i < indices.size() - 1; i++) {
            distances.add(indices.get(i + 1) - indices.get(i));
        }

        return distances;
    }

    private Integer getGCD(Map<String, List<Integer>> distanceTable) {
        List<Integer> distances = new ArrayList<>();
        distanceTable.forEach((key, value) -> distances.addAll(value));
        List<Integer> filteredDistances = distances.stream()
                .filter(distance -> distance <= MAX_LENGTH)
                .filter(distance -> distance >= MIN_LENGTH)
                .collect(Collectors.toList());
        return MathUtil.getGCD(filteredDistances);
    }

    private List<Integer> getNODs(Map<String, List<Integer>> distancesTable) {
        List<Integer> factors = new ArrayList<>();
        distancesTable.forEach((key, value) -> factors.addAll(getNODs(value)));
        return factors;
    }

    private List<Integer> getNODs(List<Integer> distances) {

        List<Integer> factors = new ArrayList<>();

        for (int i = 0; i < distances.size(); i++) {
            for (int j = i + 1; j < distances.size(); j++) {
                factors.add(MathUtil.getGCD(distances.get(i), distances.get(j)));
            }
        }

        /*for (Integer distance : distances) {
            for (int i = 1; i <= (int) (Math.sqrt(distance) + 1); i++) {
                if (distance % i == 0) {
                    factors.add(i);
                    factors.add(distance / i);
                }
            }
        }*/

        return factors;
    }

    private List<Integer> getFactorsAsSequentiallySortedList(List<Integer> factors) {

        Map<Integer, Integer> factorSequenceTable = new TreeMap<>();

        for (Integer factor : factors) {
            if (factorSequenceTable.containsKey(factor)) {
                Integer value = factorSequenceTable.get(factor);
                factorSequenceTable.put(factor, ++value);
            } else {
                factorSequenceTable.put(factor, 1);
            }
        }

        return factorSequenceTable.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(Map.Entry::getKey)
                .filter(factor -> factor <= MAX_LENGTH)
                .filter(factor -> factor >= MIN_LENGTH)
                .collect(Collectors.toList());

    }


}
