# Interview-SentiSquare

This is repository of the Naive Bayes Classifier project for the SentiSquare interview.


## Folders description

- bayes_training: Contains the program for preprocessing the training/testing sentences, training the Naive Bayes Classifier and  classifying with it, and classifier evaluator.
  - pom.xml: Maven settings
  - test.csv: Testing dataset
  - train.csv: Training dataset
  - params.json: Saved Classifier parameters
  - target: Contains the compiled classes and the (.jar) program
  - src: Contains the source code and unit tests
- bayes_spring: Contains the program for the Spring REST API server with the Naive Bayes Classifier. 
  - pom.xml: Maven settings
  - params.json: Saved Classifier parameters
  - target: Contains the compiled classes and the (.jar) program
  - src: Contains the source code 


## Technologies

- Java 17
- JSON-java
- spring-boot
- junit4
