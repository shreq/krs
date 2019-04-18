package KSR1.Processing;

import KSR1.Article;
import KSR1.FuzzySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class KeywordExtractor implements FeatureExtractor {

    private List<FuzzySet<String>> keywordsList;
    private Function<Article, Map<String, Double>> wordStat;

    public KeywordExtractor(List<FuzzySet<String>> keywordsList, Function<Article, Map<String, Double>> wordStat){
        this.keywordsList = keywordsList;
        this.wordStat = wordStat;
    }

    @Override
    public List<Double> extract(Article article) {
        List<Double> values = new ArrayList<>();

        Map<String, Double> tfs = wordStat.apply(article);
        FuzzySet<String> termSet = new FuzzySet<>(tfs);
        for(FuzzySet<String> keywords : keywordsList){
            values.add(FuzzySet.jaccardSimilarity(termSet, keywords));
            values.add(FuzzySet.cosAmpSimilarity(termSet, keywords));
        }
        return values;
    }
}
