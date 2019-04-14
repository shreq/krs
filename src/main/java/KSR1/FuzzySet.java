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
        // TODO: implement
        return 0;
    }

    public static double cosAmpSimilarity(FuzzySet<String> set1, FuzzySet<String> set2){
        // TODO: implement
        return 0;
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
