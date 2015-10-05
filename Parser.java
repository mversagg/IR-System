
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

   public Person nextDoc() {
      return parseLine();
   }
   
   //check for a next doc
   public boolean hasNextDoc()
   {
       return in.hasNextLine();
   }

   // Reads in the text file line by line and parses word by word
   //returns null if it has no more input
   private Person parseLine() {
      String[] parsed;
      
      if(!in.hasNextLine())
      {
          return null;
      }
      else
      {
          String line = in.nextLine();
          
          line = line.trim().toLowerCase();
          line = line.replace("{", "");
          line = line.replace("}", "");
          line = line.replace(",", "");
          line = line.replace(";", "");
          line = line.replace("(", "");
          line = line.replace(")", "");
          line = line.replace("\"", "");
          line = line.replace("!", "");
          line = line.replace("?", "");
          line = line.replace(".", "");
      
          return makeBaby(line);
      }
   }

   private Person makeBaby(String line) {
      String first, last, type, date, house, committee;
      String[] parsed = line.split(" ", 8);
      String[] text;
      int pid;

      pid = Integer.valueOf(parsed[0].split(":", 2)[1]);
      first = parsed[1].split(":", 2)[1];
      last = parsed[2].split(":", 2)[1];
      type = parsed[3].split(":", 2)[1];
      date = parsed[4].split(":", 2)[1];
      house = parsed[5].split(":", 2)[1];
      committee = parsed[6].split(":", 2)[1];
      text = parsed[7].split(":", 2)[1].split(" ");
      
      return new Person(pid, first, last, type, date, house, committee, text);
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
   private String[] utterance;

   public Person(int p, String f, String l, String t,
    String d, String h, String c, String[] u) {
      
      pid = p;
      first = f;
      last = l;
      type = t;
      date = d;
      house = h;
      committee = c;
      utterance = u;
      
      System.out.print("Utterance: ");
      for (int i = 0; i < u.length; i++)
        System.out.print(u[i]);
      System.out.println();
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

   public String[] getText() {
      return utterance;
   }
}
