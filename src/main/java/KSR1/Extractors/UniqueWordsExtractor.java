package KSR1.Extractors;

import KSR1.Article;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class UniqueWordsExtractor implements FeatureExtractor {
    @Override
    public List<Double> extract(Article article) {
        int uniqueCount = new HashSet<>(article.getWords()).size();
        return Collections.singletonList((double)uniqueCount/article.getWords().size());
    }
}
