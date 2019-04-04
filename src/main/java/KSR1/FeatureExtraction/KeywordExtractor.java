package KSR1.FeatureExtraction;

import KSR1.Article;
import KSR1.Statistics.DocumentCollectionStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of feature extractor that calculates feature vector based on TF-IDF of given keywords.
 */
public class KeywordExtractor implements FeatureExtractor {

    private Set<String> keywords;

    public KeywordExtractor(Set<String> keywords){
        this.keywords = keywords;
    }

    @Override
    public List<Double> extract(Article article) {
        ArrayList<Double> result = new ArrayList<>();

        int appearances = 0;
        ArrayList<String> words = article.getWords();
        for (String keyword : keywords) {
            for (String word : words) {
                if (word.equals(keyword)) {
                    appearances++;
                }
            }
            result.add(((double) appearances / (double) words.size()) * ExtractionDB.getInstance().getIdf(keyword));
            appearances = 0;
        }

        return result;
    }
}
