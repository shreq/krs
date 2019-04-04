package KSR1.Statistics;

import KSR1.Processing.Similarity;

import java.util.Comparator;
import java.util.function.BiPredicate;

public class WordComparator implements BiPredicate<String, String>, Comparator<String> {

    private Similarity similarityMeasure;
    private int threshold;

    WordComparator(Similarity similarityMeasure, int threshold){
        this.similarityMeasure = similarityMeasure;
        this.threshold = threshold;
    }

    /**
     * Test if strings are similar to certain extent specified by threshold
     * @param word1 term to compare
     * @param word2 term to compare
     * @return true if words are similar enough
     */
    @Override
    public boolean test(String word1, String word2) {
        return similarityMeasure.compare(word1, word1) > threshold;
    }

    @Override
    public int compare(String word1, String word2) {
        if(test(word1, word2)){
            return 0;
        }
        return word1.compareTo(word2);
    }
}
