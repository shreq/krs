package KSR.FeatureExtraction;

import KSR1.Article;
import KSR1.FeatureExtraction.CombinedExtractor;
import KSR1.FeatureExtraction.FeatureExtractor;
import KSR1.Statistics.DocumentCollectionStats;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CombinedExtractorTest {
    @Mock
    FeatureExtractor extractor1;
    @Mock
    FeatureExtractor extractor2;

    CombinedExtractor combinedExtractor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        combinedExtractor = new CombinedExtractor(extractor1, extractor2);
    }

    @Test
    @DisplayName("Should return list ")
    public void shouldReturnProperValueExactIDF() {
        when(extractor1.extract(any())).thenReturn(new ArrayList<>(Arrays.asList(0.5, 0.25)));
        when(extractor2.extract(any())).thenReturn(new ArrayList<>(Arrays.asList(0.125, 0.75)));

        assertThat(combinedExtractor.extract(new Article()))
                .hasSize(4)
                .containsSubsequence(0.5, 0.25, 0.125, 0.75);

    }
}
