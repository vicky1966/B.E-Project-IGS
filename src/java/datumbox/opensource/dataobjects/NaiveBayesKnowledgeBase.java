
package datumbox.opensource.dataobjects;

import java.util.HashMap;
import java.util.Map;


public class NaiveBayesKnowledgeBase {
 
    public int n=0;
    
   
    public int c=0;
    
  
    public int d=0;
  
    public Map<String, Double> logPriors = new HashMap<>();
    
  
    public Map<String, Map<String, Double>> logLikelihoods = new HashMap<>();
}
