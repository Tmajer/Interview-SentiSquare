package interview.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static interview.test.Preprocessor.preprocess;

/**
 * <h1>Naive Bayes Classifier</h1>
 * The NaiveBayesClassifier program contains methods for training the classifier,
 * classifying the sentences and saving the model parameters.
 *
 * @author  Tomas Majer
 * @version 1.0
 */

public class NaiveBayesClassifier {

    /**
     * The map of unique words and their counts in [positive, neutral, negative] sentiment.
     */
    private Map<String, int[]> wordsCounts;
    /**
     * The map of unique words and their likelihoods in [positive, neutral, negative] sentiment.
     */
    private Map<String, double[]> wordsLikelihoods;
    /**
     * Total number of positively labeled words.
     */
    private double positiveLabelFrequency;
    /**
     * Total number of neutraly labeled words.
     */
    private double neutralLabelFrequency;
    /**
     * Total number of negatively labeled words.
     */
    private double negativeLabelFrequency;
    /**
     * Prior probabilities for [positive, neutral, negative] sentiment.
     */
    private double[] priors;


    /**
     * Class constructor.
     */
    public NaiveBayesClassifier (){
        this.wordsCounts = new HashMap<>();
        this.wordsLikelihoods = new HashMap<>();
        this.positiveLabelFrequency = 0;
        this.neutralLabelFrequency = 0;
        this.negativeLabelFrequency = 0;
        this.priors = new double[3];
    }

