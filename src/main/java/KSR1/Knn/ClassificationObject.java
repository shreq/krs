package KSR1.Knn;

import java.util.ArrayList;

public class ClassificationObject {

    private String label;
    public ArrayList<Double> values;

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
}
