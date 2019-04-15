package KSR1.Knn;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnnClassifier {

    int neighboursCount;
    List<ClassificationObject> dataset;
    DistanceMeasure distance = null;
    HashMap<String, Integer> labelCount;

    public KnnClassifier(int neighboursCount, List<ClassificationObject> dataset, DistanceMeasure distance) {
        this.neighboursCount = neighboursCount;
        this.distance = distance;
        this.labelCount = new HashMap<>();
        this.dataset = new ArrayList<>();
        long min = Collections.min(dataset.stream().collect(Collectors.groupingBy(ClassificationObject::getLabel, Collectors.counting())).values());
        for(ClassificationObject object : dataset){
            int count = labelCount.getOrDefault(object.getLabel(), 0);
            if(count < min){
                labelCount.put(object.getLabel(), count + 1);
                this.dataset.add(object);
            }
        }
    }

    public String classifyObject(ClassificationObject classificationObject) {
        Map<ClassificationObject, Double> distances = new HashMap<>();

        for (ClassificationObject object : dataset) {
            double distance = this.distance.compute(
                    classificationObject.values.stream().mapToDouble(d -> d).toArray(),
                    object.values.stream().mapToDouble(d -> d).toArray());
            distances.put(object, distance);
        }

        Map<String, Long> collected =
                distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .limit(neighboursCount).map(ClassificationObject::getLabel)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(collected.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
