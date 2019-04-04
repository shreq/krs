package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.List;

public interface FeatureExtractor {
    /**
     * Extract numerical feature vector from given article
     * @param article article to extract features from
     * @return feature vector
     */
    List<Double> extract(Article article);
}
