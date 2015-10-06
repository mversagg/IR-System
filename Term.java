
/**
 * Ryan Zink and Matt Versaggi
 */
public class Term
{
    // instance variables - replace the example below with your own
    private String term;
    private int freq;
    private double weight;
    private int totalCount;
    private int df;
    private double termFreq;
    
    /**
     * Constructor for objects of class Term
     */
    public Term(String newTerm)
    {
        // initialise instance variables
        term = newTerm;
        freq = 0;
        weight = -1;
        totalCount = 0;
        df = 0;
        termFreq = 0;
    }
    
    /*
    public Term(String newTerm, int newCount)
    {
        term = newTerm;
        freq = 0;
        weight = -1;
        totalCount = newCount;
        termFreq = 0;
    }*/
    
    //use this call fro adding a new term with a new df for cloning only
    public Term(String newTerm, int newCount, int newDF)
    {
        term = newTerm;
        freq = 0;
        weight = -1;
        totalCount = newCount;
        df = newDF;
        termFreq = 0;
    }
    
    public double getTermFreq()
    {
        return termFreq;
    }
    
    public int getDF()
    {
        return df;
    }
    
    //return the number of times this term appears in a single document
    public int getFreq()
    {
        return freq;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public void incrementDF()
    {
        df++;
    }

    //increment the freq of the word
    public void incrementFreq()
    {
        freq++;
    }
    
    //set the weight of the term
    public void setWeight(double newWeight)
    {
        weight = newWeight;
    }
    
    //set the total count of how many times this term has been seen at this point
    public int getCount()
    {
        return totalCount;
    }
    
    //increment the entire count of this term
    public void incrementTotalCount()
    {
        totalCount++;
    }
    
    public void setTermFreq(double newTF)
    {
        termFreq = newTF;
    }
    
    public String toString() {
        return "Term: " + term + " "
             + "Frequency: " + freq + " " 
             + "Weight: " + weight + " "
             + "TotalCount: " + totalCount + " " 
             + "Tf: " + termFreq;
    }
}
