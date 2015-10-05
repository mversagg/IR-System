
/**
Ryan Zink and Matt Versaggi
 */
import java.io.*;
import java.util.*;

public class StopwordRemoval
{
    // instance variables - replace the example below with your own
    private File stopRFile;
    private Scanner in;
    private Hashtable<String, Boolean> stopWords;
    
    /**
     * Constructor for objects of class StopwordRemoval
     */
    public StopwordRemoval(String fileName) throws FileNotFoundException
    {
        // initialise instance variables
        stopRFile = new File(fileName);
        in = new Scanner(stopRFile);
        stopWords = new Hashtable(100);
        
        setUpStopWords();
    }

    /**
     * Set up the data structure reading from the file
     */
    private void setUpStopWords()
    {
        String word;
        //loop through file while it ahs words
        while(in.hasNextLine())
        {
            word = in.nextLine();
            
            stopWords.put(word, true);
        }
        
        System.out.println(stopWords.size());
    }
    
    public boolean isStopWord(String word)
    {
        Boolean check = stopWords.get(word);
        //check to see if it exists in the vocab
        if(check != null)
        {
            return true;
        }
        //else it is not a stop word
        else
        {
            return false;
        }
        
    }
}
