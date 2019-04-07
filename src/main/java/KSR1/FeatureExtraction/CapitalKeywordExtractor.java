package KSR1.FeatureExtraction;

import KSR1.Article;
import KSR1.Statistics.CaseComparator;
import KSR1.Statistics.DocumentCollectionStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of feature extractor that calculates feature vector based on TF-IDF of words with capital letters
 */
public class CapitalKeywordExtractor implements FeatureExtractor {

    private List<Article> articles;

    public CapitalKeywordExtractor(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public List<Double> extract(Article article) {
        ArrayList<Double> result = new ArrayList<>();

        int appearances = 0;
        ArrayList<String> words = article.getWords();
        for (String word : words) {
            if (word.charAt(0) <= 'Z' && word.charAt(0) >= 'A') {
                appearances++;
            }
        }

        result.add(((double) appearances / (double) words.size()) * DocumentCollectionStats.inverseDocumentFrequencySoft(articles, new CaseComparator()).get("upper"));

        return result;
    }
}
