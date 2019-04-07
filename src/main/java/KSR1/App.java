package KSR1;

import KSR1.FeatureExtraction.*;
import KSR1.Knn.ClassificationObject;
import KSR1.Knn.KnnClassifier;
import KSR1.Preprocessing.LancasterStemmer;
import KSR1.Processing.EditDistance;
import KSR1.Processing.GenBoundNGram;
import KSR1.Processing.NGram;
import KSR1.Statistics.DocumentCollectionStats;
import KSR1.Statistics.WordComparator;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.json.simple.parser.ParseException;

import javax.naming.ConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;

public class App {

    private static final Logger LOGGER = Logger.getLogger("Main");
    private static FeatureExtractor extractor;

    public static void main(String[] args)
    {
        initLogger();
        Settings settings = null;
        try {
            settings = Settings.loadSettings("config.json");
        } catch (IOException | ParseException | ConfigurationException e) {
            System.exit(EXIT_CONFIG);
        }

        DocumentCollection collection = null;
        try {
            collection = new DocumentCollection(Collections.singletonList("src/main/resources/reuters/reut2-000.sgm"));
//            collection = new DocumentCollection(files);
        } catch (FileNotFoundException e) {
            System.exit(EXIT_IO);
        }
        LOGGER.log(Level.INFO, "Loaded {0} articles", collection.articles.size());

        collection.preprocess(new LancasterStemmer());

        ArrayList<Article> learnArticles;
        ArrayList<Article> testArticles;

        List<ArrayList<Article>> divideResult;
        divideResult = divideData(collection.articles, settings.trainingPercent);
        learnArticles = divideResult.get(0);
        testArticles = divideResult.get(0);

        Set<String> keywords = makeKeywords(settings, learnArticles);
        extractor = makeExtractor(settings, keywords, learnArticles);

        // TODO: now we have extractor, time to classify
        ArrayList<Article> allArticles = new ArrayList<>();
        allArticles.addAll(learnArticles);
        allArticles.addAll(testArticles);
        List<ClassificationObject> classificationObjects = allArticles.stream()
                .map(App::mapArticleToClassificationObject).collect(Collectors.toList());

        List<ClassificationObject> learnClassificationObjects = classificationObjects.subList(0, learnArticles.size());
        List<ClassificationObject> testClassificationObjects = classificationObjects.subList(learnArticles.size(), allArticles.size());


    }

    private static void classify(List<ClassificationObject> learnArticles, List<ClassificationObject> testArticles, Settings settings) {
        KnnClassifier knnClassifier = new KnnClassifier(settings.k, learnArticles, new EuclideanDistance()); // TODO: distance


    }

    private static ClassificationObject mapArticleToClassificationObject(Article article) {
        ClassificationObject classificationObject = new ClassificationObject();

        classificationObject.setLabels(article.getTopics());
        classificationObject.values = extractor.extract(article);

        return classificationObject;
    }

    private static FeatureExtractor makeExtractor(Settings settings, Set<String> keywords, ArrayList<Article> articles) {
        ArrayList<FeatureExtractor> extractors = new ArrayList<>();
        for(String feature : settings.features){
            switch (feature){
                case "keywords":
                    extractors.add(new KeywordExtractor(keywords));
                    break;
                case "capitalLetters":
                    extractors.add(new CapitalKeywordExtractor(articles));
                    break;
                case "uniqueWords":
                    extractors.add(new UniqueWordsExtractor());
                    break;
            }
        }
        return new CombinedExtractor(extractors);
    }

