package KSR1.Extractors;

import KSR1.Article;
import KSR1.FuzzySet;
import KSR1.Settings;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static KSR1.Statistics.DocumentCollectionStats.inverseDocumentFrequency;
import static KSR1.Statistics.DocumentCollectionStats.termFrequency;

public class KeywordExtractor implements FeatureExtractor {

    private List<FuzzySet<String>> keywordsList;
    private Function<Article, FuzzySet<String>> wordStat;

    // TODO: change this
    private static final int keywordsCount = 20;

    public KeywordExtractor(Collection<List<Article>> articlesSets, Settings.Method trainingMethod){
        this.keywordsList = new ArrayList<>();
        for(List<Article> articleSet : articlesSets){

            // TODO: check trainingMethod and apply appropriate tranformations
            Map<String, Double> tfs = termFrequency(articleSet);
            Map<String, Double> idfs = inverseDocumentFrequency(articleSet);

            FuzzySet<String> keywordsSet = new FuzzySet<>(getNLeast(idfs));

            keywordsList.add(keywordsSet);
        }
        this.wordStat = art -> new FuzzySet<>(termFrequency(art));
    }

    private Map<String, Double> getNGreatest(Map<String, Double> in){
        return in.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(keywordsCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Double> getNLeast(Map<String, Double> in){
        return in.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(keywordsCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Double> extract(Article article) {
        List<Double> values = new ArrayList<>();

        FuzzySet<String> termSet = wordStat.apply(article);
        for(FuzzySet<String> keywords : keywordsList){
            values.add(FuzzySet.jaccardSimilarity(termSet, keywords));
            values.add(FuzzySet.cosAmpSimilarity(termSet, keywords));
        }
        return values;
    }
}
