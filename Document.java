
/**
 * Ryan Zink and Matt Versaggi
 */
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

public class Document
{
    // instance variables - replace the example below with your own
    private Hashtable<String, Term> termData;
    private Hashtable<String, Term> cloneData;
    private ArrayList<String> allWords;
    private int maxFreq;
    private Person thisPerson;

    /**
     * Constructor for objects of class Document
     */
    public Document(Person newPerson)
    {
        // initialise instance variables
        thisPerson = newPerson;
        termData = new Hashtable(500);
        cloneData = new Hashtable(500);
        maxFreq = 0;
        allWords = new ArrayList();
    }
    
    public Document(Person newPerson, Hashtable<String, Term> newTermData)
    {
        thisPerson = newPerson;
        termData = new Hashtable(500);
        //System.out.println(termData.size());
        cloneData = newTermData;
        maxFreq = 0;
        allWords = new ArrayList();
    }
    
    //clear the clone when you are done with it
    public void clearClone()
    {
        cloneData = null;
    }
    
    //get the person who is responsible for this document object
    public Person getPerson()
    {
        return thisPerson;
    }
    //get all the terms for this document
    public Hashtable<String, Term> getTermsOfDoc()
    {
        return termData;
    }
    //retrieve this Documents clone data
    public Hashtable<String, Term> getClone()
    {
        return cloneData;
    }

    public ArrayList<String> getVocab() {
        return allWords;
    }
    
    public String getTermInfo(String target) {
        return termData.get(target).toString();
    }
    
    public void addTerm(String newTerm)
    {
        Term term;
        Term cTerm;
        
        //check to see if it already exists
        if(termData.containsKey(newTerm))
        {
            //System.out.println("Entering if and adding onto: " + newTerm);
            term = termData.get(newTerm);
            cTerm = cloneData.get(newTerm);
            
            //now increment the freq
            term.incrementFreq();
            //total number of times we have seen this word
            cTerm.incrementTotalCount();
            
            //System.out.println("Current freq for " + newTerm + " is " + term.getFreq());
            
            termData.put(newTerm, term);
            //cloneData.put(newTerm, new Term(newTerm, term.getCount(), term.getDF()));
            
            //System.out.println("--- " + termData.get(newTerm).toString() + " ---");
            //System.out.println("After taking out and putting back, freq is " + termData.get(newTerm).getFreq());
            
            //check maxFreq
            if(maxFreq < term.getFreq())
            {
                maxFreq = term.getFreq();
            }
            
            //System.out.println(newTerm + "'s frequency is " + term.getFreq());
        }
        //hasnt been seen in the term data but has been seen in an earlier doc
        else if(cloneData.containsKey(newTerm))
        {
            cTerm = cloneData.get(newTerm);
            //create the new term for the term data
            termData.put(newTerm, new Term(newTerm, 0, 0));
            
            term = termData.get(newTerm);
            term.incrementFreq();
            cTerm.incrementDF();
            cTerm.incrementTotalCount();
            
            //check maxFreq
            if(maxFreq < term.getFreq())
            {
                maxFreq = term.getFreq();
            }
        }
        //add a new term and incremnt the df for that term for the clone
        else
        {
            //System.out.println("Entering else and adding term: " + newTerm);
            
            term = new Term(newTerm);
            term.incrementFreq();
            //total number of times we have seen this word
            term.incrementTotalCount();
            term.incrementDF();
            
            //System.out.println("Current freq for " + newTerm + " is " + term.getFreq());
            
            termData.put(newTerm, term);
            //System.out.println("--- " + termData.get(newTerm).toString() + " ---");
            //create the clone data for this term
            cloneData.put(newTerm, new Term(newTerm, term.getCount(), term.getDF()));
            
            //check maxFreq
            if(maxFreq < term.getFreq())
            {
                maxFreq = term.getFreq();
            }
            //System.out.println("After taking out and putting back, freq is " + termData.get(newTerm).getFreq());
            //System.out.println(newTerm + "'s frequency is " + term.getFreq());
        }
        
    }
    
    //set up the data
    public void setDataUp(StopwordRemoval r, Stemmer s)
    {
        //loop through all the words in this persons utterance
        for(int index = 0; index < thisPerson.getText().length; index++)
        {
            if(!r.isStopWord(thisPerson.getText()[index]))
            {
                s.add(thisPerson.getText()[index].toCharArray(), 
                    thisPerson.getText()[index].length());
                s.stem();
                //System.out.println("ADDING: " + s.toString());
                addTerm(s.toString());
            }
        }
        
        createTermFrequencies();
    }
    
    //clacualte the term frequencices for all the words using the current max freq
    private void createTermFrequencies()
    {
        String tempWords;
        //System.out.println("The greatest number of an occurring term is: " + maxFreq);
        //loop through the elements
        for (Enumeration<String> e = termData.keys(); e.hasMoreElements();)
        {
            tempWords = e.nextElement();
            allWords.add(tempWords);
        }
        
        for(int i = 0; i < allWords.size(); i++)
        {
            Term temp = termData.get(allWords.get(i));
            double freq = (double)temp.getFreq();
            temp.setTermFreq(freq/maxFreq);
            termData.put(allWords.get(i), temp);
        }
    }
    
    //calculate the correct weights for these terms
    public void calcWeights(Hashtable<String, Double> idfs)
    {
        //looop through and set the weights
        for(int i = 0; i < allWords.size(); i++)
        {
            Term temp = termData.get(allWords.get(i));
            temp.setWeight(idfs.get(allWords.get(i))*(double)temp.getTermFreq());
        }
        
        
    }
    
}
