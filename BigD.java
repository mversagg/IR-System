
/**
 * Write a description of class BigD here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Hashtable;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Enumeration;

public class BigD
{
    // instance variables - replace the example below with your own
    private Hashtable<String, Term> vocab;
    private Hashtable<String, Double> idf;
    private ArrayList<Document> docs;
    private ArrayList<String> vocabWords;
    /**
     * Constructor for objects of class BigD
     */
    public BigD(Hashtable<String, Term> newVoc, ArrayList<Document> newDocs)
    {
        vocab = new Hashtable(newVoc);
        docs = new ArrayList(newDocs);
        vocabWords = new ArrayList();
        idf = new Hashtable(500);
        calcIDFs();
        
        //loop through and calc weights on all docs
        for(int i = 0; i < docs.size(); i++)
        {
            docs.get(i).calcWeights(idf);
        }
        
        System.out.println("Number of documents: " + docs.size());
        System.out.println("Size of vocab: " + vocabWords.size());
        
        System.out.print("ALL WORDS: ");
        for (int i = 0; i < vocabWords.size(); i++)
            System.out.print(vocabWords.get(i) + ", ");
        System.out.println();
            
        for (int i = 0; i < docs.size(); i++) {
            Document curr = docs.get(i);
            System.out.println("\nOn Doc #" + i);
            ArrayList<String> docVocab = curr.getVocab(); 
            for (int j = 0; j < docVocab.size(); j++) {
                String currTerm = docVocab.get(j);
                System.out.println(curr.getTermInfo(currTerm));
                System.out.println("DF of " + currTerm + " is " + vocab.get(currTerm).getDF());
                System.out.println("IDf of " + currTerm + " is " +  idf.get(currTerm));
            }
        }
        
        
    }

    private void calcIDFs()
    {
        String tempWords;
        //loop through the elements
        for (Enumeration<String> e = vocab.keys(); e.hasMoreElements();)
        {
            tempWords = e.nextElement();
            vocabWords.add(tempWords);
        }
        
        //use the string of vocab words to calculate the idf for each term
        for(int i = 0; i < vocabWords.size(); i++)
        {
            Term temp = vocab.get(vocabWords.get(i));
            double df = (double) temp.getDF();
            System.out.println(vocabWords.get(i) + "'s df is " + df);
            double tIDF;
            if(df != 0)
                tIDF = Math.log10((double)docs.size()/df);
            else
                tIDF = 0;
            idf.put(vocabWords.get(i), tIDF);
            System.out.println(vocabWords.get(i) + "'s idf is " + tIDF);
        }
    }
    
    //give the hashtable containing all the idfs for the list of documents
    public Hashtable<String, Double> getIDFs()
    {
        return idf;
    }
    
    //get the entire vocab for this BigD
    public Hashtable<String, Term> getVocab()
    {
        return vocab;
    }
    
    //return the arraylist of docs for the query to use only
    public ArrayList<Document> getDocs()
    {
        return docs;
    }
}
