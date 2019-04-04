package KSR.Processing;

import KSR1.Processing.GenBoundNGram;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenBoundNGramTest {

    @ParameterizedTest
    @MethodSource("getLimits")
    @DisplayName("Should return 1 on equal strings")
    void shouldReturnOne(int low, int high){
        //given
        GenBoundNGram nGram = new GenBoundNGram(low, high);
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
    void shouldReturnAnswer(String word1, String word2, int low, int high, double expected){
        //given
        GenBoundNGram nGram = new GenBoundNGram(low, high);
        //when
        double similarity = nGram.compare(word1, word2);
        //then
        assertEquals(expected, similarity);
    }

    private static Object[] getCases(){
        return $($("abc", "abcd", 1, 2, 5/7.),
                 $("abc", "abcd", 2, 3, 3/5.),
                 $("abc", "abcd", 3, 3, 0.5),
                 $("abcd", "abc", 1, 1, 0.75),
                 $("abcdxefgh", "abcdyefgh", 1, 4, 2/3.));
    }

    private static Object[] getLimits(){
        return $($(1, 2),
                 $(2, 4),
                 $(2, 8),
                 $(7, 9),
                 $(1, 10));
    }
}
