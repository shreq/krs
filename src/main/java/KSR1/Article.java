package KSR1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article {
    String title;
    String text;
    List<String> topics = new ArrayList<String>();
    List<String> places = new ArrayList<String>();

    /**
     * Get all words from text. First word in sentence is converted to lower case.
     * @return list of words
     */
    ArrayList<String> getWords(){
        ArrayList<String> textArray = new ArrayList<>(Arrays.asList(text.split(",?\\s+|,")));
        String previous = textArray.get(0);
        textArray.set(0, stripRight(previous.toLowerCase()));

        for(int i=1; i < textArray.size(); i++){
            String current = textArray.get(i);
            if(previous.endsWith(".")){
                current = current.toLowerCase();
            }
            textArray.set(i, stripRight(current));
            previous = current;
        }
        textArray.removeIf(this::isNumeric);
        return textArray;
    }

    private boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private String stripRight(String word){
        if(word.endsWith(".")){
            return word.substring(0, word.length()-1);
        }
        return word;
    }

    @Override
    public String toString() {
        return "Article(" +
                "\n  topics=" + topics.toString() +
                "\n  places=" + places.toString() +
                "\n  title=" + title + "\n)";
    }
}
