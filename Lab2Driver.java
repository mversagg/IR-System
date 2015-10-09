/**
 * The main in our program
 * 
 * @author Ryan Zink & Matt Versaggi 
 * @version 1
 */
import java.io.*;
import java.util.*;
import java.awt.geom.Point2D;

public class Lab2Driver
{
    private static Parser parse;
    private static StopwordRemoval r;
    private static BigD data;
    
    
    public static void main(String args[]) throws FileNotFoundException
    {
        r = new StopwordRemoval("stopwords.txt");
        
        consoleUI();
      String[] parsed = "[pid:100, first:hannah beth, last:jackson, person".split(", ");
      for(int i = 0; i < parsed.length; i++)
      {
          System.out.println(i + ": " + parsed[i]);
      }
    }
    
    //a very simple console UI used to test and run our program
    public static void consoleUI() throws FileNotFoundException
    {
        String input = "";
        Scanner reader = new Scanner(System.in);
        ArrayList<Double> tempIndices = new ArrayList<Double>();
        ArrayList<Point2D.Double> results;
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
                        doc = new Document(person, doc.getClone());
                        //check if there is still more docs, if there are then clear the clone
                        if(parse.hasNextDoc())
                        {
                            //doc.clearClone();
                        }
                        doc.setDataUp(r, new Stemmer());
                    }
                    
                    //doc.createTermFrequencies();
                    
                    allDocs.add(doc);
                    
                    System.out.println("ADDED a New Doc: " + allDocs.size());
                }
                
                data = new BigD(doc.getClone(), allDocs);
                
            }
            //check 3 for searching the data using a query
            else if(input.equals("3"))
            {
                Query query;
                
                System.out.println("Use your own query or from a file? 1 own, 2 file");
                input = reader.nextLine();
                Scanner readFrom = null;
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
                
                ArrayList<String> rawQuery = new ArrayList();
                
                while (readFrom.hasNextLine())
                    rawQuery.addAll(Arrays.asList(parse.nextQuery(readFrom)));
                System.out.println("Got Out Bitch!");
                query = new Query(rawQuery, data);
                results = query.getResults();
                
                printResults(results);
                
            }
        }
        
    }

    private static void printResults(ArrayList<Point2D.Double> results) {
        
        for (int i = results.size() - 1, j = 1; i >= 0 ; i--, j++) {
            System.out.println("The " + results.get(i).x + " item is from docIndex " + results.get(i).y);
            for(int k = 0; k < data.getDocs().get((int)results.get(i).x).getPerson().getText().length; k++)
            System.out.print(data.getDocs().get((int)results.get(i).x).getPerson().getText()[k] + " ");
            
            System.out.println(".");
        }
    }
    
}
