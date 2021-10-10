package interview.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>CSVReader</h1>
 * The CSVReader program contains method for reading the CSV for training and testing
 * the classifier.
 *
 * @author  Tomas Majer
 * @version 1.0
 */

public class CSVReader {

    /**
     * Returns arraylist of sentences in the given CSV file.
     *
     * @param pathCSV path to the CSV file
     * @return loaded sentences
     */
    public static ArrayList<String> readCSV(String pathCSV) throws FileNotFoundException {
        BufferedReader csvReader = new BufferedReader(new FileReader(pathCSV));
        ArrayList<String> loadedCSVData = new ArrayList<>();
        while (true) {
            try {
                String row;
                if ((row = csvReader.readLine()) == null) break;
                loadedCSVData.add(row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedCSVData;
    }
}
