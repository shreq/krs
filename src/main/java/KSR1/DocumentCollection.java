package KSR1;

import KSR1.Knn.ClassificationObject;
import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;
import KSR1.Processing.CapitalLetterExtractor;
import KSR1.Processing.KeywordExtractor;
import KSR1.Processing.UniqueWordsextractor;
import KSR1.Processing.WordLengthExtractor;
import KSR1.Statistics.DocumentCollectionStats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static KSR1.Statistics.DocumentCollectionStats.*;

public class DocumentCollection {

    private static final Logger LOGGER = Logger.getLogger( DocumentCollection.class.getName() );

    List<Article> articles;

    public static final HashSet<String> allowedPlaces =
            new HashSet<>(Arrays.asList("usa", "france", "uk", "canada", "japan"));

    public static final HashSet<String> allowedOrgs =
            new HashSet<>(Arrays.asList("ec", "worldbank", "imf", "opec", "icco"));

    private DocumentCollection(){
        articles = new ArrayList<>();
    }

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

    public void filterCategory(Settings.Category category){
        switch(category){
            case Places:
                this.filterPlaces();
                break;
            case Orgs:
                this.filterOrgs();
                break;
            case Course:
                this.filterCourse();
                break;
            case Type:
                this.filterType();
                break;
        }
    }

    public void filterCourse(){
        articles.removeIf(article -> article.getPlaces().size() != 1 || !allowedPlaces.contains(article.getPlaces().get(0)));
    }

    public void filterType(){
        articles.removeIf(article -> article.getPlaces().size() != 1 || !allowedPlaces.contains(article.getPlaces().get(0)));
    }

    public void filterPlaces(){
        articles.removeIf(article -> article.getPlaces().size() != 1 || !allowedPlaces.contains(article.getPlaces().get(0)));
    }

    public void filterOrgs(){
        articles.removeIf(article -> article.getOrgs().size() != 1 || !allowedOrgs.contains(article.getOrgs().get(0)));
    }

    public DocumentCollection splitGetSubset(double subsetSizePercent){
        DocumentCollection subCollection = new DocumentCollection();
        int size = articles.size();
        int count = (int)Math.round(subsetSizePercent*0.01*size);
        subCollection.articles = new ArrayList<>(articles.subList(size - count, size));
        this.articles.subList(size - count, size).clear();
        return subCollection;
    }

    public List<ClassificationObject> extractClassificationObjects(Settings settings, KeywordExtractor kwExtractor, WordLengthExtractor wlExtractor){
        List<ClassificationObject> classificationObjects = new ArrayList<>();

        for(Article article : articles){
            List<Double> values = new ArrayList<>();

            // keyword features
            values.addAll(kwExtractor.extract(article));

            // capital letters
            CapitalLetterExtractor clExtractor = new CapitalLetterExtractor();
            values.addAll(clExtractor.extract(article));

            // unique words
            UniqueWordsextractor uwExtractor = new UniqueWordsextractor();
            values.addAll(uwExtractor.extract(article));

            // long words
            values.addAll(wlExtractor.extract(article));

            // save features
            ClassificationObject cObj = new ClassificationObject();
            cObj.values = values;
            if(settings.category == Settings.Category.Orgs){
                cObj.setLabel(article.getOrgs().get(0));
            }else if(settings.category == Settings.Category.Places) {
                cObj.setLabel(article.getPlaces().get(0));
            }
            classificationObjects.add(cObj);
        }
        return classificationObjects;
    }

    public WordLengthExtractor makeWordLengthExtractor(){
        List<Integer> lengths = new ArrayList<>();
        for(Article article : articles){
            for(String word : article.getWords()){
                int len = word.length();
                lengths.add(len);
            }
        }
        lengths.sort(Integer::compareTo);
        return new WordLengthExtractor(lengths.get(Math.round(0.8f*lengths.size())));
    }

    public KeywordExtractor makeKeywordExtractor(Settings settings) {
        Map<String, List<Article>> articlesSets = new HashMap<>();
        for(Article article : this.articles){
            if(settings.category == Settings.Category.Places){
                if(!articlesSets.containsKey(article.places.get(0))){
                    articlesSets.put(article.places.get(0), new ArrayList<>());
                }
                articlesSets.get(article.places.get(0)).add(article);
            }else if(settings.category == Settings.Category.Orgs){
                if(!articlesSets.containsKey(article.orgs.get(0))){
                    articlesSets.put(article.orgs.get(0), new ArrayList<>());
                }
                articlesSets.get(article.orgs.get(0)).add(article);
            }
        }

        return new KeywordExtractor(articlesSets.values(), settings.trainingMethod);
    }
}
