package KSR1.Processing;

import KSR1.Article;

import java.util.Collections;
import java.util.List;

public class WordLengthExtractor implements FeatureExtractor {
    private final int longWordLength;

    // TODO: decide what is long and what is short here
    public WordLengthExtractor(int longWordLength){
        this.longWordLength = longWordLength;
    }

    @Override
    public List<Double> extract(Article article) {
        long longWordCount = article.getWords().stream().filter(w -> w.length() > longWordLength).count();
        return Collections.singletonList((double)longWordCount/article.getWords().size());
    }
}
