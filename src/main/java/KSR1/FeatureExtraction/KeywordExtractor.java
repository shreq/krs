package KSR1.FeatureExtraction;

import KSR1.Article;

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
        // TODO: implement
        throw new RuntimeException("not implemented");
    }
}
