package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.List;
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
    public List<Double> extract(Article article) {
        // TODO: implement
        throw new RuntimeException("not implemented");
    }
}
