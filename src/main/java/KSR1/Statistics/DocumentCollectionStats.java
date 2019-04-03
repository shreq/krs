package KSR1.Statistics;

import KSR1.Article;

import java.util.*;

public class DocumentCollectionStats {

    private static class StringComparator extends WordComparator{
        StringComparator() {
            super(null, 0);
        }

        @Override
        public boolean test(String s1, String s2){
            return s1.equals(s2);
        }
    }

    public static TreeMap<String, Double> inverseDocumentFrequency(List<Article> articles){
        return inverseDocumentFrequency(articles, Integer.MAX_VALUE);
    }

    public static TreeMap<String, Double> inverseDocumentFrequency(List<Article> articles, int results){
        return inverseDocumentFrequencySoft(articles, new StringComparator(), results);
    }

    public static TreeMap<String, Double> inverseDocumentFrequencySoft(List<Article> articles, WordComparator comparator){
        return inverseDocumentFrequencySoft(articles, comparator, Integer.MAX_VALUE);
    }

    public static TreeMap<String, Double> inverseDocumentFrequencySoft(List<Article> articles, WordComparator comparator, int results){
        TreeMap<String, Double> counter = new TreeMap<>(comparator);
        int documentCount = 0;
        for(Article article : articles){
            documentCount++;
            for(String word : article.getWords()){
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

    public static TreeMap<String, Double> termFrequency(List<Article> articles){
        return termFrequency(articles, Integer.MAX_VALUE);
    }

    public static TreeMap<String, Double> termFrequency(List<Article> articles, int results){
        return termFrequencySoft(articles, new StringComparator(), results);
    }

    public static TreeMap<String, Double> termFrequencySoft(List<Article> articles, WordComparator comparator){
        return termFrequencySoft(articles, comparator, Integer.MAX_VALUE);
    }

    public static TreeMap<String, Double> termFrequencySoft(List<Article> articles, WordComparator comparator, int results){
        TreeMap<String, Double> counter = new TreeMap<>(comparator);
        int wordCount = 0;
        for(Article article : articles){
            for(String word : article.getWords()){
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
