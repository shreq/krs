package KSR1.Processing;

/**
 * Generalized bounded n-gram.
 * Similarity is based on counting all identical n-grams where n ranges from min to max.
 */
public class GenBoundNGram implements Similarity {

    public GenBoundNGram(int min, int max){
        this.min = min;
        this.max = max;
        this.nGram = new NGram(-1);
    }

    @Override
    public double compare(String a, String b) {
        int nGramsCount = 0;
        int matchingCount = 0;
        for(int n = min; n <= max; n++){
            nGram.setN(n);
            nGramsCount += nGram.countNGrams(a, b);
            matchingCount += nGram.countMatchingNGrams(a, b);
        }
        return (double)matchingCount/nGramsCount;
    }

    public void setLimits(int min, int max){
        this.min = min;
        this.max = max;
    }

    private int min;
    private int max;
    private NGram nGram;
}
