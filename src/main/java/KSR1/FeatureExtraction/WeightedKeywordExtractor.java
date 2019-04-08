package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.ArrayList;
import java.util.Map;

/**
 * Implementation of feature extractor that calculates feature vector based on TF-IDF of given keywords with weights.
 */
public class WeightedKeywordExtractor implements FeatureExtractor {

    Map<String, Double> keywords;

    public WeightedKeywordExtractor(Map<String, Double> keywords){
        this.keywords = keywords;
    }

    @Override
    public ArrayList<Double> extract(Article article) {
        ArrayList<Double> result = new ArrayList<>();

        int appearances = 0;
        ArrayList<String> words = article.getWords();
        for (String keyword : keywords.keySet()) {
            for (String word : words) {
                if (word.equals(keyword)) {
                    appearances++;
                }
            }
            result.add(keywords.get(keyword) * ((double) appearances / (double) words.size()) * ExtractionDB.getInstance().getIdf(keyword));
            appearances = 0;
        }

        return result;
    }
}
