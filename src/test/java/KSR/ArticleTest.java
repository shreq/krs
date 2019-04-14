package KSR;

import KSR1.Article;
import KSR1.SGMFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    @DisplayName("Should split text into tokens correctly")
    void shouldSplit() {
        //given
        Article article = new Article();
        article.setText("Article test. Shouldn't split into Bad list.");
        //when
        ArrayList<String> words = article.getWords();
        //then
        assertThat(words)
                .hasSize(7)
                .containsExactly("article", "test", "shouldn't", "split", "into", "Bad", "list");
    }
}
