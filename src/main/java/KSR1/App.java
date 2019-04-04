package KSR1;

import KSR1.Preprocessing.LancasterStemmer;
import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;
import KSR1.Processing.*;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class App {
    public static void main(String[] args)
    {
        System.out.println("Read articles test:");
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

        System.out.println("\nStemmer test:");
        ArrayList<String> words = article.getWords();
        words.removeIf(StopWordFilter::filter);
        Stemmer stemmer = new LancasterStemmer();
        for (int i = 0; i < 10; i++) {
            System.out.println(words.get(i));
            System.out.println(stemmer.stem(words.get(i)));
            System.out.println("========");
        }

        System.out.println("\nSimilarity test:");
        String wordA = "PROGRAMMER", wordB = "PROGRAMMING";
        Similarity similarity = new NGram(3);
        System.out.println("n-gram, n=3: " + similarity.compare(wordA, wordB));
        similarity = new GenBoundNGram(2, 5);
        System.out.println("generalized bound n-gram, n=[2..5]: " + similarity.compare(wordA, wordB));
    }
}
