package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.*;

public class UniqueWordsExtractor implements FeatureExtractor {
    @Override
    public List<Double> extract(Article article) {
        double size = new HashSet<String>(article.getWords()).size();
        ArrayList<Double> result = new ArrayList<Double>();
        result.add(size);
        return result;
    }
}
