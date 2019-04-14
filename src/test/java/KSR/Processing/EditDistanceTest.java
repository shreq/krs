package KSR.Processing;

import KSR1.Processing.EditDistance;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.jupiter.api.Assertions.*;

public class EditDistanceTest {

    private EditDistance distanceCalculator;

    @BeforeEach
    void SetUp(){
        distanceCalculator = new EditDistance();
    }

    @Test
    @DisplayName("Should return 0 on equal strings")
    void shouldReturnZero(){
        //given
        String A = "Lorem ipsum 123";
        String B = "Lorem ipsum 123";
        //when
        double distance = distanceCalculator.compare(A, B);
        //then
        assertEquals(0, distance);
    }


    @ParameterizedTest
    @MethodSource("getCases")
    @DisplayName("Should return correct answer")
    void shouldReturnAnswer(String word1, String word2, int expected){
        //given
        //when
        double distance = distanceCalculator.compare(word1, word2);
        //then
        assertEquals(expected, distance);
    }

    private static Object[] getCases(){
        return $($("abc", "abcd", 1/4.),
                 $("abcd", "abc", 1/4.),
                 $("abcd", "abce", 1/4.),
                 $("xabc", "abcx", 2),
                 $("abxcd", "abcxde", 3),
                 $("abcdefg", "uwxyzab", 7),
                 $("abcdefg", "kmlnopr", 7),
                 $("abcdef", "kmlnop", 6),
                 $("abcde", "kmlno", 5),
                 $("abcd", "kmln", 4),
                 $("a", "b", 1),
                 $("xyabc", "abcxy", 4));
    }
}
