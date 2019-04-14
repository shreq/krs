package KSR1;

import KSR1.Knn.ClassificationObject;
import KSR1.Knn.KnnClassifier;
import KSR1.Preprocessing.LancasterStemmer;

import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.json.simple.parser.ParseException;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class App {

    private static final Logger LOGGER = Logger.getLogger("Main");

    public static void main(String[] args)
    {
        initLogger();
        Settings settings = null;
        try {
            settings = Settings.loadSettings("config.json");
        } catch (IOException | ParseException | ConfigurationException e) {
            System.exit(EXIT_CONFIG);
        }

        DocumentCollection documents = null;
        try {
//            documents = new DocumentCollection(Collections.singletonList("src/main/resources/reuters/reut2-017.sgm"));
            documents = new DocumentCollection(files);
        } catch (FileNotFoundException e) {
            System.exit(EXIT_IO);
        }
        LOGGER.log(Level.INFO, "Loaded {0} articles", documents.articles.size());

        documents.filterCategory(settings.category);
        LOGGER.log(Level.INFO, "{0} articles after filtering", documents.articles.size());
        documents.preprocess(new LancasterStemmer());

        DocumentCollection trainingDocuments = documents.splitGetSubset(settings.trainingPercent);
        final List<FuzzySet<String>> keywordSets = trainingDocuments.makeKeywords(settings);

        List<ClassificationObject> trainingObjects = trainingDocuments.extractClassificationObjects(settings, keywordSets);
        KnnClassifier classifier = makeClassifier(settings, trainingObjects);

        Map<String, Map<String, Integer>> results = makeResultsArray(settings);

        List<ClassificationObject> testObjects = documents.extractClassificationObjects(settings, keywordSets);
        for(ClassificationObject object : testObjects){
            String actualClass = classifier.classifyObject(object);
            String expectedClass = object.getLabel();
            Map<String, Integer> innerMap = results.get(actualClass);
            innerMap.put(expectedClass, innerMap.get(expectedClass) + 1);
            results.put(actualClass, innerMap);
        }

        System.out.println(results);
    }

    private static Map<String, Map<String, Integer>> makeResultsArray(Settings settings) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        Set<String> categories;
        if(settings.category.equals("places")){
            categories = DocumentCollection.allowedPlaces;
        }else if(settings.category.equals("orgs")){
            categories = DocumentCollection.allowedOrgs;
        }else {
            throw new IllegalArgumentException("Invalid category");
        }
        for(String category : categories){
            Map<String, Integer> inner = new HashMap<>();
            for(String inCat : categories){
                inner.put(inCat, 0);
            }
            result.put(category, inner);
        }
        return result;
    }

    private static KnnClassifier makeClassifier(Settings settings, List<ClassificationObject> articles){
        DistanceMeasure distanceMeasure;
        switch (settings.distanceMetric){
            case Chebyshev:
                distanceMeasure = new ChebyshevDistance();
                break;
            case Euclidean:
                distanceMeasure = new EuclideanDistance();
                break;
            case Manhattan:
            default:
                distanceMeasure = new ManhattanDistance();
                break;
        }
        return new KnnClassifier(settings.k, articles, distanceMeasure);
    }

    private static void initLogger(){
        Logger logger = Logger.getGlobal();
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        logger.setLevel(Level.ALL);
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
