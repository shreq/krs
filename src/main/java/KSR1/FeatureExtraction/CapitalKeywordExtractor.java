package KSR1.FeatureExtraction;

import KSR1.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of feature extractor that calculates feature vector based on TF-IDF of words with capital letters
 */
public class CapitalKeywordExtractor implements FeatureExtractor {

    @Override
    public List<Double> extract(Article article) {      // TODO: implement
        ArrayList<Double> result = new ArrayList<>();

        int appearances = 0;
        ArrayList<String> words = article.getWords();
        for (String word : words) {
            if (word.toUpperCase().charAt(0) == word.charAt(0)) {
                appearances++;
            }
        }

        //idf of all words starting with capital letter?
        //result.add(((double) appearances / (double) words.size()) * ExtractionDB.getInstance().getIdf(word));

        return result;
    }
}
