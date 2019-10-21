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

    /**
     * Finds length of keyword, that was used to encrypt  text
     * @param cryptoText - encrypted text
     * @return - keyword length
     */
    private Integer suggestKeywordLength(String cryptoText) {

        cryptoText = StringUtil.reduceNonLetter(cryptoText);

        Map<String, List<Integer>> substringTable = new LinkedHashMap<>();

        for (int i = MIN_LENGTH; i < MAX_LENGTH; i++) {
            substringTable.putAll(getRepeatableSubstrings(cryptoText, i));
        }

        var distanceTable = getDistances(substringTable);
        var nods = getNODs(distanceTable);
        var nodsAsList = getNODsAsSequentiallySortedList(nods);

        if (nodsAsList.isEmpty()) {
            return MIN_LENGTH;
        }

        return nodsAsList.get(0);

    }

    /**
     * Find all substrings that repeats more than one time
     * @param cryptoText - source encrypted text
     * @param length - substring length
     * @return map: <substring, list of indices of substring occurrences>
     */
    private Map<String, List<Integer>> getRepeatableSubstrings(String cryptoText, Integer length) {

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

    /**
     * Filter by number of substring occurrences
     * @param substringTable - table to filter
     * @return - filtered table
     */
    private Map<String, List<Integer>> filterSubstrings(Map<String, List<Integer>> substringTable) {
        return substringTable.entrySet().stream()
                .filter(list -> list.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get map <substring, list of distances between adjacent substrings in text>
     * @param substringTable - table: <string, list of indices>
     * @return map <substring, list of distances between adjacent substrings in text>
     */
    private Map<String, List<Integer>> getDistances(Map<String, List<Integer>> substringTable) {
        return substringTable.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getDistances(entry.getValue())
                ));
    }

    /**
     * Transform list of indices into list of distances
     * @param indices - list of indices
     * @return - list of distances
     */
    private List<Integer> getDistances(List<Integer> indices) {

        List<Integer> distances = new ArrayList<>();

        for (int i = 0; i < indices.size() - 1; i++) {
            distances.add(indices.get(i + 1) - indices.get(i));
        }

        return distances;
    }

    /**
     * Get list of all distances NOD
     * @param distancesTable - initial table: <substring, distances>
     * @return - list of NODs
     */
    private List<Integer> getNODs(Map<String, List<Integer>> distancesTable) {
        List<Integer> factors = new ArrayList<>();
        distancesTable.forEach((key, value) -> factors.addAll(getNODs(value)));
        return factors;
    }

    /**
     * Get list of NODs for each pair of distances
     * @param distances - list of distances
     * @return - list of NODs
     */
    private List<Integer> getNODs(List<Integer> distances) {

        List<Integer> factors = new ArrayList<>();

        for (int i = 0; i < distances.size(); i++) {
            for (int j = i + 1; j < distances.size(); j++) {
                factors.add(MathUtil.getGCD(distances.get(i), distances.get(j)));
            }
        }

        return factors;
    }

    /**
     * Sort NODs according to how many times is occurs in list
     * @param nods - unsorted list of NODs
     * @return - sorted list of NODs
     */
    private List<Integer> getNODsAsSequentiallySortedList(List<Integer> nods) {

        Map<Integer, Integer> factorSequenceTable = new TreeMap<>();

        for (Integer factor : nods) {
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
