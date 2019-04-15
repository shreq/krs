package KSR1;

import java.util.*;

public class FuzzySet<ElemType> {

    private HashMap<ElemType, Double> set;

    public FuzzySet(){
        set = new HashMap<>();
    }

    public FuzzySet(Map<ElemType, Double> map){
        set = new HashMap<>(map);
    }

    public static double similarity(FuzzySet<String> set1, FuzzySet<String> set2, Settings.SetSimilarity similarity) {
        if(similarity == Settings.SetSimilarity.Jaccard){
            return jaccardSimilarity(set1, set2);
        }else if (similarity == Settings.SetSimilarity.CosineAmplitude){
            return cosAmpSimilarity(set1, set2);
        }else{
            return -1;
        }
    }

    public static double jaccardSimilarity(FuzzySet<String> set1, FuzzySet<String> set2){
        // TODO: implement - CHECK

        Set<String> keys1 = set1.set.keySet();
        Set<String> keys2 = set2.set.keySet();
        int keys1Size = keys1.size();
        int keys2Size = keys2.size();

        keys1.retainAll(keys2);
        int intersectionSize = keys1.size();

        return 1.0 / (keys1Size + keys2Size - intersectionSize) * intersectionSize;
    }

    public static double cosAmpSimilarity(FuzzySet<String> set1, FuzzySet<String> set2){
        // TODO: implement - CHECK

        Double[] values1 = set1.set.values().toArray(new Double[0]);
        Double[] values2 = set2.set.values().toArray(new Double[0]);

        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < values1.length; i++) {
            dot += values1[i] * values2[i];
            norm1 += Math.pow(values1[i], 2);
            norm2 += Math.pow(values2[i], 2);
        }

        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public Set<ElemType> support() {
        return set.keySet();
    }

    public double cardinality(){
         double card = 0;
         for (Map.Entry<ElemType, Double> entry : set.entrySet()){
             card += entry.getValue();
         }
         return card;
    }

    public Set<Map.Entry<ElemType, Double>> entrySet(){
        return set.entrySet();
    }

    public double contains(ElemType element) {
        return set.getOrDefault(element, 0.);
    }

    public double set(ElemType element, double degree) {
        Double res = set.put(element, degree);
        if(res != null){
            return res;
        }
        return 0;
    }

    public void add(ElemType element, double degree){
        set.put(element, degree);
    }

    public void remove(ElemType element) {
        set.remove(element);
    }

    public void clear() {
        set.clear();
    }
}
