package KSR1.Knn;

import KSR1.Processing.Distance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnnClassifier {

    int neighboursCount;
    List<ClassificationObject> set;
    Distance iDistance;

    public KnnClassifier(int neighboursCount, List<ClassificationObject> set, Distance iDistance) {
        this.neighboursCount = neighboursCount;
        this.set = set;
        this.iDistance = iDistance;
    }

    public String classifyObject(ClassificationObject classificationObject) {
        Map<ClassificationObject, Double> disctances = new HashMap<>();

        DistanceMeasure dm = new EuclideanDistance();
        for (ClassificationObject s : set) {
            //double distance = iDistance.compare(classificationObject.values, s.values);
            double distance = dm.compute(
                    classificationObject.values.stream().mapToDouble(d -> d).toArray(),
                    s.values.stream().mapToDouble(d -> d).toArray());
            disctances.put(s, distance);
        }

        Map<String, Long> collector = disctances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .limit(neighboursCount).map(ClassificationObject::getLabel)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return Collections.max(collector.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
