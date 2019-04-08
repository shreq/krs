package KSR1.FeatureExtraction;

import KSR1.Article;
import KSR1.Statistics.DocumentCollectionStats;
import KSR1.Statistics.WordComparator;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Database of words TFs and IDFs to enable feature extraction per single Article (FeatureExtractor interface) and optimize it.
 */
public class ExtractionDB {

    private static ExtractionDB instance = null;

    public static Map<String, Double> idfs;

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
        try{
            return idfs.get(word);
        }catch (NullPointerException e){
            Logger.getLogger("XD").info(word);
        }
        return 0.;
    }

    private ExtractionDB(List<Article> articles) {
        idfs = DocumentCollectionStats.inverseDocumentFrequency(articles);
    }

    private ExtractionDB(List<Article> articles, WordComparator comparator) {
        idfs = DocumentCollectionStats.inverseDocumentFrequencySoft(articles, comparator);
    }
}
