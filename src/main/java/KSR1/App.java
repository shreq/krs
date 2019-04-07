package KSR1;

import org.json.simple.parser.ParseException;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.*;
import java.util.logging.*;


public class App {

    private static final Logger LOGGER = Logger.getLogger("Main");

    public static void main(String[] args)
    {
        Logger logger = Logger.getGlobal();
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        logger.setLevel(Level.ALL);
        try {
            Settings.loadSettings("config.json");
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

        ArrayList<Article> learnArticles;
        ArrayList<Article> testArticles;

        List<ArrayList<Article>> divideResult;
        divideResult = divideData(collection.articles, Settings.trainingPercent);
        learnArticles = divideResult.get(0);
        testArticles = divideResult.get(0);

        // TODO: learn
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
