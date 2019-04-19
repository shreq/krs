package KSR1.Processing;

import KSR1.Article;

import java.util.Collections;
import java.util.List;

public class CapitalLetterExtractor implements FeatureExtractor {

    @Override
    public List<Double> extract(Article article) {
        int capLetCount = 0;
        for(String word : article.getWords()){
            if(Character.isUpperCase(word.charAt(0))){
                capLetCount++;
            }
        }
        return Collections.singletonList((double)capLetCount/article.getWords().size());
    }
}
