package KSR1;

import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import javax.naming.ConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

    private static final Logger LOGGER = Logger.getLogger( Settings.class.getName() );

    // data
    double trainingPercent;

    // training
    int keywordsCount;
    Method trainingMethod;

    // features
    ArrayList<String> features;

    // equality
    Equality equalityType;
    double threshold;

    // knnParams
    int k;
    Metric distanceMetric;

    String category;

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
            result.trainingMethod = Method.IDF;
        }else{
            result.trainingMethod = methodFromString(training.get("method").toString());
        }

        result.features = new ArrayList<>();
        JSONArray features = (JSONArray) mapper.get("features");
        if(features != null && !features.isEmpty()){
            for(Object value : features){
                result.features.add((String) value);
            }
        }else{
            LOGGER.log(Level.INFO, "No features found - setting to default value: [keywords]");
            result.features.add("keywords");
        }

        JSONObject equality = (JSONObject) mapper.get("equality");
        if(equality == null || !equality.containsKey("equalityType")){
            LOGGER.log(Level.INFO, "No equality.equalityType found - setting to default value: strict");
            result.equalityType = Equality.Strict;
        }else{
            result.equalityType = equalityFromString(equality.get("equalityType").toString());
        }
        if(!result.equalityType.equals(Equality.Strict) && !equality.containsKey("threshold")){
            LOGGER.log(Level.SEVERE, "No equality.threshold found");
            throw new ConfigurationException("No equality.threshold parameter");
        }else{
            result.threshold = Double.parseDouble(equality.get("threshold").toString());
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
            result.category = mapper.get("category").toString().toLowerCase();
        }else{
            LOGGER.log(Level.INFO, "No category found - setting to default value: places");
            result.category = "places";
        }

        return result;
    }

    enum Equality{
        Strict,
        NGram,
        GenBoundNGram,
        EDist
    }

    private static Equality equalityFromString(String str){
        switch (str){
            case "strict":
                return Equality.Strict;
            case "nGram":
                return Equality.NGram;
            case "genBoundNGram":
                return Equality.GenBoundNGram;
            case "eDist":
                return Equality.EDist;
            default:
                throw new IllegalArgumentException("Invalid equality type");
        }
    }

    private static String toString(Equality eq){
        switch (eq){
            case Strict:
                return "strict";
            case NGram:
                return "nGram";
            case GenBoundNGram:
                return "genBoundNGram";
            case EDist:
                return "eDist";
            default:
                return "";
        }
    }

    enum Method{
        IDF,
        TF,
        ROne,
    }

    private static Method methodFromString(String str){
        switch (str){
            case "idf":
                return Method.IDF;
            case "tf":
                return Method.TF;
            case "r1":
            case "r-one":
                return Method.ROne;
            default:
                throw new IllegalArgumentException("Invalid equality equalityType");
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
                throw new IllegalArgumentException("Invalid equality distanceMetric");
        }
    }
}
