package KSR1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class SGMFile {

    public List<Article> articles = new ArrayList<>();
    private static final Pattern articlePattern = Pattern.compile("<REUTERS.*?>(.*?)</REUTERS>", Pattern.DOTALL);
    private static final Pattern bodyPattern = Pattern.compile("<BODY>(.+?)</BODY>", Pattern.DOTALL);
    private static final Pattern titlePattern = Pattern.compile("<TITLE>(.+?)</TITLE>", Pattern.DOTALL);
    private static final Pattern orgsPattern = Pattern.compile("<ORGS>((<D>.+?</D>)*)</ORGS>", Pattern.DOTALL);
    private static final Pattern placesPattern = Pattern.compile("<PLACES>((<D>.+?</D>)*)</PLACES>", Pattern.DOTALL);
    private static final Logger LOGGER = Logger.getLogger(SGMFile.class.getName());

    static public SGMFile loadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        SGMFile result = new SGMFile();
        while (true){
            try {
                String reuters = scanner.findWithinHorizon(articlePattern, 0);
                if (reuters == null || reuters.isEmpty()) {
                    return result;
                }
                MatchResult matchResult = scanner.match();
                if (matchResult.groupCount() < 1 || matchResult.group(1) == null) {
                    continue;
                }
                String articleString = stripXMLentities(matchResult.group(1));
                Article article = new Article();

                article.orgs.addAll(getList(articleString, orgsPattern));
                article.places.addAll(getList(articleString, placesPattern));

                // region parse title
                Matcher matcher = titlePattern.matcher(articleString);
                if(!matcher.find() || matcher.groupCount() != 1 || matcher.group(1) == null){
                    continue;
                }
                article.title = matcher.group(1).replaceAll("[<>]", "");
                // endregion
                // region parse body
                matcher = bodyPattern.matcher(articleString);
                if(!matcher.find() || matcher.groupCount() != 1 || matcher.group(1) == null){
                    continue;
                }
                article.setText(matcher.group(1).replace("\"", "").replaceAll("[<>]", ""));
                // endregion
                result.articles.add(article);
            } catch (IllegalStateException ex){
                LOGGER.log(Level.INFO, "Invalid article in file {0}", file.getName());
                LOGGER.log(Level.INFO, "Reason {0}", ex.getMessage());
            }
        }
    }

    static private ArrayList<String> getList(String text, Pattern pattern){
        Matcher matcher = pattern.matcher(text);
        if(!matcher.find() || matcher.groupCount() != 2 || matcher.group(1) == null || matcher.group(1).isEmpty()){
            return new ArrayList<>();
        }
        String result = matcher.group(1);
        result = result.substring(3, result.length()-4);
        return new ArrayList<>(Arrays.asList(result.split("</D><D>")));
    }

    static public String stripXMLentities(String text){
        return text.replaceAll("&lt;(.*?)&gt;", "$1")
                .replace("&amp;", "&")
                .replace("&apos;", "'")
                .replaceAll("&#\\d+;|&[a-z]{2,4};|' | '|\\.\\.\\.|&quot;|[-;:+&$()%^*]", " ")
                .replaceAll("[?!.]", ". ")
                .replaceAll("\\d+[a-zA-Z]+|[a-zA-Z]\\d+", " ")
                .replaceAll("\\s+", " ");
    }
}
