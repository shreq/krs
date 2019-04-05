package KSR1;

import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Settings {

    // data
    static double trainingPercent;

    // training
    static double keywordsCount;
    static String method;

    // features
    static ArrayList<String> keywords;
    static ArrayList<String> capitalLetters;
    static ArrayList<String> uniqueWords;

    // equality
    static String type;
    static double threshold;

    // knnParams
    static double k;

    public static void loadSettings(String filepath) {
        Map<String, Map<String, Object>> mapper = null;
        try {
            mapper = (JSONObject) new JSONParser().parse(new FileReader(filepath));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //Map<String, Map<String, Object>> mapper = parser;

        trainingPercent = Double.parseDouble(mapper.get("data").getOrDefault("trainingPercent", "40").toString());
        keywordsCount = Double.parseDouble(mapper.get("training").getOrDefault("keywordsCount", "20").toString());
        method = mapper.get("training").getOrDefault("method", "idf|tf|r1").toString();

        // TODO: fix it
        /*for (Object o : (ArrayList) mapper.get("features").get("keywords")) {
            keywords.add(o.toString());
        }
        for (Object o : (ArrayList) mapper.get("features").get("capitalLetters")) {
            capitalLetters.add(o.toString());
        }
        for (Object o : (ArrayList) mapper.get("features").get("uniqueWords")) {
            uniqueWords.add(o.toString());
        }*/

        type = mapper.get("equality").getOrDefault("type", "strict|nGram|genBoundNGram|eDist").toString();
        threshold = Double.parseDouble(mapper.get("equality").getOrDefault("threshold", "0.9").toString());
        k = Double.parseDouble(mapper.get("knnParams").getOrDefault("k", "5").toString());
    }
}
