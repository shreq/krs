package KSR1;

import java.util.*;
import java.util.stream.Collectors;

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

    public static <T> double jaccardSimilarity(FuzzySet<T> set1, FuzzySet<T> set2){
        double intersectionCard = FuzzySet.intersection(set1, set2).cardinality();
        double sumCard = FuzzySet.sum(set1, set2).cardinality();
        return intersectionCard/sumCard;
    }

    public static <T> double cosAmpSimilarity(FuzzySet<T> set1, FuzzySet<T> set2){
        List<Double> values1 = new ArrayList<>(set1.set.values());
        List<Double> values2 = new ArrayList<>(set2.set.values());

        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        int size = Integer.min(values1.size(), values2.size());
        for (int i = 0; i < size; i++) {
            dot += values1.get(i) * values2.get(i);
            norm1 += Math.pow(values1.get(i), 2);
            norm2 += Math.pow(values2.get(i), 2);
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

    public Set<ElemType> universe() {
        return set.keySet();
    }

    public Set<ElemType> support() {
        return set.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toSet());
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

    public FuzzySet<ElemType> persistAllCopy(Collection<ElemType> set) {
        FuzzySet<ElemType> result = new FuzzySet<>();
        for(ElemType elem : set){
            if(this.set.containsKey(elem)){
                result.set.put(elem, this.set.get(elem));
            }
        }
        return result;
    }

    public Collection<Double> values() {
        return set.values();
    }
}
