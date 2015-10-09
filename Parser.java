
/**
 * Write a description of class Parser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
/**
 * Matt Versaggi
 * Ryan Zink
 * CPE 466
 * Lab 1
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Parser {
   
   private Scanner in;

   public Parser(File f) throws FileNotFoundException {
      in = new Scanner(f);
   }

   // Grabs the next document and parses it into a Person object
   public Person nextDoc() {
      return parseLine();
   }
   
   // Parses a query provided by the input stream in the Scanner
   //   and returns the parsed version in the form of a String[]
   public ArrayList<String> nextQuery(Scanner queryStream) {
      return parseQuery(queryStream);
   }
   
   //check for a next doc
   public boolean hasNextDoc()
   {
      return in.hasNextLine();
   }

   // Reads in a query line by line and parses the
   //   entire thing into a String array
   private ArrayList<String> parseQuery(Scanner queryStream) {
       ArrayList<String> toReturn =  new ArrayList();
       
       while (queryStream.hasNextLine()) {
           toReturn.addAll(toListSansDash(removePunc(queryStream.nextLine()).replace(":", "").replace(",", "").split(" ")));
       }
        
       return toReturn;
   }
   
   private ArrayList<String> toListSansDash(String[] target) {
       ArrayList<String> newArr = new ArrayList();
       
       for (int i = 0; i < target.length; i++) {
           if (!target[i].equals("-"))
               newArr.add(target[i]);
       }
       
       return newArr;
   }
   
   private String removePunc(String target) {
       String toReturn =  target.trim().toLowerCase();
       
       toReturn = toReturn.trim().toLowerCase();
       toReturn = toReturn.replace("{", "");
       toReturn = toReturn.replace("}", "");
       toReturn = toReturn.replace(";", "");
       toReturn = toReturn.replace("[", "");
       toReturn = toReturn.replace("]", "");              
       toReturn = toReturn.replace("(", "");
       toReturn = toReturn.replace(")", "");
       toReturn = toReturn.replace("\\", "");
       toReturn = toReturn.replace("/", "");
       toReturn = toReturn.replace("!", "");
       toReturn = toReturn.replace("?", "");
       toReturn = toReturn.replace(".", "");
       toReturn = toReturn.replace("\"", "");
       toReturn = toReturn.replace("'", "");

       return toReturn;
   }
   
   // Reads in the text file line by line and parses word by word;
   // returns null if it has no more input
   private Person parseLine() {
      String[] parsed;
      
      if(!in.hasNextLine())
          return null;
      else {
          String line = in.nextLine();
          return makeBaby(removePunc(line), line);
      }
   }

   private Person makeBaby(String modifiedLine, String origLine) {
      String first, last, type, date, house, committee;
      String[] parsed = modifiedLine.split(", ", 8);
      String[] text;
      ArrayList<String> temp;
      int pid;
      
      pid = Integer.valueOf(parsed[0].split(":", 2)[1]);
      first = parsed[1].split(":", 2)[1];
      last = parsed[2].split(":", 2)[1];
      type = parsed[3].split(":", 2)[1];
      date = parsed[4].split(":", 2)[1];
      house = parsed[5].split(":", 2)[1];
      committee = parsed[6].split(":", 2)[1];
      text = parsed[7].split(":", 2)[1].replace(",", "").replace(":", "").split(" ");
      temp = toListSansDash(text);
      text = temp.toArray(new String[temp.size()]);
      
      return new Person(pid, first, last, type, date, house, committee, text, origLine);
   }
}

// Used by Parser to store both metadata and the
//    text of the utterance in raw form
class Person {
   private int pid;
   private String first;
   private String last;
   private String type;
   private String date;
   private String house;
   private String committee;
   private String[] filteredText;
   private String originalText;

   public Person(int p, String f, String l, String t,
    String d, String h, String c, String[] u, String o) {
      
      pid = p;
      first = f;
      last = l;
      type = t;
      date = d;
      house = h;
      committee = c;
      filteredText = u;
      originalText = o;

   }

   public int getPid() {
      return pid;
   }

   public String getFirst() {
      return first;
   }

   public String getLast() {
      return last;
   }

   public String getType() {
      return type;
   }

   public String getDate() {
      return date;
   }

   public String getHouse() {
      return house;
   }

   public String getCommittee() {
      return committee;
   }

   public String[] getFilteredText() {
      return filteredText;
   }
   
   public String getOriginalText() {
       return originalText;
    }
}
