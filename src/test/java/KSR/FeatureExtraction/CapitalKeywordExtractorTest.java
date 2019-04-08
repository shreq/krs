package KSR.FeatureExtraction;

import KSR1.Article;
import KSR1.FeatureExtraction.CapitalKeywordExtractor;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CapitalKeywordExtractorTest {

    CapitalKeywordExtractor extractor;
    ArrayList<Article> articles;

    @Before
    public void init() {
        articles = new ArrayList<>();
        Article article = new Article();
        article.setText("Jeden dwa Trzy cztery piec szesc");
        articles.add(article);
        extractor = new CapitalKeywordExtractor(articles);
    }

    @Test
    public void shouldReturnProperValueExactIDF() {
        // given
        // when
        ArrayList<Double> list = extractor.extract(articles.get(0));
        // then
        assertThat(list)
                .hasSize(2)
                .containsSubsequence(2., 4.);

    }
}
