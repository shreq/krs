package KSR1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                "\n   Title:  " + title + "\n)";
    }

    public List<String> getTokens() {
        List<String> result = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t,./+&#;");
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }

        return result;
    }

    public List<String> getAllWords() {
        List<String> result = new ArrayList<>();

        for (String token : getTokens()) {
            if (!isNumeric(token) && token.length() > 1) {
                result.add(token);
            }
        }

        return result;
    }

    public List<String> getWords() {
        List<String> stopwords = new ArrayList<>();
        try {
            stopwords = Files.readAllLines(Paths.get("src/main/resources/stopwords.txt"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        List<String> result = new ArrayList<>();

        for (String word : getAllWords()) {
            if (word.length() > 1 && !stopwords.contains(word.toLowerCase())) {
                result.add(word);
            }
        }

        return result;
    }

    public List<String> getUniqueWords() {
        List<String> result = new ArrayList<>();

        for (String word : getWords()) {
            if (!result.contains(word)) {
                result.add(word);
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
