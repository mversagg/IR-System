/**
 * This class will be used as an Object to contain the weights
 * of all the terms inside a query
 * 
 * @author Ryan Zink & Matt Versaggi
 * @version 1
 */

import java.io.*;
import java.util.*;
import java.lang.Math;
import java.awt.geom.Point2D;

public class Query
{
    // instance variables - replace the example below with your own
    private Hashtable<String, Double> termWeights;
    private Hashtable<String, Integer> termFreq;
    private ArrayList<Point2D.Double> relavDocCorr;
    private ArrayList<String> terms;
    private ArrayList<String> origTerms;
    private ArrayList<String> vocab;
    private BigD bigData;
    //this will be the variable containing the number of total docs
    private int numDocs;
    //the average length of document
    private double avdl;
    private StopwordRemoval swr;
    private int algChoice;

    /**
     * Constructor for objects of class Query
     */
    public Query(ArrayList<String> newTerms, BigD newData, int alg) throws FileNotFoundException
    {
        // initialise instance variables
        //make sure the alg choice is valid
        if(alg < 4 && alg > 0)
        algChoice = alg;
        else
        algChoice = 1;
        
        swr = new StopwordRemoval("stopwords.txt");
        terms = newTerms;
        origTerms = newTerms;
        QueryCleanUp();
        bigData = newData;
        termWeights = new Hashtable(20);
        termFreq = new Hashtable(20);
        vocab = new ArrayList();
        relavDocCorr = new ArrayList();
        calcAVDL();
        //calc the term frequencies inside the query
        createTermFreq();
        calcWeights();
        calcRelavance();
        sortResults();
    }
    
    //clean up the words before they are looked at
    private void QueryCleanUp()
    {
        Stemmer s = new Stemmer();
        ArrayList<String> cleanTerms = new ArrayList();

        //loop through all the words in this persons utterance
        for(int index = 0; index < terms.size(); index++)
        {
            if(!swr.isStopWord(terms.get(index)))
            {
                s.add(terms.get(index).toCharArray(), 
                    terms.get(index).length());
                s.stem();
                //System.out.println("ADDING: " + s.toString());
                cleanTerms.add(s.toString());
            }
        }
        
        terms = cleanTerms;
    }

    public ArrayList<Point2D.Double> getResults() {
        ArrayList<Point2D.Double> filteredResults;
        int fromIndex = relavDocCorr.size() - 10;
        int toIndex = relavDocCorr.size();
        
        filteredResults = new ArrayList(relavDocCorr.subList(fromIndex, toIndex));
        
        for (int i = 0; i < 10; i++) {
            if (filteredResults.get(i).y == 0)
                filteredResults.remove(i);
        }
        
        return filteredResults;
    }
    
    //clac the termfreq of each word in the q, basically loop
    //the terms array and count the occurences
    private void createTermFreq()
    {
        //loop through the terms
        for(int i = 0; i < terms.size(); i++)
        {
            if(termFreq.containsKey(terms.get(i)))
            {
                int temp = termFreq.get(terms.get(i));
                termFreq.put(terms.get(i), ++temp);
            }
            //else its not in there
            else
            {
                termFreq.put(terms.get(i), 1);
            }
        }

        //set up the vocab for this query
        String tempWords;
        //loop through the elements of termFreq to get the vocab
        for (Enumeration<String> e = termFreq.keys(); e.hasMoreElements();)
        {
            tempWords = e.nextElement();
            vocab.add(tempWords);
            //System.out.println(tempWords);
        }
        
    }

    //clac the weight of each term in the query
    private void calcWeights()
    {
        //go through terms and calc their weight
        for(int i = 0; i < vocab.size(); i++)
        {
            if(bigData.getIDFs().containsKey(vocab.get(i))){
                Double weight = (0.5 + 0.5*(double)(termFreq.get(vocab.get(i))))*
                    bigData.getIDFs().get(vocab.get(i));
                termWeights.put(vocab.get(i), weight);
            }
            else
            {
                termWeights.put(vocab.get(i), 0.0);
            }
        }
    }

    //calc the relavance of all the docs
    private void calcRelavance()
    {
        ArrayList<Document> docs = bigData.getDocs();
        numDocs = docs.size();
        Hashtable<String, Term> termsForDoc;
        double wDoc = 0;
        //we have vocab to use for a dictionary
        //loop through the docs one by one
        for(int docIndex = 0; docIndex < docs.size(); docIndex++)
        {
            //check for the alg choice
            if(algChoice == 1)
            wDoc = cosSimilarity(docs.get(docIndex));
            if(algChoice == 2)
            wDoc = okapi(docs.get(docIndex));
            if(algChoice == 3)
            wDoc = pnw(docs.get(docIndex));
            
            relavDocCorr.add(new Point2D.Double((double) docIndex, wDoc));
        }
        
        if(algChoice == 1)
        System.out.println("Using CosSim Alg");
        if(algChoice == 2)
        System.out.println("Using Okapi Alg");
        if(algChoice == 3)
        System.out.println("Using PNW Alg");
    }
    
