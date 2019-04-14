package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.*;

public class UniqueWordsExtractor implements FeatureExtractor {
    @Override
    public ArrayList<Double> extract(Article article) {
        double size = new HashSet<>(article.getWords()).size();
        ArrayList<Double> result = new ArrayList<>();
        result.add(size/article.getWords().size());
        return result;
    }
}
