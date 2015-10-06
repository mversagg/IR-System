
/**
 * The main in our program
 * 
 * @author Ryan Zink & Matt Versaggi 
 * @version 1
 */
import java.io.*;
import java.util.*;

public class Lab2Driver
{
    private static Parser parse;
    private static StopwordRemoval r;
    private static BigD data;
    
    
    public static void main(String args[]) throws FileNotFoundException
    {
        r = new StopwordRemoval("stopwords.txt");
        
        consoleUI();
    }
    
    //a very simple console UI used to test and run our program
    public static void consoleUI() throws FileNotFoundException
    {
        String input = "";
        Scanner reader = new Scanner(System.in);
        ArrayList<Double> tempIndices = new ArrayList<Double>();
        //looping structure for manipulating data
        while(!input.equals("q"))
        {
            System.out.println("\n1: To Load a JSON File\n2: Setup the Documents\n3: Query");
            System.out.print("Enter a number for operations or q to quit: ");
            input = reader.nextLine();
            //System.out.println(input);

            //check for possible commands
            //1 means we would like to pull data from a JSON
            if(input.equals("1"))
            {
                System.out.print("Enter the name of the JSON file you wish to load from: ");
                input = reader.nextLine();
                parse = new Parser(new File(input));
            }
            //2 is setting up the documents and all their variables
            else if(input.equals("2"))
            {
                System.out.print("");
                Document doc = new Document(null);
                boolean firstDoc = true;
                ArrayList<Document> allDocs = new ArrayList();
                //
                while(parse.hasNextDoc())
                {
                    Person person = parse.nextDoc();
                    //check for very first document
                    if(firstDoc)
                    {
                        doc = new Document(person);
                        doc.setDataUp(r, new Stemmer());
                        firstDoc = false;
                    }
                    //else it wasnt and we need the previous docs vocab
                    else
                    {
                        ArrayList<String> arr = doc.getVocab();
                        for (int i = 0; i < arr.size(); i++) {
                            //String tempWord = arr.get(i);
                            //System.out.println(doc.getTermInfo(tempWord));
                        }
                        doc = new Document(person, new Hashtable(doc.getClone()));
                        doc.setDataUp(r, new Stemmer());
                    }
                    
                    //doc.createTermFrequencies();
                    System.out.println("ADDED a New Doc");
                    allDocs.add(doc);
                }
                
                data = new BigD(doc.getClone(), allDocs);
                
            }
            //check 3 for searching the data using a query
            else if(input.equals("3"))
            {
                System.out.println("Use your own query or from a file? 1 own, 2 file");
                input = reader.nextLine();
                Scanner readFrom;
                if(input.equals("1"))
                {
                    readFrom = new Scanner(System.in);
                }
                else if(input.equals("2"))
                {
                    System.out.println("Enter the name of the file you wish to load from: ");
                    input = reader.nextLine();
                    readFrom = new Scanner(new File(input));
                }
            }
        }
    }

  
}
