package KSR1.Extractors;

import KSR1.Article;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordLengthExtractor implements FeatureExtractor {
    private final int veryLongWord;
    private final int longWord;

    public WordLengthExtractor(List<Article> articles) {
        List<Integer> lengths = articles.stream().map(art -> art.getWords().size()).sorted().collect(Collectors.toList());
        veryLongWord = lengths.get(Math.round(0.9f*lengths.size()));
        longWord = lengths.get(Math.round(0.75f*lengths.size()));
    }

    @Override
    public List<Double> extract(Article article) {
        long veryLongWordCount = article.getWords().stream().filter(w -> w.length() > veryLongWord).count();
        long longWordCount = article.getWords().stream().filter(w -> w.length() > longWord).count();
        return Arrays.asList((double)longWordCount/article.getWords().size(),
                             (double)veryLongWordCount/article.getWords().size());
    }
}
