package KSR1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article {
    String title;
    private String text;
    List<String> orgs = new ArrayList<String>();
    List<String> places = new ArrayList<String>();
    private ArrayList<String> wordlist = null;
    /**
     * Get all words from text. First word in sentence is converted to lower case.
     * @return list of words
     */
    public ArrayList<String> getWords(){
        if(wordlist != null){
            return wordlist;
        }
        ArrayList<String> textArray = new ArrayList<>(Arrays.asList(text.split(",?\\s+|,|/")));
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
        textArray.removeIf(String::isEmpty);
        wordlist = textArray;
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
                "orgs=" + orgs.toString() +
                ", places=" + places.toString() +
                ", title=" + title + ")";
    }

    public String getTitle() {
        return title;
    }

    public List<String> getOrgs() {
        return orgs;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setWordlist(ArrayList<String> wordlist) {
        this.wordlist = wordlist;
        this.text = null;
    }
}
