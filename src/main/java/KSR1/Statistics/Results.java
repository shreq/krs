package KSR1.Statistics;

import KSR1.Article;
import KSR1.Settings;
import com.sun.javafx.collections.MappingChange;

import java.util.*;

public class Results {

    private Map<String, Map<String, Integer>> rawResults;
    private Map<String, Map<String, Double>> scaledResults = null;

    public Results(Settings.Category category){
        rawResults = new HashMap<>();
        Set<String> allLabels = Article.getAllLabels(category);
        for(String label : allLabels){
            Map<String, Integer> inner = new HashMap<>();
            for(String inLabel : allLabels){
                inner.put(inLabel, 0);
            }
            rawResults.put(label, inner);
        }
    }

    public void add(String expected, String actual){
        rawResults.get(expected).put(actual, rawResults.get(expected).get(actual) + 1);
    }

    @Override
    public String toString() {
        if(scaledResults == null){
            scaleResults();
        }
        int fieldLength = 0;
        for(String label : scaledResults.keySet()){
            fieldLength = Integer.max(label.length(), fieldLength);
        }
        fieldLength += 1;
        StringBuilder result = new StringBuilder();

        result.append(String.join("", Collections.nCopies(fieldLength, " ")));
        for(String rowLabel : scaledResults.keySet()){
            result.append(String.format("%-xs".replace("x", Integer.toString(fieldLength)), rowLabel));
        }
        result.append('\n');
        for(Map.Entry<String, Map<String, Double>> row : scaledResults.entrySet()){
            result.append(String.format("%-xs".replace("x", Integer.toString(fieldLength)), row.getKey()));
            for(double val : row.getValue().values()){
                result.append(String.format("%-x.3f".replace("x", Integer.toString(fieldLength)), val));
            }
            result.append('\n');
        }
        return result.toString();
    }

    public void scaleResults(){
        scaledResults = new HashMap<>();
        for(Map.Entry<String, Map<String, Integer>> row : rawResults.entrySet()){
            int count = 0;
            for(int val : row.getValue().values()){
                count += val;
            }
            Map<String, Double> rowMap = new HashMap<>();
            for(Map.Entry<String, Integer> col : row.getValue().entrySet()){
                rowMap.put(col.getKey(), (double)col.getValue()/count);
            }
            scaledResults.put(row.getKey(), rowMap);
        }
    }

    /**
     * https://www.jakbadacdane.pl/accuracy-precision-recall-f1-co-to-za-czary/
     */
    public Map<String, Double> stats() {
        Map<String, Double> results = new HashMap<>();

        Map<String, Integer> rowSums = new HashMap<>();
        Map<String, Integer> colSums = new HashMap<>();
        Map<String, Integer> diagVals = new HashMap<>();
        int allCount = 0;

        for(Map.Entry<String, Map<String, Integer>> row : rawResults.entrySet()){
            int rowCount = 0;
            for(int val : row.getValue().values()){
                rowCount += val;
            }
            allCount += rowCount;
            rowSums.put(row.getKey(), rowCount);
            diagVals.put(row.getKey(), row.getValue().get(row.getKey()));

            for(Map.Entry<String, Integer> col : row.getValue().entrySet()){
                int count = colSums.getOrDefault(col.getKey(), 0);
                colSums.put(col.getKey(), count + col.getValue());
            }
        }

        // TODO: calculate actual values based on rowSums, colSums and diagVals
        results.put("accuracy", (double)diagVals.values().stream().reduce(Integer::sum).get()/allCount);
        double precision = 0;
        for(String key : diagVals.keySet()){
            precision += (double)diagVals.get(key)/colSums.get(key);
        }
        precision /= diagVals.size();
        results.put("precision", precision);
        double recall = 0;
        for(String key : diagVals.keySet()){
            recall += (double)diagVals.get(key)/rowSums.get(key);
        }
        recall /= diagVals.size();
        results.put("recall", recall);
        results.put("f1", 2*(precision*recall)/(precision+recall));

        return results;
    }
}
