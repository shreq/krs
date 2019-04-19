package KSR1;

import KSR1.Knn.ClassificationObject;
import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;
import KSR1.Processing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentCollection {

    private static final Logger LOGGER = Logger.getLogger( DocumentCollection.class.getName() );

    List<Article> articles;

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
        articles.removeIf(article -> !article.isGood(category));
    }

    public DocumentCollection splitGetSubset(double subsetSizePercent){
        DocumentCollection subCollection = new DocumentCollection();
        int size = articles.size();
        int count = (int)Math.round(subsetSizePercent*0.01*size);
        subCollection.articles = new ArrayList<>(articles.subList(size - count, size));
        this.articles.subList(size - count, size).clear();
        return subCollection;
    }

    public List<ClassificationObject> extractClassificationObjects(Settings settings, List<FeatureExtractor> featureExtractors){
        List<ClassificationObject> classificationObjects = new ArrayList<>();

        for(Article article : articles){
            List<Double> values = new ArrayList<>();

            for(FeatureExtractor extractor : featureExtractors){
                values.addAll(extractor.extract(article));
            }

            // save features
            ClassificationObject cObj = new ClassificationObject();
            cObj.values = values;
            cObj.setLabel(article.getLabel(settings.category));
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
            String label = article.getLabel(settings.category);
            if(!articlesSets.containsKey(label)){
                articlesSets.put(label, new ArrayList<>());
            }
            articlesSets.get(label).add(article);
        }

        return new KeywordExtractor(articlesSets.values(), settings.trainingMethod);
    }
}
