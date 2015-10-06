
/**
 * This class will be used as an Object to contain the weights
 * of all the terms inside a query
 * 
 * @author Ryan Zink & Matt Versaggi
 * @version 1
 */

import java.io.*;
import java.util.*;

public class Query
{
    // instance variables - replace the example below with your own
    private Hashtable<String, Double> termWeights;
    private Hashtable<String, Integer> termFreq;
    private Hashtable<Document, Double> relavDocCorrelation;
    private String[] terms;
    private ArrayList<String> vocab;
    private BigD bigData;

    /**
     * Constructor for objects of class Query
     */
    public Query(String[] newTerms, BigD newData)
    {
        // initialise instance variables
        terms = newTerms;
        bigData = newData;
        termWeights = new Hashtable(20);
        termFreq = new Hashtable(20);
        vocab = new ArrayList();
        relavDocCorrelation = new Hashtable(100);
    }
    
    //clac the termfreq of each word in the q, basically loop
    //the terms array and count the occurences
    private void createTermFreq()
    {
        int newTerm = 1;
        //loop through the terms
        for(int i = 0; i < terms.length; i++)
        {
            if(termFreq.containsKey(terms[i]))
            {
                int temp = termFreq.get(terms[i]);
                termFreq.put(terms[i], ++temp);
            }
            //else its not in there
            else
            {
                termFreq.put(terms[i], newTerm);
            }
        }
        
        //set up the vocab for this query
        String tempWords;
        //loop through the elements of termFreq to get the vocab
        for (Enumeration<String> e = termFreq.keys(); e.hasMoreElements();)
        {
            tempWords = e.nextElement();
            vocab.add(tempWords);
        }
    }
    
    //clac the weight of each term in the query
    private void calcWeights()
    {
        //go through terms and calc their weight
        for(int i = 0; i < vocab.size(); i++)
        {
            Double weight = (0.5 + 0.5*(double)(termFreq.get(vocab.get(i))))*
                bigData.getIDFs().get(vocab.get(i));
            termWeights.put(vocab.get(i), weight);
        }
    }
    
    
    //calc the relavance of all the docs
    private void calcRelavance()
    {
        ArrayList<Document> docs = bigData.getDocs();
        Hashtable<String, Term> termsForDoc;
        //now loop through all the docs and calc the relavance of that doc to the query
        /*
         * Work here next time
         */
    }
}
