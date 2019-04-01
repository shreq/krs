package KSR1;

import KSR1.Preprocessing.LancasterStemmer;
import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class App {
    public static void main(String[] args)
    {
        File file = new File("src/main/resources/reuters/reut2-000.sgm");

        SGMFile sgm;
        try {
            sgm = SGMFile.loadFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Article article = sgm.articles.get(0);
        System.out.println(article.toString());
        ArrayList<String> words = article.getWords();
        words.removeIf(StopWordFilter::filter);
        Stemmer stemmer = new LancasterStemmer();
        for (int i = 0; i < 40; i++) {
            System.out.println(words.get(i));
            System.out.println(stemmer.stem(words.get(i)));
            System.out.println("========");
        }
    }
}
