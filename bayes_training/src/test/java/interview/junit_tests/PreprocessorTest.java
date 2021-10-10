package interview.junit_tests;

import interview.test.Preprocessor;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreprocessorTest {
    private Preprocessor preprocessor = new Preprocessor();

    @Test
    public void preprocessorSplits() {
        assertArrayEquals(new String[]{"positive", "", "sushi"}, preprocessor.preprocess("Posi+tive; sU+s56+§.!-hi"));
    }
}
