package KSR1;

import KSR1.Preprocessing.LancasterStemmer;
import KSR1.Preprocessing.Stemmer;

import java.io.*;

public class App {
    public static void main(String[] args)
    {
        File file = new File("src/main/resources/reuters/reut2-000.sgm");

        SGMFile sgm = null;
        try {
            sgm = SGMFile.loadFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Article article = sgm.articles.get(0);
        System.out.println(article.toString());
        String[] words = article.text.split("\\s+");
        Stemmer stemmer = new LancasterStemmer();
        for (int i = 0; i < 40; i++) {
            System.out.println(words[i]);
            System.out.println(stemmer.stem(words[i]));
            System.out.println("========");
        }
    }
}
