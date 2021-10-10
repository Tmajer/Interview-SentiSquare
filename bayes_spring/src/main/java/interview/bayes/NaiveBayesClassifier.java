package interview.bayes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * <h1>Preprocessor</h1>
 * The Preprocessor program contains the methods for text preprocessing for
 * the classifier. It splits the sentences into words and deletes any white-space
 * and punctuation characters.
 *
 * @author  Tomas Majer
 * @version 1.0
 */
class Preprocessor {

    /**
     * Returns array of Strings of words cleared of white-spaces and
     * punctuation in the given sentence.
     *
     * @param sentence the sentence to preprocess
     * @return preprocessed words
     */
    public static String[] preprocess(String sentence){
        String[] words = splitToWords(sentence);
        return normalizeWords(words);
    }

    /**
     * Returns array of words in the given sentence.
     *
     * @param sentence the sentence to split
     * @return words in the sentence
     */
    private static String[] splitToWords(String sentence){
        return sentence.split(";|\\s+");
    }

    /**
     * Returns array of lower case words with removed punctuation and numbers.
     *
     * @param words the words to normalize
     * @return normalized words
     */
    private static String[] normalizeWords (String[] words){
        String[] normalized = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            normalized[i] = words[i].replaceAll("[^a-zA-Z ]", "").toLowerCase();
        }
        return normalized;
    }
}

/**
 * <h1>Naive Bayes Classifier</h1>
 * The NaiveBayesClassifier program contains methods for loading the model parameters,
 * classifying the sentences.
 *
 * @author  Tomas Majer
 * @version 1.0
 */
public class NaiveBayesClassifier {

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
        this.wordsLikelihoods = new HashMap<>();
        this.positiveLabelFrequency = 0;
        this.neutralLabelFrequency = 0;
        this.negativeLabelFrequency = 0;
        this.priors = new double[3];
    }

    /**
     * Reads a json file at the path filename containing current model attributes
     * and loads them to the according object attributes.
     *
     * @param filename the path of the json file
     */
    public void loadParams(Path filename) {
        try {
            String content = Files.readString(filename, StandardCharsets.UTF_8);
            JSONObject jsonObj = new JSONObject(content);
            positiveLabelFrequency = jsonObj.getDouble("positiveLabelFrequency");
            neutralLabelFrequency = jsonObj.getDouble("neutralLabelFrequency");
            negativeLabelFrequency = jsonObj.getDouble("negativeLabelFrequency");
            JSONArray tempPriors = jsonObj.getJSONArray("priors");
            for (int i = 0; i < tempPriors.length(); i++) {
                priors[i] = tempPriors.getDouble(i);
            }
            JSONObject likeliHoodsObj = jsonObj.getJSONObject("wordsLikelihoods");
            Iterator<String> likeliHoodsIterator = likeliHoodsObj.keys();
            while(likeliHoodsIterator.hasNext()) {
                String word = likeliHoodsIterator.next();
                JSONArray tempLikelihoods = likeliHoodsObj.getJSONArray(word);
                double[] tempLhoodsArray = new double[3];
                for (int i = 0; i < tempPriors.length(); i++) {
                    tempLhoodsArray[i] = tempLikelihoods.getDouble(i);
                }
                wordsLikelihoods.put(word, tempLhoodsArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classifies the given sentence and returns the most probable sentiment label.
     *
     * @param sentence the sentence to label
     * @return the determined sentiment label
     */
    public String classify(String sentence) {
        String[] words = Preprocessor.preprocess(sentence);
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
