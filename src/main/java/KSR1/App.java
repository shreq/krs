package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class App {
    public static void main(String[] args)
    {
        File file = new File("src/main/resources/reuters/reut2-000.sgm");

        SGM sgm = null;
        try {
            sgm = SGM.parse(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(sgm.articles.get(0).toString());

        List<String> words = sgm.articles.get(0).getWords();
        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
        }
    }
}
