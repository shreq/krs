package KSR1;

import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import javax.naming.ConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

    private static final Logger LOGGER = Logger.getLogger( Settings.class.getName() );

    // data
    double trainingPercent;

    // training
    int keywordsCount;
    public Method trainingMethod;

    // knnParams
    int k;
    Metric distanceMetric;

    Category category;

    public static Settings loadSettings(String filepath) throws IOException, ParseException, ConfigurationException {
        Settings result = new Settings();
        JSONObject mapper = null;
        try {
            FileReader fileReader = new FileReader(filepath);
            mapper = (JSONObject) new JSONParser().parse(fileReader);
        } catch (IOException | ParseException ex) {
            LOGGER.log(Level.CONFIG, ex.toString());
            throw ex;
        }

        JSONObject data = (JSONObject) mapper.get("data");
        if(data == null || !data.containsKey("trainingPercent")){
            LOGGER.log(Level.INFO, "No data.trainingPercent found - setting to default value: 60");
            result.trainingPercent = 60;
        }else{
            result.trainingPercent = Double.parseDouble(data.get("trainingPercent").toString());
        }

        JSONObject training = (JSONObject) mapper.get("training");
        if(training == null || !training.containsKey("keywordsCount")){
            LOGGER.log(Level.INFO, "No training.keywordsCount found - setting to default value: 20");
            result.keywordsCount = 20;
        }else{
            if(training.get("keywordsCount").equals("MAX")){
                result.keywordsCount = Integer.MAX_VALUE;
            }else{
                result.keywordsCount = Integer.parseInt(training.get("keywordsCount").toString());
            }
        }
        if(training == null || !training.containsKey("method")){
            LOGGER.log(Level.INFO, "No training.trainingMethod found - setting to default value: tf-idf");
            result.trainingMethod = Method.TFIDF;
        }else{
            result.trainingMethod = methodFromString(training.get("method").toString());
        }

        JSONObject knnParams = (JSONObject) mapper.get("knnParams");
        if(knnParams == null || !knnParams.containsKey("k")){
            LOGGER.log(Level.SEVERE, "No knnParams.k found");
            throw new ConfigurationException("No knnParams.k parameter");
        }else{
            result.k = Integer.parseInt(knnParams.get("k").toString());
        }
        if(!knnParams.containsKey("distanceMetric")){
            LOGGER.log(Level.INFO, "No knnParams.distanceMetric found - setting to default value: manhattan");
            result.distanceMetric = Metric.Manhattan;
        }else{
            result.distanceMetric = metricFromString(knnParams.get("distanceMetric").toString());
        }

        if(mapper.containsKey("category")){
            result.category = categoryFromString(mapper.get("category").toString());
        }else{
            LOGGER.log(Level.SEVERE, "No category found");
            throw new ConfigurationException("No category parameter");
        }

        return result;
    }

    public enum Method{
        TF,
        TFIDF,
        ROne
    }

    private static Method methodFromString(String str){
        switch (str){
            case "tf":
                return Method.TF;
            case "tfidf":
                return Method.TFIDF;
            case "r1":
            case "r-one":
                return Method.ROne;
            default:
                throw new IllegalArgumentException("Invalid method");
        }
    }

    enum Metric{
        Manhattan,
        Chebyshev,
        Euclidean
    }

    private static Metric metricFromString(String str){
        switch (str.toLowerCase()){
            case "manhattan":
                return Metric.Manhattan;
            case "chebyshev":
                return Metric.Chebyshev;
            case "euclidean":
                return Metric.Euclidean;
            default:
                throw new IllegalArgumentException("Invalid distanceMetric");
        }
    }

    public enum Category {
        Places,
        Orgs,
        Course,
        Type
    }

    private static Category categoryFromString(String str){
        switch (str.toLowerCase()){
            case "places":
                return Category.Places;
            case "orgs":
                return Category.Orgs;
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }
}
