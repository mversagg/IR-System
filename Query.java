
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

public class Query
{
    // instance variables - replace the example below with your own
    private Hashtable<String, Double> termWeights;
    private Hashtable<String, Integer> termFreq;
    private Hashtable<Integer, Double> relavDocCorrelation;
    private String[] terms;
    private ArrayList<String> vocab;
    private BigD bigData;
    //this will be the variable containing the number of total docs
    private int numDocs;
    //the average length of document
    private double avdl;

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
        calcAVDL();
        //calc the term frequencies inside the query
        createTermFreq();
        calcWeights();
        calcRelavance();

    }

public double okapi(Document doc) {
   double piece1, piece2, piece3, sumRes = 0, k1 = 1.5, k2 = 900, b = .75;
   int docLen, currFreq, currDF = 0;
   String currTerm;
   HashTable<String, Term> docTerms = doc.getTermsofDoc();

   docLen = doc.getPerson().getText().length;

   for (int i = 0; i < vocab.size(); i++) {
      currTerm = vocab.get(i);
      currFreq = docTerms.get(currTerm).getFreq();
      if (currFreq != 0) {
         for (int j = 0; j < ; j++) {
            currDF = bigData.getVocab().get(currTerm).getDF();

            sumRes = log((numDocs - currDF  + 0.5) / (currDF + 0.5)) *
                     (((k1 + 1) * currFreq) / (k1 * (1 - b + b * (docLen / avdl)))) *
                     ((k2 + 1) / (k2 + termFreq.get(currTerm))) * termFreq.get(currTerm);
         }
      }
   }

   return sumRes;
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
        numDocs = docs.size();
        Hashtable<String, Term> termsForDoc;
        double wDoc;
        //we have vocab to use for a dictionary
        //loop through the docs one by one
        for(int docIndex = 0; docIndex < docs.size(); docIndex++)
        {
            wDoc = cosSimilarity(docs.get(docIndex));
            relavDocCorrelation.put(docIndex, wDoc);
            System.out.println("Relavence of Doc " + docIndex + " is: " + wDoc);
        }

    }
    
    //little function to calculate the average length of documents in bigd
    private void calcAVDL()
    {
        ArrayList<Document> docs = bigData.getDocs();
        double totalNumWords = 0;
        //loop through the docs one by one
        for(int docIndex = 0; docIndex < docs.size(); docIndex++)
        {
            totalNumWords += docs.get(docIndex).getPerson().getText().length;
        }
        
        avdl = totalNumWords/docs.size();
    }
    //do the cosine similarity
    private Double cosSimilarity(Document theDoc)
    {
        Hashtable<String, Term> termsForDoc = theDoc.getTermsOfDoc();
        double tempWeight, qWeight, qDenom = 0, tDenom = 0, qtNumer = 0;
        
        //now go through all the terms in the doc and get the weight
        //for each word that is in the doc and the query, vocab is the list of words in the q
        for(int index = 0; index < vocab.size(); index++)
        {
            qWeight = termWeights.get(vocab.get(index));
            //first check if the query word is in the doc list of words
            if(termsForDoc.contains(vocab.get(index)))
            {
                tempWeight = termsForDoc.get(index).getWeight();
            }
            //doent exist so the weight will be 0 by default
            else
            {
                tempWeight = 0.0;
            }
            
            qtNumer += qWeight*tempWeight;
            qDenom += qWeight*qWeight;
            tDenom += tempWeight*tempWeight;
        }

        double answer = qtNumer/(Math.sqrt(qDenom*tDenom));
        
        return answer;
    }
    
    //do the pivoted normalization weighting
    private Double pnw(Document theDoc)
    {
        Hashtable<String, Term> termsForDoc = theDoc.getTermsOfDoc();
        double dfi, fij, dl = theDoc.getPerson().getText().length, qf, answer = 0;
        double s = 0.2; //doc length normalization parameter
        
        //now go through all the terms in the doc and get the weight
        //for each word that is in the doc and the query, vocab is the list of words in the q
        for(int index = 0; index < vocab.size(); index++)
        {
            qf = termFreq.get(vocab.get(index));
            dfi = bigData.getVocab().get(vocab.get(index)).getDF();
            //first check if the query word is in the doc list of words
            if(termsForDoc.contains(vocab.get(index)))
            {
                fij = termsForDoc.get(index).getFreq();
            }
            //doent exist so the weight will be 0 by default
            else
            {
                fij = 0.0;
            }
            
            answer+= (1 + Math.log1p(1 + Math.log1p(fij)))/((1 - s) + 
                s*(dl/avdl))*qf*(Math.log1p((numDocs + 1)/dfi));
        }
        
        return answer;
    }
}
