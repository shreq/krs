package KSR1.Statistics;

import KSR1.Article;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Map<String, Double> inverseDocumentFrequency(List<Article> articles){
        return inverseDocumentFrequencySoft(articles, new StringComparator());
    }

    public static Map<String, Double> inverseDocumentFrequency(List<Article> articles, int results){
        return inverseDocumentFrequencySoft(articles, new StringComparator(), results);
    }

    /**
     * Inverse document frequency, where word equality is custom defined.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @return map where values are IDF for each group of words equal to key
     */
    public static Map<String, Double> inverseDocumentFrequencySoft(List<Article> articles, CaseComparator comparator) {
        TreeMap<String, Double> counter = new TreeMap<>(comparator);
        int documentCount = 0;
        for (Article article : articles) {
            documentCount++;

            for (String word : article.getWords()) {
                String letterCase = word.toUpperCase().charAt(0) == word.charAt(0) ? "upper" : "lower";
                double count = counter.getOrDefault(letterCase, 0.);
                count = Double.min(count + 1, documentCount);
                counter.put(letterCase, count);
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
     * Inverse document frequency, where word equality is custom defined. Only {@code results} best results are returned.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @param results numbers of results with
     * @return map where values are IDF for each group of words equal to key
     */
    public static Map<String, Double> inverseDocumentFrequencySoft(List<Article> articles, WordComparator comparator, int results){
        return getNGreatestMapValues(articles, comparator, results);
    }

    public static Map<String, Double> termFrequency(List<Article> articles){
        return termFrequencySoft(articles, new StringComparator());
    }

    public static Map<String, Double> termFrequency(List<Article> articles, int results){
        return termFrequencySoft(articles, new StringComparator(), results);
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
                wordCount++;
                counter.put(word, counter.getOrDefault(word, 0.) + 1);
            }
        }

        for(Map.Entry<String, Double> wordAndCount : counter.entrySet()){
            counter.replace(wordAndCount.getKey(), wordAndCount.getValue()/wordCount);
        }
        return counter;
    }

    /**
     * Term frequency, where word equality is custom defined.  Only {@code results} best results are returned.
     * @param articles list of articles
     * @param comparator comparator two use when checking equality of words
     * @return map where values are TF for each group of words equal to key
     */
    public static Map<String, Double> termFrequencySoft(List<Article> articles, WordComparator comparator, int results){
        return getNGreatestMapValues(articles, comparator, results);
    }

    private static Map<String, Double> getNGreatestMapValues(List<Article> articles, WordComparator comparator, int results) {
        Map<String, Double> tfs = inverseDocumentFrequencySoft(articles, comparator, results);
        SortedSet<Map.Entry<String, Double>> sortedTfs = new TreeSet<>(Comparator.comparing(Map.Entry::getValue));
        sortedTfs.addAll(tfs.entrySet());
        final Map<String, Double> result = sortedTfs.stream().skip(sortedTfs.size() - results).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return result;
    }



    /*

    1R
    https://pdfs.semanticscholar.org/03af/c233b07d0fdfbab169fae5dfd44e7e0fc1b9.pdf

     */
}
