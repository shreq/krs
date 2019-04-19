package KSR.Knn;

import KSR1.Knn.ClassificationObject;
import KSR1.Knn.KnnClassifier;
import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class KnnClassifierTest {

    @Test
    public void shouldClassifyProperly() {
        ArrayList<Double> a = new ArrayList<>(Arrays.asList(1.0, 1.0));
        ArrayList<Double> b = new ArrayList<>(Arrays.asList(1.0, -1.0));
        ArrayList<Double> c = new ArrayList<>(Arrays.asList(-1.0, -1.0));
        ArrayList<Double> x = new ArrayList<>(Arrays.asList(2.0, 2.0));

        ClassificationObject ao = new ClassificationObject("a", a);
        ClassificationObject bo = new ClassificationObject("b", b);
        ClassificationObject co = new ClassificationObject("b", c);
        ClassificationObject xo = new ClassificationObject(x);

        KnnClassifier classifier = new KnnClassifier(2, Arrays.asList(ao, bo, co), new ChebyshevDistance());

        String result = classifier.classifyObject(xo);
        assertThat(result).isEqualTo("a");
    }
}
