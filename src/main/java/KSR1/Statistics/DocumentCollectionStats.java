package KSR1.Statistics;

import KSR1.Article;

import java.util.*;

public class DocumentCollectionStats {

    /**
     * Inverse document frequency
     * @param articles list of articles
     * @return map of IDF for each word
     */
    public static Map<String, Double> inverseDocumentFrequency(List<Article> articles){
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
            counter.replace(wordCount.getKey(), Math.log(documentCount/wordCount.getValue()));
        }
        return counter;
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
}
