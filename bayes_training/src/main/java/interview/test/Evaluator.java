package interview.test;

import java.util.ArrayList;

/**
 * <h1>Evaluator</h1>
 * The Evaluator program contains methods for evaluating the Bayes classifier,
 * classifying the sentences and saving the model parameters.
 *
 * @author  Tomas Majer
 * @version 1.0
 */

public class Evaluator {

    /**
     * Returns the accuracy of the given model on the testSentences set.
     *
     * @param testSentences the set of testing sentences
     * @param model the tested classifier
     * @return the accuracy of the model
     */
    public static double evaluate(ArrayList<String[]> testSentences, NaiveBayesClassifier model) {
        int hit = 0;
        ArrayList<String[]> testResults = classifySentences(testSentences, model);
        for (String[] result: testResults) {
            if (result[0].equals(result[1])) hit++;
        }
        return (double) hit / (double) testResults.size();
    }

    /**
     * Returns the resulting labels of the sentences in the testSentences set in arraylist
     * containing String pairs [expected results, given results].
     *
     * @param testSentences the set of testing sentences
     * @param model the tested classifier
     * @return the results of the classification
     */
    private static ArrayList<String[]> classifySentences(ArrayList<String[]> testSentences, NaiveBayesClassifier model) {
        ArrayList<String[]> testingPairs = prepareSentences(testSentences);
        ArrayList<String[]> testingResults = new ArrayList<>();

        for (String[] pair: testingPairs) {
            testingResults.add(new String[]{pair[0], model.classify(pair[1])});
        }
        return testingResults;
    }

    /**
     * Builds the sentences to form acceptable input for interview.test.NaiveBayesClassifier.classify.
     *
     * @param testSentences the set of testing sentences
     * @return the set of modified testing sentences
     */
    private static ArrayList<String[]> prepareSentences(ArrayList<String[]> testSentences){
        ArrayList<String[]> testingPairs = new ArrayList<>();
        for (String[] testSentence: testSentences) {
            String label = testSentence[0];
            StringBuilder sentence = new StringBuilder();
            for (int i = 1; i < testSentence.length; i++) {
                sentence.append(testSentence[i]);
                if (i != (testSentence.length - 1)) sentence.append(" ");
            }
            testingPairs.add(new String[]{label, sentence.toString()});
        }
        return testingPairs;
    }
}
