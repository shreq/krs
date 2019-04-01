package KSR1.Preprocessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container for keeping stemming rule attributes in easily accessible form
 */
public class StemRule {

    public StemRule(String rule){
        Matcher matcher = ruleValidator.matcher(rule);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Provided rule could not be parsed" + rule);
        }

        String chars = matcher.group(1);
        wordEnd = new StringBuilder(chars).reverse().toString();
        intact = !matcher.group(2).isEmpty();
        removeTotal = Integer.parseInt(matcher.group(3));
        appendString = matcher.group(4);
        isContinuous = !matcher.group(5).equals(".") || matcher.group(5).equals(">");
    }

    /**
     * Suffix of word to be changed
     */
    String wordEnd;

    /**
     * False if we can process word that has been processed before, true otherwise
     */
    boolean intact;

    /**
     * Number of characters to be removed from end of word
     */
    int removeTotal;

    /**
     * String to be appended as new suffix after removal
     */
    String appendString;

    /**
     * Should the stemming be continued after applying this rule
     */
    boolean isContinuous;

    private static Pattern ruleValidator = Pattern.compile("^([a-z]+)(\\*?)(\\d)([a-z]*)([>.]?)$");
}
