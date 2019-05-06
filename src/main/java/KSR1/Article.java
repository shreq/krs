package KSR1;

import java.util.*;

public class Article {
    String type;
    String course;

    String title;
    private String text;
    List<String> orgs = new ArrayList<String>();
    List<String> places = new ArrayList<String>();
    private ArrayList<String> wordlist = null;

    private static final Map<Settings.Category, Set<String>> allowedLabels = new HashMap<>();

    static {
        allowedLabels.put(Settings.Category.Places, new HashSet<>(Arrays.asList("usa", "france", "uk", "canada", "japan")));
        allowedLabels.put(Settings.Category.Orgs, new HashSet<>(Arrays.asList("ec", "worldbank", "imf", "opec", "icco")));
        allowedLabels.put(Settings.Category.Course, new HashSet<>(Arrays.asList("dessert", "breakfast", "dinner")));
        allowedLabels.put(Settings.Category.Type, new HashSet<>(Arrays.asList("cake", "salad", "meat", "pasta", "soup")));
    }

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

    public String getTitle() {
        return title;
    }

    public String getLabel(Settings.Category category){
        switch (category){
            case Orgs:
                return orgs.get(0);
            case Places:
                return places.get(0);
            case Type:
                return type;
            case Course:
                return course;
            default:
                throw new RuntimeException("Unknown category");
        }
    }

    public static Set<String> getAllLabels(Settings.Category category) {
        return allowedLabels.get(category);
    }

    public boolean isGood(Settings.Category category){
        if(category == Settings.Category.Orgs && orgs.size() != 1){
            return false;
        }else if(category == Settings.Category.Places && places.size() != 1){
            return false;
        }else if(category == Settings.Category.Course && course == null){
            return false;
        }else if(category == Settings.Category.Type && type == null){
            return false;
        }
        return allowedLabels.get(category).contains(getLabel(category));
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setWordlist(ArrayList<String> wordlist) {
        this.wordlist = wordlist;
        this.text = null;
    }
}
