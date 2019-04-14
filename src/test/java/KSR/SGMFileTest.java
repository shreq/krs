package KSR;

import KSR1.Article;
import KSR1.SGMFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;

class SGMFileTest {

    @Test
    @DisplayName("Should read file correctly")
    void shouldReadCorrectly() throws FileNotFoundException {
        //given
        File file = new File("src/test/resources/test.sgm");
        //when
        SGMFile sgm = SGMFile.loadFile(file);
        //then
        assertThat(sgm.articles)
                .hasSize(2)
                .extracting(Article::getTitle)
                .containsExactly("BAHIA COCOA REVIEW", "STANDARD OIL <SRD> TO FORM FINANCIAL UNIT");
    }

    @Test
    @DisplayName("Should strip XML entities/artifacts properly")
    void shouldStrip() {
        //given
        String text = "123 &lt;XYZ&gt; abc";
        //when
        String res = SGMFile.stripXMLentities(text);
        //then
        assertThat(res).matches("123 XYZ abc");
    }
}
