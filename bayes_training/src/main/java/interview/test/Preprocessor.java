package interview.test;

import java.util.ArrayList;

/**
 * <h1>Preprocessor</h1>
 * The Preprocessor program contains the methods for text preprocessing for
 * the classifier. It splits the sentences into words and deletes any white-space
 * and punctuation characters.
 *
 * @author  Tomas Majer
 * @version 1.0
 */

public class Preprocessor {

    /**
     * Returns arraylist of words cleared of white-spaces and
     * punctuation in the given arraylist of sentences.
     *
     * @param sentences the sentences to preprocess
     * @return preprocessed words
     */
    public static ArrayList<String[]> preprocess(ArrayList<String> sentences){
        ArrayList<String[]> preprocessedSentences = new ArrayList<>();
        for (String s: sentences) {
            String[] words = splitToWords(s);
            String[] normalizedWords = normalizeWords(words);
            preprocessedSentences.add(normalizedWords);
        }
        return preprocessedSentences;
    }

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
