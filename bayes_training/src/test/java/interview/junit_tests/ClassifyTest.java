package interview.junit_tests;

import interview.test.NaiveBayesClassifier;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ClassifyTest {
    private NaiveBayesClassifier model = new NaiveBayesClassifier();

    @Test
    public void modelClassifiesPositive() {
        ArrayList<String[]> posit = new ArrayList<>();
        posit.add(new String[]{"positive", "good", "shop"});
        model.fit(posit);
        assertEquals("positive", model.classify("Good shop"));
    }

    @Test
    public void modelClassifiesNeutral() {
        ArrayList<String[]> neut = new ArrayList<>();
        neut.add(new String[]{"neutral", "average", "shop"});
        model.fit(neut);
        assertEquals("neutral", model.classify("Average shop"));
    }

    @Test
    public void modelClassifiesNegative() {
        ArrayList<String[]> negat = new ArrayList<>();
        negat.add(new String[]{"negative", "bad", "shop"});
        model.fit(negat);
        assertEquals("negative", model.classify("Bad shop"));
    }
}
