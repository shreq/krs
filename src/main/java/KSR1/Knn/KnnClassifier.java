package KSR1.Knn;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnnClassifier {

    int neighboursCount;
    List<ClassificationObject> dataset;
    DistanceMeasure distance = null;
    HashMap<String, Integer> labelFrequency;

    public KnnClassifier(int neighboursCount, List<ClassificationObject> dataset, DistanceMeasure distance) {
        this.neighboursCount = neighboursCount;
        this.dataset = dataset;
        this.distance = distance;
        this.labelFrequency = new HashMap<>();
        for(ClassificationObject object : this.dataset){
            labelFrequency.put(object.getLabel(), labelFrequency.getOrDefault(object.getLabel(), 0) + 1);
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
        Map<String, Double> results = new HashMap<>();
        for(Map.Entry<String, Long> result : collected.entrySet()){
            results.put(result.getKey(), result.getValue().doubleValue()/labelFrequency.get(result.getKey()));
        }

        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
