package KSR1.Knn;

import java.util.ArrayList;
import java.util.List;

public class ClassificationObject {

    private List<String> labels;
    public List<Double> values;

    public ClassificationObject() {}

    public ClassificationObject(List<String> labels, ArrayList<Double> values) {
        this.labels = labels;
        this.values = values;
    }

    public ClassificationObject(ArrayList<Double> values) {
        this.values = values;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
