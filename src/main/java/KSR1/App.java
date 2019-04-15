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
import java.util.stream.Collectors;

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
            documents = new DocumentCollection(reutFiles);
        } catch (FileNotFoundException e) {
            System.exit(EXIT_IO);
        }
        LOGGER.log(Level.INFO, "Loaded {0} articles", documents.articles.size());

        documents.filterCategory(settings.category);
        LOGGER.log(Level.INFO, "{0} articles after filtering", documents.articles.size());
        documents.preprocess(new LancasterStemmer());

        DocumentCollection trainingDocuments = documents.splitGetSubset(settings.trainingPercent);
        final List<FuzzySet<String>> keywordSets = trainingDocuments.makeKeywords(settings);
        final int longWordLength = trainingDocuments.getLongWordLength();

        List<ClassificationObject> trainingObjects = trainingDocuments.extractClassificationObjects(settings, keywordSets, longWordLength);
        KnnClassifier classifier = makeClassifier(settings, trainingObjects);

        Map<String, Map<String, Integer>> results = makeResultsArray(settings);

        List<ClassificationObject> testObjects = documents.extractClassificationObjects(settings, keywordSets, longWordLength);
        for(ClassificationObject object : testObjects){
            String actualClass = classifier.classifyObject(object);
            String expectedClass = object.getLabel();
            Map<String, Integer> innerMap = results.get(expectedClass);
            innerMap.put(actualClass, innerMap.get(actualClass) + 1);
            results.put(expectedClass, innerMap);
        }

        System.out.println(results);
        printResults(results);
        System.out.println();
        printStats(results);
    }

    private static void printStats(Map<String, Map<String, Integer>> results) {
        List<Integer> accVals = new ArrayList<>();
        Map<String, Integer> precVals = new HashMap<>();
        Map<String, Integer> precValsSpec = new HashMap<>();

        int count = 0;
        for(Map.Entry<String, Map<String, Integer>> row : results.entrySet()){
            for(int val : row.getValue().values()){
                count += val;
            }
            accVals.add(row.getValue().get(row.getKey()));
            precValsSpec.put(row.getKey(), row.getValue().get(row.getKey()));
            for(Map.Entry<String, Integer> col : row.getValue().entrySet()){
                int pCount = precVals.getOrDefault(col.getKey(), 0);
                precVals.put(col.getKey(), pCount + col.getValue());
            }
        }
        int accSum = 0;
        for(int val : accVals){
            accSum += val;
        }
        System.out.println("Accuracy = " + (double)accSum/count);

        double pSumm = 0;
        for(Map.Entry<String, Integer> p : precVals.entrySet()){
            pSumm += (double)precValsSpec.get(p.getKey())/p.getValue();
        }
        pSumm /= 5;
        System.out.println("Precision = " + pSumm);
    }

    private static void printResults(Map<String, Map<String, Integer>> res){
        Map<String, Integer> counter = new HashMap<>();

        System.out.print("       ");
        for(String rowLabel : res.keySet()){
            System.out.print(String.format("%-7s", rowLabel));
        }
        System.out.println();
        for(Map.Entry<String, Map<String, Integer>> row : res.entrySet()){
            int count = 0;
            for(int val : row.getValue().values()){
                count += val;
            }

            System.out.print(String.format("%-7s", row.getKey()));
            for(int val : row.getValue().values()){
                System.out.print(String.format("%-7.3f", (double)val/count));
            }
            System.out.println();
        }
    }

    private static Map<String, Map<String, Integer>> makeResultsArray(Settings settings) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        Set<String> categories;
        if(settings.category == Settings.Category.Places){
            categories = DocumentCollection.allowedPlaces;
        }else if(settings.category == Settings.Category.Orgs){
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

    static final ArrayList<String> reutFiles = new ArrayList<>(Arrays.asList(
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

    static final String custFile = "src/main/resources/custom/recipes.xml";
}
