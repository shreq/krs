package KSR1;

import java.util.ArrayList;
import java.util.List;

public class Article {
    String title;
    String text;
    List<String> topics = new ArrayList<String>();
    List<String> places = new ArrayList<String>();

    @Override
    public String toString() {
        return "Article(" +
                "\n  topics=" + topics.toString() +
                "\n  places=" + places.toString() +
                "\n  title=" + title + "\n)";
    }
}
