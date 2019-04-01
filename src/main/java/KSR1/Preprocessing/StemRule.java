package KSR1.Preprocessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StemRule {

    private static Pattern ruleValidator = Pattern.compile("^([a-z]+)(\\*?)(\\d)([a-z]*)([>.]?)$");
    String wordEnd;
    boolean intact;
    int removeTotal;
    String appendString;
    boolean isContinuous;

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
}