    //little function to calculate the average length of documents in bigd
    private void calcAVDL()
    {
        ArrayList<Document> docs = bigData.getDocs();
        double totalNumWords = 0;
        //loop through the docs one by one
        for(int docIndex = 0; docIndex < docs.size(); docIndex++)
        {
            totalNumWords += docs.get(docIndex).getPerson().getFilteredText().length;
        }
        
        avdl = totalNumWords/docs.size();
    }
    
    // Uses insertion sort to sort the docs based on relevance
    private void sortResults() {
        for (int i = 1; i < relavDocCorr.size(); i++) {
            int j = i;
            Point2D.Double temp = relavDocCorr.get(i);
            for (j = i; j > 0 && temp.y < relavDocCorr.get(j - 1).y; j--)
                relavDocCorr.set(j, relavDocCorr.get(j - 1));
                
            relavDocCorr.set(j, temp);
        }
    }
    
    //do the cosine similarity
    private Double cosSimilarity(Document theDoc)
    {
        Hashtable<String, Term> termsForDoc = theDoc.getTermsOfDoc();
        double tempWeight, qWeight, qDenom = 0, tDenom = 0, qtNumer = 0;
        double answer;
        String currQTerm;
        
        //now go through all the terms in the doc and get the weight
        //for each word that is in the doc and the query, vocab is the list of words in the q
        for (int index = 0; index < vocab.size(); index++)
        {
            currQTerm = vocab.get(index);
            
            qWeight = termWeights.get(currQTerm);
            
            //first check if the query word is in the doc list of words
            if(termsForDoc.containsKey(currQTerm))
            {
                tempWeight = termsForDoc.get(currQTerm).getWeight();
            }
            // doesn't exist so the weight will be 0 by default
            else
            {
                tempWeight = 0.0;
            }
            
            qtNumer += qWeight * tempWeight;
            qDenom += qWeight * qWeight;
            tDenom += tempWeight * tempWeight;
        }

        if (qDenom == 0 || tDenom == 0)
            answer = 0;
        else
            answer = qtNumer/(Math.sqrt(qDenom*tDenom));
        
        return answer;
    }
    
    //do the pivoted normalization weighting
    private Double pnw(Document theDoc)
    {
        Hashtable<String, Term> termsForDoc = theDoc.getTermsOfDoc();
        double dfi, fij, dl = theDoc.getPerson().getFilteredText().length, qf, answer = 0;
        double s = .2; //doc length normalization parameter
        
        //now go through all the terms in the doc and get the weight
        //for each word that is in the doc and the query, vocab is the list of words in the q
        for(int index = 0; index < vocab.size(); index++)
        {
            qf = termFreq.get(vocab.get(index));
            if(bigData.getVocab().containsKey(vocab.get(index)))
            dfi = bigData.getVocab().get(vocab.get(index)).getDF();
            else
            dfi = 100000000;
            //first check if the query word is in the doc list of words
            if(termsForDoc.containsKey(vocab.get(index)))
            {
                fij = termsForDoc.get(vocab.get(index)).getFreq();
            }
            //doent exist so the weight will be 0 by default
            else
            {
                fij = 0.0;
            }
            
            if(fij != 0.0)
            answer += (1 + Math.log(1 + Math.log(fij)))/((1 - s) + 
                s*(dl/avdl))*qf*(Math.log((numDocs + 1)/dfi));
        }
        
        return answer;
    }
    
    // Finds the correlation based on the okapi similarity equation
    private double okapi(Document doc) {
       double piece1, piece2, piece3, sumRes = 0, k1 = 1.5, k2 = 500, b = 0.25;
       double docLen, currFreq, currDF = 0;
       String currTerm;
       Hashtable<String, Term> docTerms = doc.getTermsOfDoc();
    
       docLen = doc.getPerson().getFilteredText().length;
    
       for (int i = 0; i < vocab.size(); i++) {
          currTerm = vocab.get(i);
          if(docTerms.containsKey(currTerm))
          {
              currFreq = docTerms.get(currTerm).getFreq();
          }
          else
          {
              currFreq = 0;
          }
          
          if (currFreq != 0.0) {
             currDF = bigData.getVocab().get(currTerm).getDF();
    
             sumRes = Math.log((numDocs - currDF  + 0.5) / (currDF + 0.5)) *
                      (((k1 + 1) * currFreq) / (k1 * (1 - b + b * (docLen / avdl)))) *
                      ((k2 + 1) / (k2 + termFreq.get(currTerm))) * termFreq.get(currTerm);
          }
       }
    
       return sumRes;
    }
    
    //print out the query used
    public void printQuery()
    {
        System.out.print("\nTHE QUERY: ");
        for(int i = 0; i < origTerms.size(); i++)
        {
            System.out.print(origTerms.get(i) + " ");
        }
        System.out.println();
    }
}