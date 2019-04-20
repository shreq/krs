package KSR1.Statistics;

import KSR1.Article;

import java.util.*;

public class DocumentCollectionStats {

    /**
     * Document frequency
     * @param articles list of articles
     * @return map of DF for each word
     */
    public static Map<String, Double> documentFrequency(List<Article> articles){
        Map<String, Double> counter = new HashMap<>();
        int documentCount = 0;
        for(Article article : articles){
            documentCount++;
            for(String word : article.getWords()){
                word = word.toLowerCase();
                double count = counter.getOrDefault(word, 0.);
                count = Double.min(count + 1, documentCount);
                counter.put(word, count);
            }
        }

        for(Map.Entry<String, Double> wordCount : counter.entrySet()){
            counter.replace(wordCount.getKey(), wordCount.getValue()/documentCount);
        }
        return counter;
    }

    /**
     * Inverse document frequency
     * @param articles list of articles
     * @return map of IDF for each word
     */
    public static Map<String, Double> inverseDocumentFrequency(List<Article> articles){
        Map<String, Double> dfs = documentFrequency(articles);

        for(Map.Entry<String, Double> df : dfs.entrySet()){
            dfs.replace(df.getKey(), Math.log(1/df.getValue()));
        }
        return dfs;
    }

    public static Map<String, Double> inverseDocumentFrequency(Article article){
        return inverseDocumentFrequency(Collections.singletonList(article));
    }

    /**
     * Term frequency
     * @param articles list of articles
     * @return map of TF for each word
     */
    public static Map<String, Double> termFrequency(List<Article> articles){
        Map<String, Double> counter = new HashMap<>();
        int wordCount = 0;
        for(Article article : articles){
            for(String word : article.getWords()){
                word = word.toLowerCase();
                wordCount++;
                counter.put(word, counter.getOrDefault(word, 0.) + 1);
            }
        }

        for(Map.Entry<String, Double> wordAndCount : counter.entrySet()){
            counter.replace(wordAndCount.getKey(), wordAndCount.getValue()/wordCount);
        }
        return counter;
    }

    public static Map<String, Double> termFrequency(Article article){
        return termFrequency(Collections.singletonList(article));
    }
}
