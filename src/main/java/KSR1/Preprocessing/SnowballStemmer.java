package KSR1.Preprocessing;

import org.tartarus.snowball.ext.PorterStemmer;

public class SnowballStemmer implements Stemmer {
    private static final PorterStemmer stemmer = new PorterStemmer();

    @Override
    public String stem(String input){
        stemmer.setCurrent(input);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}