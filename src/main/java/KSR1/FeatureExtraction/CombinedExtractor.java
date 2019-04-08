package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concatenate feature vectors of given extractors.
 */
public class CombinedExtractor implements FeatureExtractor {

    private List<FeatureExtractor> extractors;

    public CombinedExtractor(FeatureExtractor... extractors){
        this.extractors = Arrays.stream(extractors).collect(Collectors.toList());
    }

    public CombinedExtractor(List<FeatureExtractor> extractors){
        this.extractors = extractors;
    }

    @Override
    public ArrayList<Double> extract(Article article) {
        ArrayList<Double> result = new ArrayList<>();
        for (FeatureExtractor extractor : extractors) {
            result.addAll(extractor.extract(article));
        }
        return result;
    }
}