    private static Set<String> makeKeywords(Settings settings, ArrayList<Article> articles){
        WordComparator comparator = makeComparator(settings);
        switch (settings.trainingMethod){
            case TF: {
                Map<String, Double> tfs = DocumentCollectionStats.termFrequencySoft(articles, comparator);
                if (settings.keywordsCount == Integer.MAX_VALUE) {
                    return tfs.keySet();
                }
                // sorted in order from smallest to greates term frequency
                SortedSet<Map.Entry<String, Double>> sortedTfs;
                sortedTfs = new TreeSet<>(Comparator.comparing(Map.Entry::getValue));
                sortedTfs.addAll(tfs.entrySet());
                // get first N results
                Set<String> result = new HashSet<>();
                int counter = 0;
                for (Map.Entry<String, Double> entry : sortedTfs) {
                    result.add(entry.getKey());
                    counter++;
                }
                return result;
            }
            case IDF: {
                Map<String, Double> idfs = DocumentCollectionStats.inverseDocumentFrequencySoft(articles, comparator);
                if (settings.keywordsCount == Integer.MAX_VALUE) {
                    return idfs.keySet();
                }
                // sorted in order from greates to smallest inverse document frequency
                SortedSet<Map.Entry<String, Double>> sortedIdfs;
                sortedIdfs = new TreeSet<>(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
                sortedIdfs.addAll(idfs.entrySet());
                // get first N results
                Set<String> result = new HashSet<>();
                int counter = 0;
                for (Map.Entry<String, Double> entry : sortedIdfs) {
                    result.add(entry.getKey());
                    counter++;
                }
                return result;
            }
            case ROne: {
                //ExtractionDB.initDB(articles, comparator);
                throw new RuntimeException("i will implement it in a moment");
            }

        }
        return null;
    }

    private static WordComparator makeComparator(Settings settings){
        switch (settings.equalityType){
            case NGram:
                return new WordComparator(new NGram(3), settings.threshold);
            case EDist:
                return new WordComparator(new EditDistance(), settings.threshold);
            case GenBoundNGram:
                return new WordComparator(new GenBoundNGram(2, 5), settings.threshold);
            case Strict:
            default:
                return new DocumentCollectionStats.StringComparator();
        }
    }

    private static void initLogger(){
        Logger logger = Logger.getGlobal();
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        logger.setLevel(Level.ALL);
    }

    private static List<ArrayList<Article>> divideData(ArrayList<Article> articles, double trainingPercent){
        ArrayList<Article> learnArticles = new ArrayList<>();
        ArrayList<Article> testArticles = new ArrayList<>();
        PrimitiveIterator.OfDouble random = new Random().doubles(0, 100).iterator();
        for(Article article : articles){
            if(random.nextDouble() < trainingPercent){
                learnArticles.add(article);
            }else {
                testArticles.add(article);
            }
        }
        return Arrays.asList(learnArticles, testArticles);
    }

    public static final int EXIT_CONFIG = 1;
    public static final int EXIT_IO = 2;

    static final ArrayList<String> files = new ArrayList<>(Arrays.asList(
            "src/main/resources/reuters/reut2-000.sgm",
            "src/main/resources/reuters/reut2-001.sgm",
            "src/main/resources/reuters/reut2-002.sgm",
            "src/main/resources/reuters/reut2-003.sgm",
            "src/main/resources/reuters/reut2-004.sgm",
            "src/main/resources/reuters/reut2-005.sgm",
            "src/main/resources/reuters/reut2-006.sgm",
            "src/main/resources/reuters/reut2-007.sgm",
            "src/main/resources/reuters/reut2-008.sgm",
            "src/main/resources/reuters/reut2-009.sgm",
            "src/main/resources/reuters/reut2-010.sgm",
            "src/main/resources/reuters/reut2-011.sgm",
            "src/main/resources/reuters/reut2-012.sgm",
            "src/main/resources/reuters/reut2-013.sgm",
            "src/main/resources/reuters/reut2-014.sgm",
            "src/main/resources/reuters/reut2-015.sgm",
            "src/main/resources/reuters/reut2-016.sgm",
            "src/main/resources/reuters/reut2-017.sgm",
            "src/main/resources/reuters/reut2-018.sgm",
            "src/main/resources/reuters/reut2-019.sgm",
            "src/main/resources/reuters/reut2-020.sgm",
            "src/main/resources/reuters/reut2-021.sgm"
        ));
}
