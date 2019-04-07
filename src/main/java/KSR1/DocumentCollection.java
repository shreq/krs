package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentCollection {

    private static final Logger LOGGER = Logger.getLogger( DocumentCollection.class.getName() );

    ArrayList<Article> articles;

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
}
