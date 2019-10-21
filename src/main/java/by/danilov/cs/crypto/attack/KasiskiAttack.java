package by.danilov.cs.crypto.attack;

import by.danilov.cs.crypto.util.MathUtil;
import by.danilov.cs.crypto.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class KasiskiAttack {

    /*Min keyword length*/
    private static final Integer MIN_LENGTH = 3;
    /*Max keyword length*/
    private static final Integer MAX_LENGTH = 30;

    public Integer perform(String cryptoText) {
        return suggestKeywordLength(cryptoText);
    }

    /**
     * Find length of keyword, that was used to encrypt  text
     * @param cryptoText - encrypted text
     * @return - keyword length
     */
    private Integer suggestKeywordLength(String cryptoText) {

        cryptoText = StringUtil.reduceNonLetter(cryptoText);

        /*Map: <substring, list of indices of each substring occurrence>*/
        Map<String, List<Integer>> substringTable = new LinkedHashMap<>();

        /*Find map of substrings of each length between min and max possible lengths*/
        for (int i = MIN_LENGTH; i < MAX_LENGTH; i++) {
            substringTable.putAll(getRepeatableSubstrings(cryptoText, i));
        }

        var distanceTable = getDistances(substringTable);
        var GCDs = getGCDs(distanceTable);
        var GCDsAsList = getGCDsAsSequentiallySortedList(GCDs);

        if (GCDsAsList.isEmpty()) {
            return MIN_LENGTH;
        }

        return GCDsAsList.get(0);

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
     * Get list of all distances GCD
     * @param distancesTable - initial table: <substring, distances>
     * @return - list of GCDs
     */
    private List<Integer> getGCDs(Map<String, List<Integer>> distancesTable) {
        List<Integer> factors = new ArrayList<>();
        distancesTable.forEach((key, value) -> factors.addAll(getGCDs(value)));
        return factors;
    }

    /**
     * Get list of GCDs for each pair of distances
     * @param distances - list of distances
     * @return - list of GCDs
     */
    private List<Integer> getGCDs(List<Integer> distances) {

        List<Integer> factors = new ArrayList<>();

        for (int i = 0; i < distances.size(); i++) {
            for (int j = i + 1; j < distances.size(); j++) {
                factors.add(MathUtil.getGCD(distances.get(i), distances.get(j)));
            }
        }

        return factors;
    }

    /**
     * Sort GCDs according to how many times they occur in list
     * @param GCDs - unsorted list of GCDs
     * @return - sorted list of GCDs
     */
    private List<Integer> getGCDsAsSequentiallySortedList(List<Integer> GCDs) {

        /*
        Map: <GCD, how many times GCD occurs>
         */
        Map<Integer, Integer> GCDSequenceTable = new TreeMap<>();

        for (Integer factor : GCDs) {
            if (GCDSequenceTable.containsKey(factor)) {
                Integer value = GCDSequenceTable.get(factor);
                GCDSequenceTable.put(factor, ++value);
            } else {
                GCDSequenceTable.put(factor, 1);
            }
        }

        return GCDSequenceTable.entrySet().stream()
                /* sort by number of GCD's occurrences*/
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                /*Transform map to list of GCDs*/
                .map(Map.Entry::getKey)
                /* Filter out GCDs that can't be keywords */
                .filter(factor -> factor <= MAX_LENGTH)
                .filter(factor -> factor >= MIN_LENGTH)
                .collect(Collectors.toList());

    }


}
