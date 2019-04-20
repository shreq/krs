package KSR1.Extractors;

import KSR1.Article;

import java.util.List;

public interface FeatureExtractor {
    public abstract List<Double> extract(Article article);
}
