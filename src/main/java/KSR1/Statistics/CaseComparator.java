package KSR1.Statistics;

import java.util.Comparator;
import java.util.function.BiPredicate;

public class CaseComparator implements BiPredicate<String, String>, Comparator<String> {

    /**
     * Test if beginning of words is of the same case
     * @param word1 term to compare
     * @param word2 term to compare
     * @return true if both words starts with upper or lower case letters
     */
    @Override
    public boolean test(String word1, String word2) {
        return (word1.toUpperCase().charAt(0) == word1.charAt(0) && word2.toUpperCase().charAt(0) == word2.charAt(0)) ||
                (word1.toLowerCase().charAt(0) == word1.charAt(0) && word2.toLowerCase().charAt(0) == word2.charAt(0));

    }

    @Override
    public int compare(String word1, String word2) {
        return test(word1, word2) ? 1 : 0;
    }
}
