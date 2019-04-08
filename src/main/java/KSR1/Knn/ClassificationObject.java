package KSR1.Knn;

import java.util.ArrayList;
import java.util.List;

public class ClassificationObject {

    private String label;
    public List<Double> values;

    public ClassificationObject() {}

    public ClassificationObject(String label, ArrayList<Double> values) {
        this.label = label;
        this.values = values;
    }

    public ClassificationObject(ArrayList<Double> values) {
        this.values = values;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
