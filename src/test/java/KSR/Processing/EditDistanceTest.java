package KSR.Processing;

import KSR1.Processing.EditDistance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {

    private EditDistance similarity;

    @BeforeEach
    void SetUp(){
        similarity = new EditDistance();
    }

    @Test
    @DisplayName("Should return 1 on equal strings")
    void shouldReturnZero(){
        //given
        String A = "Lorem ipsum 123";
        String B = "Lorem ipsum 123";
        //when
        double sim = similarity.compare(A, B);
        //then
        assertEquals(1, sim, .000001);
    }


    @ParameterizedTest
    @MethodSource("getCases")
    @DisplayName("Should return correct answer")
    void shouldReturnAnswer(String word1, String word2, double expected){
        //given
        //when
        double sim = similarity.compare(word1, word2);
        //then
        assertEquals(expected, sim, .000001);
    }

    private static Object[] getCases(){
        return $($("abc", "abcd", .75),
                 $("abcd", "abc", .75),
                 $("abcd", "abce", .75),
                 $("xabc", "abcx", .5),
                 $("abxcd", "abcxde", .5),
                 $("abcdefg", "uwxyzab", 0),
                 $("abcdefg", "kmlnopr", 0),
                 $("abcdef", "kmlnop", 0),
                 $("abcde", "kmlno", 0),
                 $("abcd", "kmln", 0),
                 $("a", "b", 0),
                 $("xyabc", "abcxy", .2));
    }
}
