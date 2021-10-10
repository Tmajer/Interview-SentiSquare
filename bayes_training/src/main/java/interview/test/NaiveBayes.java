package interview.test;

import java.io.*;
import java.util.ArrayList;


import static interview.test.CSVReader.readCSV;
import static interview.test.Evaluator.evaluate;
import static interview.test.Preprocessor.preprocess;

/**
 * <h1>Naive Bayes Classifier</h1>
 * The NaiveBayes program runs Naive Bayes Classifier of sentences.
 *
 * @author  Tomas Majer
 * @version 1.0
 */

public class NaiveBayes {

    /**
     * This is the main method which utilises the other classes in the package.
     * It reads the passed CSV documents, preprocesses the words in them,
     * trains and tests the Bayes Classifier while writing its parameters into
     * a json file.
     *
     * @param args Unused.
     * @return Nothing.
     */

    public static void main(String[] args) {
        String pathToTrain = "train.csv";
        String pathToTest = "test.csv";
        try {
            ArrayList<String> trainSentences = readCSV(pathToTrain);
            trainSentences.remove(0);
            ArrayList<String> testSentences = readCSV(pathToTest);
            testSentences.remove(0);

            ArrayList<String[]> trainWords = preprocess(trainSentences);
            NaiveBayesClassifier model = new NaiveBayesClassifier();
            model.fit(trainWords);

            ArrayList<String[]> testWords = preprocess(testSentences);
            double accuracy = evaluate(testWords, model);
            model.writeParams("params.json");
            System.out.printf("Accuracy of the model is: %.4f%n", accuracy);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
