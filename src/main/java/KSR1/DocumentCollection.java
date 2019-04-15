package KSR1;

import KSR1.Knn.ClassificationObject;
import KSR1.Preprocessing.Stemmer;
import KSR1.Preprocessing.StopWordFilter;
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
            new HashSet<>(Arrays.asList("west-germany", "usa", "france", "uk", "canada", "japan"));

    // TODO: znaleźć 3-5 najczęściej występujących organizacji i wstawić tu zamiast tych, co są teraz
    public static final HashSet<String> allowedOrgs =
            new HashSet<>(Arrays.asList("ec", "worldbank", "oecd"));

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
        if(category == Settings.Category.Places){
            this.filterPlaces();
        }else if(category == Settings.Category.Orgs){
            this.filterOrgs();
        }
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

    public List<ClassificationObject> extractClassificationObjects(Settings settings, List<FuzzySet<String>> keywordsSets){
        List<ClassificationObject> classificationObjects = new ArrayList<>();
        Function<Article, Map<String, Double>> wordStat;
        switch (settings.trainingMethod){
            case TF:
                wordStat = art -> termFrequency(Collections.singletonList(art));
                break;
            case TFIDF:
                final Map<String, Double> idfs = inverseDocumentFrequency(articles);
                wordStat = (art) -> {
                    Map<String, Double> tfs = termFrequency(Collections.singletonList(art));
                    for(Map.Entry<String, Double> tf : tfs.entrySet()){
                        tfs.replace(tf.getKey(), idfs.get(tf.getKey())*tf.getValue());
                    }
                    return tfs;
                };
                break;
            case ROne:
                // TODO: implement
                throw new RuntimeException("Not implemented");
            default:
                throw new IllegalArgumentException("Invalid training method");
        }

        for(Article article : articles){
            List<Double> values = new ArrayList<>();

            // keyword features
            Map<String, Double> tfs = wordStat.apply(article);
            FuzzySet<String> termSet = new FuzzySet<>(tfs);
            for(FuzzySet<String> keywords : keywordsSets){
                values.add(FuzzySet.similarity(termSet, keywords, settings.setSimilarity));
            }

            // capital letters
            int capLetCount = 0;
            for(String word : article.getWords()){
                if(Character.isUpperCase(word.charAt(0))){
                    capLetCount++;
                }
            }
            values.add((double)capLetCount/article.getWords().size());

            // unique words
            int uniqueCount = new HashSet<>(article.getWords()).size();
            values.add((double)uniqueCount/article.getWords().size());

            // TODO: here we can add more features

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

    /**
     * Make keywords for each label in collection
     * @param settings needed to get extracting method and keywords count
     * @return list of fuzzy sets of keywords
     */
    public List<FuzzySet<String>> makeKeywords(Settings settings) {
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

        Function<List<Article>, Map<String, Double>> wordStat = makeWordStat(settings.trainingMethod);
        return extractKeywords(articlesSets, settings.keywordsCount, wordStat);
    }

    private Function<List<Article>, Map<String, Double>> makeWordStat(Settings.Method trainingMethod) {
        switch (trainingMethod){
            case TF:
                return DocumentCollectionStats::termFrequency;
            case TFIDF:
                return (artList) -> {
                    Map<String, Double> tfs = termFrequency(artList);
                    Map<String, Double> idfs = inverseDocumentFrequency(artList);
                    for(Map.Entry<String, Double> tf : tfs.entrySet()){
                        tfs.replace(tf.getKey(), idfs.get(tf.getKey())*tf.getValue());
                    }
                    return tfs;
                };
            case ROne:
                // TODO: implement
                // https://pdfs.semanticscholar.org/03af/c233b07d0fdfbab169fae5dfd44e7e0fc1b9.pdf
                throw new RuntimeException("i will implement it in a moment");
            default:
                throw new IllegalArgumentException("Invalid training method");
        }
    }

    private List<FuzzySet<String>> extractKeywords(Map<String, List<Article>> articlesSets, int keywordsCount,
                                                          Function<List<Article>, Map<String, Double>> wordStat){
        List<FuzzySet<String>> result = new ArrayList<>();
        for(Map.Entry<String, List<Article>> articleSet : articlesSets.entrySet()){
            FuzzySet<String> keywordsSet = new FuzzySet<>();
            Map<String, Double> tfs = wordStat.apply(articleSet.getValue());
            // sort in order from greatest to smallest term frequency
            SortedSet<Map.Entry<String, Double>> sortedTfs;
            sortedTfs = new TreeSet<>(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
            sortedTfs.addAll(tfs.entrySet());
            // get first N results
            int counter = 0;
            for (Map.Entry<String, Double> entry : sortedTfs) {
                keywordsSet.add(entry.getKey(), entry.getValue());
                counter++;
                if(counter == keywordsCount){
                    break;
                }
            }
            result.add(keywordsSet);
        }
        return result;
    }
}
