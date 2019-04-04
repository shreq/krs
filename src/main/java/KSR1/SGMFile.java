package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class SGMFile {

    public List<Article> articles = new ArrayList<Article>();

    static public SGMFile loadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        SGMFile result = new SGMFile();
        while (true){
            String reuters = scanner.findWithinHorizon("<REUTERS", 10000);
            if(reuters == null || reuters.isEmpty()){
                return result;
            }
            Article article = new Article();
            // region parse topics
            scanner.findWithinHorizon("<TOPICS>", 1000);
            while (true){
                scanner.findWithinHorizon("<D>(.+?)</D>|</TOPICS>", 100);
                MatchResult matchResult = scanner.match();
                if(matchResult.groupCount() < 1 || matchResult.group(1) == null) {
                    break;
                }
                article.topics.add(matchResult.group(1));
            }
            // endregion
            // region parse places
            scanner.findWithinHorizon("<PLACES>", 1000);
            while (true){
                scanner.findWithinHorizon("<D>(.+?)</D>|</PLACES>", 100);
                MatchResult matchResult = scanner.match();
                if(matchResult.groupCount() < 1 || matchResult.group(1) == null) {
                    break;
                }
                article.places.add(matchResult.group(1));
            }
            // endregion
            // region parse title
            scanner.findWithinHorizon("<TITLE>(.+?)</TITLE>", 100000);
            MatchResult matchResult = scanner.match();
            if(matchResult.groupCount() < 1){
                continue;
            }
            article.title = stripXMLentities(matchResult.group(1));
            // endregion
            // region parse body
            Pattern bodyPattern = Pattern.compile("<BODY>(.+?)</BODY>", Pattern.DOTALL);
            scanner.findWithinHorizon(bodyPattern, 10000000);
            matchResult = scanner.match();
            if(matchResult.groupCount() < 1){
                continue;
            }
            article.text = stripXMLentities(matchResult.group(1));
            // endregion
            result.articles.add(article);
        }
    }

    static private String stripXMLentities(String text){
        return text.replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&amp;", "&")
                   .replace("&apos;", "'")
                   .replace("&quot;", "\"")
                   .replace("&#3;", ""); // end of text character
    }
}
