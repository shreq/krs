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
                "\n   Topics: " + topics.toString() +
                "\n   Places: " + places.toString() +
                "\n   Title: " + title + "\n)";
    }
}
