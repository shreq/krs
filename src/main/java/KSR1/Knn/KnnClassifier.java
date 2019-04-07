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
    // https://commons.apache.org/proper/commons-math/javadocs/api-3.4/org/apache/commons/math3/ml/distance/DistanceMeasure.html
    DistanceMeasure distance = null;

    public KnnClassifier(int neighboursCount, List<ClassificationObject> dataset, DistanceMeasure distance) {
        this.neighboursCount = neighboursCount;
        this.dataset = dataset;
        this.distance = distance;
    }

    public String classifyObject(ClassificationObject classificationObject) {
        Map<ClassificationObject, Double> distances = new HashMap<>();

        for (ClassificationObject s : dataset) {
            double distance = this.distance.compute(
                    classificationObject.values.stream().mapToDouble(d -> d).toArray(),
                    s.values.stream().mapToDouble(d -> d).toArray());
            distances.put(s, distance);
        }

        Map<String, Long> collector = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .limit(neighboursCount).map(ClassificationObject::getLabels)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(collector.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
