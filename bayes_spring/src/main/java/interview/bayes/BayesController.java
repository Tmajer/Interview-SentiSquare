package interview.bayes;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <h1>Bayes Controller</h1>
 * The BayesController program handles the HTTP requests GET and POST,
 * initializes NaiveBayesClassifier at the server startup.
 *
 * @author  Tomas Majer
 * @version 1.0
 */
@RestController
public class BayesController {

    /**
     * The NaiveBayesClassifier instance for the server.
     */
    NaiveBayesClassifier model;

    /**
     * Initializes the model attribute and uses the json file to load its
     * attributes.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup(){
        this.model = new NaiveBayesClassifier();
        Path path = Paths.get("./params.json");
        model.loadParams(path);
    }

    /**
     * Server response to GET HTTP request.
     */
    @GetMapping("/")
    public String index() {
        return "Please POST your text!";
    }

    /**
     * Server response to POST HTTP request.
     *
     * @param requestBody body of the POST request
     */
    @PostMapping("/api")
    public String returnLabel(@RequestBody String requestBody) {

        JSONObject jsonObj = new JSONObject(requestBody);
        String textToClassify = jsonObj.getString("text");
        String label = model.classify(textToClassify);

        return "{ \n \"label\": \"" + label + "\" \n }";

    }

}