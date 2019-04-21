package KSR1;

import KSR1.Extractors.CapitalLetterExtractor;
import KSR1.Extractors.FeatureExtractor;
import KSR1.Extractors.UniqueWordsExtractor;
import KSR1.Knn.ClassificationObject;
import KSR1.Knn.KnnClassifier;

import KSR1.Preprocessing.SnowballStemmer;
import KSR1.Statistics.Results;
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
//            documents = new DocumentCollection("src/main/resources/reuters/reut2-017.sgm");
            documents = new DocumentCollection(reutFiles);
        } catch (FileNotFoundException e) {
            System.exit(EXIT_IO);
        }
        LOGGER.log(Level.INFO, "Loaded {0} articles", documents.articles.size());

        documents.filterCategory(settings.category);
        LOGGER.log(Level.INFO, "{0} articles after filtering", documents.articles.size());
        documents.preprocess(new SnowballStemmer());

        DocumentCollection trainingDocuments = documents.splitGetSubset(settings.trainingPercent);
        List<FeatureExtractor> featureExtractors = new ArrayList<>();
        featureExtractors.add(trainingDocuments.makeKeywordExtractor(settings));
        featureExtractors.add(trainingDocuments.makeWordLengthExtractor());
        featureExtractors.add(new CapitalLetterExtractor());
        featureExtractors.add(new UniqueWordsExtractor());

        List<ClassificationObject> trainingObjects = trainingDocuments.extractClassificationObjects(settings, featureExtractors);
        KnnClassifier classifier = makeClassifier(settings, trainingObjects);

        Results results = new Results(settings.category);

        List<ClassificationObject> testObjects = documents.extractClassificationObjects(settings, featureExtractors);
        for(ClassificationObject object : testObjects){
            String actualClass = classifier.classifyObject(object);
            String expectedClass = object.getLabel();
            results.add(expectedClass, actualClass);
        }

        System.out.println(results);
        System.out.println(results.stats());
        LOGGER.info("END");
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
