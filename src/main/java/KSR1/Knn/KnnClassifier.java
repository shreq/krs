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

    public KnnClassifier(int neighboursCount, List<ClassificationObject> dataset, DistanceMeasure distance) {
        this.neighboursCount = neighboursCount;
        this.dataset = dataset;
        this.distance = distance;
    }

    public String classifyObject(ClassificationObject classificationObject) {
        Map<ClassificationObject, Double> distances = new HashMap<>();

        for (ClassificationObject object : dataset) {
            double distance = this.distance.compute(
                    classificationObject.values.stream().mapToDouble(d -> d).toArray(),
                    object.values.stream().mapToDouble(d -> d).toArray());
            distances.put(object, distance);
        }

        Map<String, Long> collector =
                distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .limit(neighboursCount).map(ClassificationObject::getLabel)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(collector.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