    /**
     * Outputs a json file with the name filename containing current model attributes - parameters.
     *
     * @param filename the name of the created file
     */
    public void writeParams(String filename){
        try {
            Writer myWriter = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8);
            StringBuilder jsonB = new StringBuilder();
            jsonB.append("{");
            jsonB.append("\"priors\": [" + priors[0] + "," + priors[1] + "," + priors[2] + "],");
            jsonB.append("\"positiveLabelFrequency\" : " + positiveLabelFrequency + ",");
            jsonB.append("\"neutralLabelFrequency\" :" + neutralLabelFrequency + ",");
            jsonB.append("\"negativeLabelFrequency\" :" + negativeLabelFrequency + ",");
            jsonB.append("\"wordsLikelihoods\" : {");
            String delimiter = "";
            for (Map.Entry<String, double[]> entry : wordsLikelihoods.entrySet()) {
                jsonB.append(delimiter);
                delimiter = ",";
                double[] val = entry.getValue();
                String arrayInString = "[" + val[0] + "," + val[1] + "," + val[2] + "]";
                jsonB.append("\"" + entry.getKey() + "\" :" + arrayInString);
            }
            jsonB.append("}}");
            String json = jsonB.toString();
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Trains the model given the labeled data in the array list of String arrays.
     * First element of the array is required to be the label.
     *
     * @param fittedData preprocessed words to train the model on
     */
    public void fit(ArrayList<String[]> fittedData){
        setWordCounts(fittedData);
        handleNullCounts();
        setWordsLikelihoods();
        setPriors();
    }

    /**
     * Classifies the given sentence and returns the most probable sentiment label.
     *
     * @param sentence the sentence to label
     * @return the determined sentiment label
     */
    public String classify(String sentence) {
        String[] words = preprocess(sentence);
        double[] jointProbabilities = new double[3];
        double[] defaultLikelihoods = new double[]{1.0 / positiveLabelFrequency, 1.0 / neutralLabelFrequency, 1.0 / negativeLabelFrequency};
        for (String word: words) {
            jointProbabilities = addLogs(jointProbabilities,wordsLikelihoods.getOrDefault(word, defaultLikelihoods));
        }
        jointProbabilities = addLogs(jointProbabilities, priors);
        int maxIndex = arrayMax(jointProbabilities);
        String classification;
        switch (maxIndex) {
            case 0:
                classification = "positive";
                break;
            case 1:
                classification = "neutral";
                break;
            case 2:
                classification = "negative";
                break;
            default:
                classification = "unknown";
                break;
        }
        return classification;
    }

    /**
     * Given the training set of words, sets the attributes wordsCounts,
     * positiveLabelFrequency, neutralLabelFrequency and negativeLabelFrequency
     * of the model.
     *
     * @param allWords the words to fit the model with
     */
    private void setWordCounts (ArrayList<String[]> allWords) {
        for (String[] words: allWords) {
            String sentiment = words[0];
            int[] sentimentArray = new int[]{0, 0, 0};
            if (sentiment.equals("positive")) {
                sentimentArray[0] = 1;
            } else if (sentiment.equals("neutral")) {
                sentimentArray[1] = 1;
            } else if (sentiment.equals("negative")) {
                sentimentArray[2] = 1;
            }
            for (int i = 1; i < words.length; i++){
                if (wordsCounts.containsKey(words[i]) && !(words[i].equals(""))) {
                    int[] wordSentimentCount = wordsCounts.get(words[i]);
                    int[] newCount = sumElements(wordSentimentCount, sentimentArray);
                    wordsCounts.put(words[i], newCount);
                }else if (!(words[i].equals(""))){
                    wordsCounts.put(words[i], sentimentArray);
                }
            }
            positiveLabelFrequency += (words.length - 1) * sentimentArray[0];
            neutralLabelFrequency += (words.length - 1) * sentimentArray[1];
            negativeLabelFrequency += (words.length - 1) * sentimentArray[2];
        }
    }

    /**
     * Updates the wordsCounts Map to not contain any zero values, updates the label
     * frequencies correspondingly.
     */
    private void handleNullCounts () {
        for (Map.Entry<String, int[]> entry : wordsCounts.entrySet()) {
            wordsCounts.put(entry.getKey(), sumElements(entry.getValue(), new int[]{1,1,1}));
            positiveLabelFrequency += 1;
            neutralLabelFrequency += 1;
            negativeLabelFrequency += 1;
        }
    }

    /**
     * Sets the attribute wordsLikelihoods.
     */
    private void setWordsLikelihoods () {
        for (Map.Entry<String, int[]> entry : wordsCounts.entrySet()) {
            String word = entry.getKey();
            int[] wordCounts = wordsCounts.get(word);
            double positiveLikelihood =  (double) wordCounts[0] / positiveLabelFrequency;
            double neutralLikelihood = (double) wordCounts[1] / neutralLabelFrequency;
            double negativeLikelihood = (double) wordCounts[2] / negativeLabelFrequency;
            wordsLikelihoods.put(word, new double[]{positiveLikelihood, neutralLikelihood, negativeLikelihood});
        }
    }

    /**
     * Sets the attribute priors.
     */
    private void setPriors () {
        double labelsFrequency = positiveLabelFrequency + neutralLabelFrequency + negativeLabelFrequency;
        priors[0] = positiveLabelFrequency / labelsFrequency;
        priors[1] = neutralLabelFrequency / labelsFrequency;
        priors[2] = negativeLabelFrequency / labelsFrequency;
    }

    /**
     * Returns the element-wise sum of two integer arrays.
     *
     * @param firstArray the first array to sum
     * @param secondArray the second array to sum
     * @return the summed array
     */
    private int[] sumElements (int[] firstArray, int[] secondArray){
        int[] resultArray = new int[firstArray.length];
        for (int i = 0; i < firstArray.length; i++){
            resultArray[i] = firstArray[i] + secondArray[i];
        }
        return resultArray;
    }

    /**
     * Element-wise adds natural logarithms of an array elements to an existing array elements.
     *
     * @param modifiedArray the array that gets modified
     * @param addedLogArray the array that gets added
     * @return the modified array
     */
    private double[] addLogs (double[] modifiedArray, double[] addedLogArray){
        for (int i = 0; i < modifiedArray.length; i++){
            modifiedArray[i] += Math.log(addedLogArray[i]);
        }
        return modifiedArray;
    }

    /**
     * Returns the first index of the array maximum.
     *
     * @param array the array for maximum localisation
     * @return the maximum index
     */
    private int arrayMax (double[] array){
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++)
            maxIndex = Double.compare(array[i], array[maxIndex]) > 0 ? i : maxIndex;
        return maxIndex;
    }
}
