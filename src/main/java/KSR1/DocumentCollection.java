package KSR1;

import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentCollection {

    private static final Logger LOGGER = Logger.getLogger( DocumentCollection.class.getName() );

    ArrayList<Article> articles;

    public static final HashSet<String> allowedPlaces =
            new HashSet<>(Arrays.asList("west-germany", "usa", "france", "uk", "canada", "japan"));

    public static final HashSet<String> allowedOrgs =
            new HashSet<>(Arrays.asList("ec", "worldbank", "oecd"));

    public DocumentCollection(List<String> filePaths) throws FileNotFoundException {
        articles = new ArrayList<>();
        for(String path : filePaths){
            File file = new File(path);

            SGMFile sgm;
            try {
                sgm = SGMFile.loadFile(file);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "File {0} not found", file);
                throw ex;
            }
            articles.addAll(sgm.articles);
        }
    }

    public void preprocess(Stemmer stemmer){
        for (int i=0; i<articles.size(); i++){
            ArrayList<String> wordlist = new ArrayList<>();
            Article article = articles.get(i);
            for (String word : article.getWords()){
                if(StopWordFilter.isValidWord(word)){
                    wordlist.add(stemmer.stem(word));
                }
            }
            article.setWordlist(wordlist);
            articles.set(i, article);
        }
    }

    public void filterPlaces(){
        articles.removeIf(article -> article.getPlaces().size() != 1 || !allowedPlaces.contains(article.getPlaces().get(0)));
    }

    public void filterOrgs(){
        articles.removeIf(article -> article.getOrgs().size() != 1 || !allowedPlaces.contains(article.getOrgs().get(0)));
    }
}
