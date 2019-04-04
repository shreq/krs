package KSR1.FeatureExtraction;

import KSR1.Article;
import KSR1.Statistics.DocumentCollectionStats;
import KSR1.Statistics.WordComparator;

import java.util.List;
import java.util.Map;

/**
 * Database of words TFs and IDFs to enable feature extraction per single Article (FeatureExtractor interface) and optimize it.
 */
public class ExtractionDB {

    private static ExtractionDB instance = null;

    private static Map<String, Double> idfs;

    public static void initDB(List<Article> articles) {
        if(instance == null){
            instance = new ExtractionDB(articles);
        }
    }

    public static void initDB(List<Article> articles, WordComparator comparator) {
        if(instance == null){
            instance = new ExtractionDB(articles, comparator);
        }
    }

    public static void dropDB() {
        instance = null;
        idfs.clear();
    }

    public static ExtractionDB getInstance() {
        if(instance == null){
            throw new RuntimeException("Not initialized");
        }
        return instance;
    }

    public double getIdf(String word){
        return idfs.get(word);
    }

    private ExtractionDB(List<Article> articles) {
        idfs = DocumentCollectionStats.inverseDocumentFrequency(articles);
    }

    private ExtractionDB(List<Article> articles, WordComparator comparator) {
        idfs = DocumentCollectionStats.inverseDocumentFrequencySoft(articles, comparator);
    }
}
