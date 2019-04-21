package KSR1.Extractors;

import KSR1.Article;
import KSR1.FuzzySet;
import KSR1.Settings;

import java.util.*;
import java.util.stream.Collectors;

import static KSR1.Statistics.DocumentCollectionStats.documentFrequency;
import static KSR1.Statistics.DocumentCollectionStats.termFrequency;

public class KeywordExtractor implements FeatureExtractor {

    private List<FuzzySet<String>> keywordsList;

    public KeywordExtractor(Collection<List<Article>> articlesSets, Settings.Method trainingMethod, int keywordsCount){
        this.keywordsList = new ArrayList<>();
        switch (trainingMethod){
            case TF:
                for(List<Article> articleSet : articlesSets){
                    keywordsList.add(new FuzzySet<>(getNLeast(termFrequency(articleSet), keywordsCount)));
                }
                break;
            case IDF:
                for(List<Article> articleSet : articlesSets){
                    keywordsList.add(new FuzzySet<>(getNLeast(documentFrequency(articleSet), keywordsCount)));
                }
                break;
            case DFRatio:
                // word -> df in each document set
                Map<String, List<Double>> results = new HashMap<>();
                for(List<Article> articleSet : articlesSets){
                    Map<String, Double> dfs = documentFrequency(articleSet);
                    for(Map.Entry<String, Double> df : dfs.entrySet()){
                        if(!results.containsKey(df.getKey())){
                            results.put(df.getKey(), new ArrayList<>());
                        }
                        results.get(df.getKey()).add(df.getValue());
                    }
                }
                results = results.entrySet().stream()
                        .filter(entry -> entry.getValue().size() == 5)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                for(int i=0; i<articlesSets.size(); i++){
                    Map<String, Double> keywords = new HashMap<>();
                    for(Map.Entry<String, List<Double>> wordDfs : results.entrySet()){
                        double currentArticleSetDF = wordDfs.getValue().get(i);
                        double meanDF = 0;
                        for(int j=0; j<wordDfs.getValue().size(); j++){
                            if(j!=i){
                                meanDF += wordDfs.getValue().get(j);
                            }
                        }
                        meanDF /= wordDfs.getValue().size()-1;
                        keywords.put(wordDfs.getKey(), currentArticleSetDF/meanDF);
                    }
                    double max = Collections.max(keywords.values());
                    keywordsList.add(new FuzzySet<>(keywords.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .limit(keywordsCount)
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()/max))));
                }
                break;
        }
    }

    private Map<String, Double> getNLeast(Map<String, Double> in, int keywordsCount){
        return in.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(keywordsCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Double> extract(Article article) {
        List<Double> values = new ArrayList<>();

        FuzzySet<String> termSet = new FuzzySet<>(termFrequency(article));
        for(FuzzySet<String> keywords : keywordsList){
            // we assume that for keywords universe equals supports i.e. each element has membership > 0
            // copy of $termSet containing only values that appear in $keywords
            FuzzySet<String> tmpSet = termSet.persistAllCopy(keywords.universe());

            // did all keywords appeared
            values.add(tmpSet.support().size() == keywords.universe().size() ? 1. : 0.);
            // percent of keywords that appeared
            values.add((double)tmpSet.support().size()/keywords.universe().size());
            // max term frequency
            values.add(Collections.max(tmpSet.values()));
            // min term frequency
            values.add(Collections.min(tmpSet.values()));
            // mean term frequency
            double mean = tmpSet.values().stream().reduce(Double::sum).get();
            mean /= tmpSet.universe().size();
            values.add(mean);
            // standard deviation of TF
            double variance = 0;
            for (double x : tmpSet.values()) {
                variance += Math.pow(x - mean, 2);
            }
            values.add(Math.sqrt(variance));

            // TODO: add more

            values.add(FuzzySet.jaccardSimilarity(tmpSet, keywords));
            values.add(FuzzySet.cosAmpSimilarity(tmpSet, keywords));
        }
        return values;
    }
}
