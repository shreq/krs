package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class SGMFile {

    public List<Article> articles = new ArrayList<Article>();
    private static final Pattern bodyPattern = Pattern.compile("<BODY>(.+?)</BODY>", Pattern.DOTALL);
    private static final Pattern titlePattern = Pattern.compile("<TITLE>(.+?)</TITLE>", Pattern.DOTALL);
    private static final Pattern placesPattern = Pattern.compile("<D>(.+?)</D>|</PLACES>", Pattern.DOTALL);
    private static final Pattern topicsPattern = Pattern.compile("<D>(.+?)</D>|</TOPICS>", Pattern.DOTALL);
    private static final Logger LOGGER = Logger.getLogger(SGMFile.class.getName());

    static public SGMFile loadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        SGMFile result = new SGMFile();
        while (true){
            try {
                String reuters = scanner.findWithinHorizon("<REUTERS", 0);
                if (reuters == null || reuters.isEmpty()) {
                    return result;
                }
                Article article = new Article();
                // region parse topics
                scanner.findWithinHorizon("<TOPICS>", 0);
                while (true) {
                    scanner.findWithinHorizon(topicsPattern, 0);
                    MatchResult matchResult = scanner.match();
                    if (matchResult.groupCount() < 1 || matchResult.group(1) == null) {
                        break;
                    }
                    article.topics.add(matchResult.group(1));
                }
                // endregion
                // region parse places
                scanner.findWithinHorizon("<PLACES>", 0);
                while (true) {
                    scanner.findWithinHorizon(placesPattern, 0);
                    MatchResult matchResult = scanner.match();
                    if (matchResult.groupCount() < 1 || matchResult.group(1) == null) {
                        break;
                    }
                    article.places.add(matchResult.group(1));
                }
                // endregion
                // region parse title
                scanner.findWithinHorizon(titlePattern, 0);
                MatchResult matchResult = scanner.match();
                if (matchResult.groupCount() < 1) {
                    continue;
                }
                article.title = stripXMLentities(matchResult.group(1));
                // endregion
                // region parse body
                scanner.findWithinHorizon(bodyPattern, 0);
                matchResult = scanner.match();
                if (matchResult.groupCount() < 1) {
                    continue;
                }
                article.setText(stripXMLentities(matchResult.group(1)));
                // endregion
                result.articles.add(article);
            } catch (IllegalStateException ex){
                LOGGER.log(Level.INFO, "Invalid article in file {0}", file.getName());
            }
        }
    }

    static private String stripXMLentities(String text){
        return text.replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&amp;", "&")
                   .replace("&apos;", "'")
                   .replace("&quot;", "\"")
                   .replace('\n', ' ')
                   .replaceAll("&#\\d\\d?;", "");
    }
}
