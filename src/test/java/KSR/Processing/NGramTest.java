package KSR.Processing;

import KSR1.Processing.NGram;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NGramTest {

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("Should return 1 on equal strings")
    void shouldReturnOne(int n){
        //given
        NGram nGram = new NGram(n);
        String A = "Lorem ipsum 123";
        String B = "Lorem ipsum 123";
        //when
        double similarity = nGram.compare(A, B);
        //then
        assertEquals(1, similarity);
    }


    @ParameterizedTest
    @MethodSource("getCases")
    @DisplayName("Should return correct answer")
    void shouldReturnAnswer(String word1, String word2, int n, double expected){
        //given
        NGram nGram = new NGram(n);
        //when
        double similarity = nGram.compare(word1, word2);
        //then
        assertEquals(expected, similarity);
    }

    private static Object[] getCases(){
        return $($("abc", "abcd", 1, 0.75),
                 $("abc", "abcd", 2, 2/3.),
                 $("abc", "abcd", 3, 0.5),
                 $("abcd", "abc", 1, 0.75),
                 $("abcdxefgh", "abcdyefgh", 2, 0.75));
    }
}
