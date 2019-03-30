package KSR1;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

    public List<String> getWords() {
        List<String> result = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t,.");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (!isNumeric(token)) {
                result.add(token);
            }
        }

        return result;
    }

    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
        }
        catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
