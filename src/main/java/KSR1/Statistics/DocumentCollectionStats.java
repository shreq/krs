package KSR1.Statistics;

import KSR1.Article;

import java.util.*;

public class DocumentCollectionStats {

    public static class StringComparator extends WordComparator{
        public StringComparator() {
            super(null, 0);
        }

        @Override
        public boolean test(String s1, String s2){
            return s1.equals(s2);
        }
    }

    public static Map<String, Double> inverseDocumentFrequency(List<Article> articles){
        return inverseDocumentFrequencySoft(articles, new StringComparator());
    }

    /**
     * Inverse document frequency, where word equality is custom defined.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @return map where values are IDF for each group of words equal to key
     */
    public static Map<String, Double> inverseDocumentFrequencySoft(List<Article> articles, CaseComparator comparator) {
        TreeMap<String, Double> counter = new TreeMap<>();
        counter.put("upper", 0.);
        counter.put("lower", 0.);
        int documentCount = 0;
        for (Article article : articles) {
            documentCount++;

            for (String word : article.getWords()) {
                if(word.toUpperCase().charAt(0) == word.charAt(0)){
                    counter.put("upper", counter.get("upper")+1);
                }else{
                    counter.put("lower", counter.get("lower")+1);
                }
            }
        }

        for (Map.Entry<String, Double> wordCount : counter.entrySet()) {
            counter.replace(wordCount.getKey(), Math.log(documentCount / wordCount.getValue()));
        }
        return counter;
    }

    /**
     * Inverse document frequency, where word equality is custom defined.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @return map where values are IDF for each group of words equal to key
     */
    public static Map<String, Double> inverseDocumentFrequencySoft(List<Article> articles, WordComparator comparator){
        TreeMap<String, Double> counter = new TreeMap<>(comparator);
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

    public static Map<String, Double> termFrequency(List<Article> articles){
        return termFrequencySoft(articles, new StringComparator());
    }

    /**
     * Term frequency, where word equality is custom defined.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @return map where values are TF for each group of words equal to key
     */
    public static Map<String, Double> termFrequencySoft(List<Article> articles, WordComparator comparator){
        Map<String, Double> counter = new TreeMap<>(comparator);
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
