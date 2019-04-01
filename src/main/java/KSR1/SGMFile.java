package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class SGMFile {

    List<Article> articles = new ArrayList<Article>();

    static SGMFile loadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        SGMFile result = new SGMFile();
        while (true){
            String reuters = scanner.findWithinHorizon("<REUTERS", 10000);
            if(reuters == null || reuters.isEmpty()){
                return result;
            }
            Article article = new Article();
            // parse topics
            scanner.findWithinHorizon("<TOPICS>", 1000);
            while (true){
                scanner.findWithinHorizon("<D>(.+?)</D>|</TOPICS>", 100);
                MatchResult matchResult = scanner.match();
                if(matchResult.groupCount() < 1 || matchResult.group(1) == null)
                {
                    break;
                }
                article.topics.add(matchResult.group(1));
            }
            // parse places
            scanner.findWithinHorizon("<PLACES>", 1000);
            while (true){
                scanner.findWithinHorizon("<D>(.+?)</D>|</PLACES>", 100);
                MatchResult matchResult = scanner.match();
                if(matchResult.groupCount() < 1 || matchResult.group(1) == null)
                {
                    break;
                }
                article.places.add(matchResult.group(1));
            }
            // parse title
            scanner.findWithinHorizon("<TITLE>(.+?)</TITLE>", 100000);
            MatchResult matchResult = scanner.match();
            if(matchResult.groupCount() < 1){
                continue;
            }
            article.title = matchResult.group(1).replace("&lt;", "<");

            // parse body
            Pattern bodyPattern = Pattern.compile("<BODY>(.+?)</BODY>", Pattern.DOTALL);
            scanner.findWithinHorizon(bodyPattern, 10000000);
            matchResult = scanner.match();
            if(matchResult.groupCount() < 1){
                continue;
            }
            article.text = matchResult.group(1).replace("&lt;", "<");
            result.articles.add(article);
        }
    }
}
