package KSR.Statistics;

import KSR1.Article;
import KSR1.Statistics.DocumentCollectionStats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DocumentCollectionStatsTest {
    @Mock
    Article article1;
    @Mock
    Article article2;

    ArrayList<Article> collection;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        collection = new ArrayList<>(Arrays.asList(article1, article2));
    }

    @Test
    @DisplayName("Should return proper values when queried for IDF with exact comparison")
    void shouldReturnProperValueExactIDF() {
        when(article1.getWords()).thenReturn(new ArrayList<>(Arrays.asList("abc", "xyz")));
        when(article2.getWords()).thenReturn(new ArrayList<>(Arrays.asList("abc", "klm")));

        assertThat(DocumentCollectionStats.inverseDocumentFrequency(collection))
                .containsEntry("abc", Math.log(1))
                .containsEntry("xyz", Math.log(2))
                .containsEntry("klm", Math.log(2));

    }

    @Test
    @DisplayName("Should return proper values when queried for TF with exact comparison")
    void shouldReturnProperValueExactTF() {
        when(article1.getWords()).thenReturn(new ArrayList<>(Arrays.asList("abc", "xyz")));
        when(article2.getWords()).thenReturn(new ArrayList<>(Arrays.asList("abc", "klm")));

        assertThat(DocumentCollectionStats.termFrequency(collection))
                .containsEntry("abc", 0.5)
                .containsEntry("xyz", 0.25)
                .containsEntry("klm", 0.25);

    }
}
