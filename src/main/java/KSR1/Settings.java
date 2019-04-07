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
    static double trainingPercent;

    // training
    static int keywordsCount;
    static String method;

    // features
    static ArrayList<String> features;

    // equality
    static Equality type;
    static double threshold;

    // knnParams
    static int k;

    public static void loadSettings(String filepath) throws IOException, ParseException, ConfigurationException {
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
            LOGGER.log(Level.INFO, "No data.trainingPercent found - setting to default value: 40");
            trainingPercent = 40;
        }else{
            trainingPercent = Double.parseDouble(data.get("trainingPercent").toString());
        }

        JSONObject training = (JSONObject) mapper.get("training");
        if(training == null || !training.containsKey("keywordsCount")){
            LOGGER.log(Level.INFO, "No training.keywordsCount found - setting to default value: 20");
            keywordsCount = 20;
        }else{
            if(training.get("keywordsCount").equals("MAX")){
                keywordsCount = Integer.MAX_VALUE;
            }else{
                keywordsCount = Integer.parseInt(training.get("keywordsCount").toString());
            }
        }
        if(training == null || !training.containsKey("method")){
            LOGGER.log(Level.INFO, "No training.method found - setting to default value: tf-idf");
            method = "tf-idf";
        }else{
            method = training.get("method").toString();
        }

        Settings.features = new ArrayList<>();
        JSONArray features = (JSONArray) mapper.get("features");
        if(features != null && !features.isEmpty()){
            for(Object value : features){
                Settings.features.add((String) value);
            }
        }else{
            LOGGER.log(Level.INFO, "No features found - setting to default value: [keywords]");
            Settings.features.add("keywords");
        }

        JSONObject equality = (JSONObject) mapper.get("equality");
        if(equality == null || !equality.containsKey("type")){
            LOGGER.log(Level.INFO, "No equality.type found - setting to default value: strict");
            type = Equality.Strict;
        }else{
            type = fromString(equality.get("type").toString());
        }
        if(!type.equals(Equality.Strict) && !equality.containsKey("threshold")){
            LOGGER.info(equality.toJSONString());
            LOGGER.log(Level.SEVERE, "No equality.threshold found");
            throw new ConfigurationException("No equality.threshold parameter");
        }else{
            threshold = Double.parseDouble(equality.get("threshold").toString());
        }

        JSONObject knnParams = (JSONObject) mapper.get("knnParams");
        if(knnParams == null || !knnParams.containsKey("k")){
            LOGGER.log(Level.SEVERE, "No knnParams.k found");
            throw new ConfigurationException("No knnParams.k parameter");
        }else{
            k = Integer.parseInt(knnParams.get("k").toString());
        }
    }

    enum Equality{
        Strict,
        NGram,
        GenBoundNGram,
        EDist
    }
    private static Equality fromString(String str){
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
}
