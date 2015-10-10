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
    }
    
    //a very simple console UI used to test and run our program
    public static void consoleUI() throws FileNotFoundException
    {
        String input = "";
        Scanner reader = new Scanner(System.in);
        ArrayList<Double> tempIndices = new ArrayList<Double>();
        ArrayList<Point2D.Double> results;
        ArrayList<String> rawQuery;
        parse = new Parser(new File("Data.json"));
        Query query;
        int alg = 3;
        
        prepDocs();
        System.err.println("\nDocuments prepared");
        
        //looping structure for manipulating data
        while(!input.equals("q"))
        {
            System.err.println("\n1: Query from keyboard\n2: Query from file\n3: Choose an Algorithm");
            System.err.print("Enter a number for operations or q to quit: ");
            input = reader.nextLine();
            Scanner readFrom = null;

            if (!input.equals("q")) {
                //check 3 for searching the data using a query
                if(input.equals("1"))
                {
                    System.err.print("\nEnter your query and press CTRL-d when done: ");
                    readFrom = new Scanner(System.in);
                }
                else if(input.equals("2"))
                {
                    System.err.println("Enter the name of the file you wish to load from: ");
                    readFrom = new Scanner(new File(reader.nextLine()));
                }
                else if(input.equals("3"))
                {
                    System.out.println("Choose an algorithm:\n 1 CosSim\n 2 Okapi\n 3 PNW");
                    alg = Integer.parseInt(reader.nextLine());
                }
                
                //make sure we got a query
                if(input.equals("1") || input.equals("2"))
                {
                    rawQuery = parse.nextQuery(readFrom);
                    
                    System.out.println("Got Out Bitch!");
                    query = new Query(rawQuery, data, alg);
                    results = query.getResults();
                    
                    printResults(results);
                    query.printQuery();
                }
            }
        }
        
    }
    
    /*
     * Prepares the documents contained in Data.json for querying
     */
    private static void prepDocs() {
        Document doc = new Document(null);
        boolean firstDoc = true;
        ArrayList<Document> allDocs = new ArrayList();

        while(parse.hasNextDoc())
        {
            Person person = parse.nextDoc();
            
            //check for very first document
            if(firstDoc)
            {
                doc = new Document(person);
                firstDoc = false;
            }
            //else it wasnt and we need the previous docs vocab
            else
            {
                doc = new Document(person, doc.getClone());
            }
            
            doc.setDataUp(r, new Stemmer());
            allDocs.add(doc);         
        }
        
        data = new BigD(doc.getClone(), allDocs);        
    }
    
    /*
     * Prints out the top ten documents that are the results of the query 
     */
    private static void printResults(ArrayList<Point2D.Double> results) {
        
        System.out.println("\n" + results.size() + " results found:");
        
        for (int i = results.size() - 1, j = 1; i >= 0 ; i--, j++) {
            System.out.println("\n\t\t---------------------------------------------");
            System.out.println("\nThe " + results.get(i).x + " document has relevance " + results.get(i).y);
            System.out.println();           
            System.out.println(data.getDocs().get((int)results.get(i).x).getPerson().getOriginalText());
        }
    }
    
}
