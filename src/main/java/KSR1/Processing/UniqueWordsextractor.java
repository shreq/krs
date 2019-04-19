package KSR1.Processing;

import KSR1.Article;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class UniqueWordsextractor implements FeatureExtractor {
    @Override
    public List<Double> extract(Article article) {
        int uniqueCount = new HashSet<>(article.getWords()).size();
        return Collections.singletonList((double)uniqueCount/article.getWords().size());
    }
}
