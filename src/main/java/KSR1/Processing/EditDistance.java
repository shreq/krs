package KSR1.Processing;

/**
 * Similarity based on edit distance, also known as Levenshtein distance, which is rescaled to [0, 1] range
 */
public class EditDistance implements Similarity {

    @Override
    public double compare(String a, String b) {
        int lA = a.length()+1;
        int lB = b.length()+1;
        int d[][] = new int[lA][lB];
        for(int i=0; i<lB; i++){
            d[0][i] = i;
        }
        for(int i=1; i<lA; i++){
            d[i][0] = d[i-1][0] + 1;
            for(int j=1; j<lB; j++){
                d[i][j] = Integer.min(d[i][j-1], d[i-1][j]) + 1;
                if(a.charAt(i-1) == b.charAt(j-1)){
                    d[i][j] = Integer.min(d[i-1][j-1], d[i][j]);
                }else{
                    d[i][j] = Integer.min(d[i-1][j-1] + 1, d[i][j]);
                }
            }
        }
        return 1. - ((double) d[lA-1][lB-1] / (Integer.max(lA, lB) - 1));
    }
}
