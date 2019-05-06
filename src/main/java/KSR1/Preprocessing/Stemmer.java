package KSR1.Preprocessing;

import org.tartarus.snowball.ext.PorterStemmer;

public class Stemmer {
    private static final PorterStemmer stemmer = new PorterStemmer();

    public String stem(String input){
        stemmer.setCurrent(input);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}