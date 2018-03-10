
package datumbox.opensource.examples;

import datumbox.opensource.classifiers.NaiveBayes;
import datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaiveBayesExample2 {


    public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
    
    public static String classify(String input) throws IOException {
        Map<String, URL> trainingFiles = new HashMap<>();
        trainingFiles.put("topic1", NaiveBayesExample.class.getResource("training.language.en.txt"));
        trainingFiles.put("topic2", NaiveBayesExample.class.getResource("training.language.fr.txt"));
        trainingFiles.put("topic3", NaiveBayesExample.class.getResource("training.language.de.txt"));
        
        
        Map<String, String[]> trainingExamples = new HashMap<>();
        for(Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
        }
        
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        nb.train(trainingExamples);
        
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();
        
        nb = null;
        trainingExamples = null;
        
        
        nb = new NaiveBayes(knowledgeBase);
        String outputEn="";
        outputEn = nb.predict(input);

        return (outputEn);
    }
    
}
