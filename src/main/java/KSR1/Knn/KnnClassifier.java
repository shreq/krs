package KSR1.Knn;

import KSR1.Processing.Distance;
import jdk.jshell.spi.ExecutionControl;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnnClassifier {

    int neighboursCount;
    List<ClassificationObject> set;
    Distance customDistance = null;
    DistanceMeasure libDistance = null;

    public KnnClassifier(int neighboursCount, List<ClassificationObject> set, Distance customDistance) throws ExecutionControl.NotImplementedException {
        this.neighboursCount = neighboursCount;
        this.set = set;
        this.customDistance = customDistance;
        throw new ExecutionControl.NotImplementedException("");
    }

    public KnnClassifier(int neighboursCount, List<ClassificationObject> set, DistanceMeasure libDistance) {
        this.neighboursCount = neighboursCount;
        this.set = set;
        this.libDistance = libDistance;
        // https://commons.apache.org/proper/commons-math/javadocs/api-3.4/org/apache/commons/math3/ml/distance/DistanceMeasure.html
    }

    public String classifyObject(ClassificationObject classificationObject) {
        Map<ClassificationObject, Double> distances = new HashMap<>();

        for (ClassificationObject s : set) {
            // TODO: add case for customDistance?

            double distance = libDistance.compute(
                    classificationObject.values.stream().mapToDouble(d -> d).toArray(),
                    s.values.stream().mapToDouble(d -> d).toArray());
            distances.put(s, distance);
        }

        Map<String, Long> collector = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .limit(neighboursCount).map(ClassificationObject::getLabel)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(collector.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
