package KSR1.Preprocessing;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Lancaster stemming algorithm implementation.
 * Based on <a href="http://horusiath.blogspot.com/2012/08/nlp-stemming-i-lematyzacja.html">this blog post</a>
 */
public class LancasterStemmer implements Stemmer {
    @Override
    public String stem(String word) {
        boolean isIntact = true;
        boolean continueFlag = true;
        while(continueFlag){
            continueFlag = false;
            char lastLetter = word.charAt(word.length()-1);
            if(!rulesDictionary.containsKey(lastLetter)){
                break;
            }
            for(StemRule rule : rulesDictionary.get(lastLetter)){
                if(!word.endsWith(rule.wordEnd)){
                    continue;
                }
                if(!canBeStemmed(word, rule.removeTotal)){
                    continue;
                }
                if(rule.intact && !isIntact){
                    continue;
                }
                word = word.substring(0, word.length()-rule.removeTotal);
                word = word + rule.appendString;
                isIntact = false;
                if(rule.isContinuous){
                    continueFlag = true;
                }
                break;
            }
        }
        return word;
    }

    private boolean canBeStemmed(String word, int removeTotal){
        int resultLength = word.length() - removeTotal;
        return hasVowelAt(word, 0)
                    ? resultLength >= 2
                    : (resultLength >= 3 && (hasVowelAt(word, 1) || hasVowelAt(word, 2)));
    }

    private boolean hasVowelAt(String word, int index){
        final String vowels = "aeiouyAEIOUY";
        return vowels.contains(word.substring(index, index+1));
    }

    public LancasterStemmer(){
        parseRules();
    }

    private void parseRules(){
        rulesDictionary = new HashMap<>();
        for (int i = 0; i < rulesDescriptions.length; i++) {
            String rule = rulesDescriptions[i];
            char firstLetter = rule.charAt(0);
            if(!rulesDictionary.containsKey(firstLetter)){
                rulesDictionary.put(firstLetter, new LinkedList<>());
            }
            rulesDictionary.get(firstLetter).add(new StemRule(rule));
        }
    }

    private HashMap<Character, LinkedList<StemRule>> rulesDictionary;

    // region stemming rules descriptions
    static final private String[] rulesDescriptions =
            {
                    "'s*2>",     // 's > - if intact
                    "n't*3>",    // n't > - if intact
                    "ai*2.",     // -ia > - if intact
                    "a*1.",      // -a > - if intact
                    "bb1.",      // -bb > -b
                    "city3s.",   // -ytic > -ys
                    "ci2>",      // -ic > -
                    "cn1t>",     // -nc > -nt
                    "dd1.",      // -dd > -d
                    "dei3y>",    // -ied > -y
                    "deec2ss.",  // -ceed >", -cess
                    "dee1.",     // -eed > -ee
                    "de2>",      // -ed > -
                    "dooh4>",    // -hood > -
                    "e1>",       // -e > -
                    "feil1v.",   // -lief > -liev
                    "fi2>",      // -if > -
                    "gni3>",     // -ing > -
                    "gai3y.",    // -iag > -y
                    "ga2>",      // -ag > -
                    "gg1.",      // -gg > -g
                    "ht*2.",     // -th > -   if intact
                    "hsiug5ct.", // -guish > -ct
                    "hsi3>",     // -ish > -
                    "i*1.",      // -i > -    if intact
                    "i1y>",      // -i > -y
                    "ji1d.",     // -ij > -id   --  see nois4j> & vis3j>
                    "juf1s.",    // -fuj > -fus
                    "ju1d.",     // -uj > -ud
                    "jo1d.",     // -oj > -od
                    "jeh1r.",    // -hej > -her
                    "jrev1t.",   // -verj > -vert
                    "jsim2t.",   // -misj > -mit
                    "jn1d.",     // -nj > -nd
                    "j1s.",      // -j > -s
                    "lbaifi6.",  // -ifiabl > -
                    "lbai4y.",   // -iabl > -y
                    "lba3>",     // -abl > -
                    "lbi3.",     // -ibl > -
                    "lib2l>",    // -bil > -bl
                    "lc1.",      // -cl > c
                    "lufi4y.",   // -iful > -y
                    "luf3>",     // -ful > -
                    "lu2.",      // -ul > -
                    "lai3>",     // -ial > -
                    "lau3>",     // -ual > -
                    "la2>",      // -al > -
                    "ll1.",      // -ll > -l
                    "mui3.",     // -ium > -
                    "mu*2.",     // -um > -   if intact
                    "msi3>",     // -ism > -
                    "mm1.",      // -mm > -m
                    "nois4j>",   // -sion > -j
                    "noix4ct.",  // -xion > -ct
                    "noi3>",     // -ion > -
                    "nai3>",     // -ian > -
                    "na2>",      // -an > -
                    "nee0.",     // protect  -een
                    "ne2>",      // -en > -
                    "nn1.",      // -nn > -n
                    "pihs4>",    // -ship > -
                    "pp1.",      // -pp > -p
                    "re2>",      // -er > -
                    "rae0.",     // protect  -ear
                    "ra2.",      // -ar > -
                    "ro2>",      // -or > -
                    "ru2>",      // -ur > -
                    "rr1.",      // -rr > -r
                    "rt1>",      // -tr > -t
                    "rei3y>",    // -ier > -y
                    "sei3y>",    // -ies > -y
                    "sis2.",     // -sis > -s
                    "si2>",      // -is > -
                    "ssen4>",    // -ness > -
                    "ss0.",      // protect  -ss
                    "suo3>",     // -ous > -
                    "su*2.",     // -us > -   if intact
                    "s*1>",      // -s > -    if intact
                    "s0.",       // -s > -s
                    "tacilp4y.", // -plicat > -ply
                    "ta2>",      // -at > -
                    "tnem4>",    // -ment > -
                    "tne3>",     // -ent > -
                    "tna3>",     // -ant > -
                    "tpir2b.",   // -ript > -rib
                    "tpro2b.",   // -orpt > -orb
                    "tcud1.",    // -duct > -duc
                    "tpmus2.",   // -sumpt > -sum
                    "tpec2iv.",  // -cept > -ceiv
                    "tulo2v.",   // -olut > -olv
                    "tsis0.",    // protect  -sist
                    "tsi3>",     // -ist > -
                    "tt1.",      // -tt > -t
                    "uqi3.",     // -iqu > -
                    "ugo1.",     // -ogu > -og
                    "vis3j>",    // -siv > -j
                    "vie0.",     // protect  -eiv
                    "vi2>",      // -iv > -
                    "ylb1>",     // -bly > -bl
                    "yli3y>",    // -ily > -y
                    "ylp0.",     // protect  -ply
                    "yl2>",      // -ly > -
                    "ygo1.",     // -ogy > -og
                    "yhp1.",     // -phy > -ph
                    "ymo1.",     // -omy > -om
                    "ypo1.",     // -opy > -op
                    "yti3>",     // -ity > -
                    "yte3>",     // -ety > -
                    "ytl2.",     // -lty > -l
                    "yrtsi5.",   // -istry > -
                    "yra3>",     // -ary > -
                    "yro3>",     // -ory > -
                    "yfi3.",     // -ify > -
                    "ycn2t>",    // -ncy > -nt
                    "yca3>",     // -acy > -
                    "zi2>",      // -iz > -
                    "zy1s."      // -yz > -ys
            };
    // endregion
}
