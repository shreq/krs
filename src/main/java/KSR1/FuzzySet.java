package KSR1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FuzzySet<ElemType> {

    private HashMap<ElemType, Double> set;

    public FuzzySet(){
        set = new HashMap<>();
    }

    public FuzzySet(Map<ElemType, Double> map){
        set = new HashMap<>(map);
    }

    public FuzzySet(FuzzySet<ElemType> set){
        this.set = (HashMap<ElemType, Double>) set.set.clone();
    }

    public static <T> double similarity(FuzzySet<T> set1, FuzzySet<T> set2, Settings.SetSimilarity similarity) {
        if(similarity == Settings.SetSimilarity.Jaccard){
            return jaccardSimilarity(set1, set2);
        }else if (similarity == Settings.SetSimilarity.CosineAmplitude){
            return cosAmpSimilarity(set1, set2);
        }else{
            return -1;
        }
    }

    public static <T> double jaccardSimilarity(FuzzySet<T> set1, FuzzySet<T> set2){
        double intersectionCard = FuzzySet.intersection(set1, set2).cardinality();
        double sumCard = FuzzySet.sum(set1, set2).cardinality();
        return intersectionCard/sumCard;
    }

public static <T> double cosAmpSimilarity(FuzzySet<T> set1, FuzzySet<T> set2){
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

    public static <T> FuzzySet<T> intersection(FuzzySet<T> set1, FuzzySet<T> set2){
        FuzzySet<T> result = new FuzzySet<T>();
        for(Map.Entry<T, Double> entry : set1.entrySet()){
            if (set2.set.containsKey(entry.getKey())){
                result.set.put(entry.getKey(), Double.min(entry.getValue(), set2.set.get(entry.getKey())));
            }
        }
        return result;
    }

    public static <T> FuzzySet<T> sum(FuzzySet<T> set1, FuzzySet<T> set2){
        FuzzySet<T> result = new FuzzySet<T>(set1);
        for(Map.Entry<T, Double> entry : set2.entrySet()){
            if (result.set.containsKey(entry.getKey())){
                result.set.put(entry.getKey(), Double.max(entry.getValue(), result.set.get(entry.getKey())));
            }else{
                result.set.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
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
