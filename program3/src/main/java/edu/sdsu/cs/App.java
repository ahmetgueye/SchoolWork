//Chase Parker(821253141)
//Ahmet Gueye(821319753)
package edu.sdsu.cs;
import edu.sdsu.cs.datastructures.*;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.*;



public class App {

    private static List<String> cities;
    private static String vertFileName;

    public static void main(String[] args) {
        DirectedGraph<String> graph = new DirectedGraph<>();



        Scanner scan;
        if(args.length == 0)
            vertFileName = "/Users/ahmetgueye/program3/src/layout.csv";
        else if(args.length==1)
            vertFileName = args[0];
        else {
            System.out.println("Too many arguments");
            System.exit(0);
        }
        File file = new File(vertFileName);
        try {
            scan = new Scanner(file);

            while (scan.hasNext()) {
                String p = scan.nextLine();
                String[] cities = p.split(",");
                String first = cities[0];
                String second;
                if (cities.length <= 1) {
                    graph.add(first);
                } else {
                    second = cities[1];
                    graph.add(first);
                    graph.add(second);
                    graph.connect(first, second);


                }
            }
            System.out.println(((DirectedGraph<String>)graph).toString());
            System.out.println("Enter starting and ending vertices: (Format: Line Seperated)");

            Scanner scan1 = new Scanner(System.in);
            String input1;
            String input2;
            input1 = scan1.nextLine();
            input2 = scan1.nextLine();

            ArrayList newList = new ArrayList();

            try {
                newList = (ArrayList) graph.shortestPath(input1, input2);
                int distance = newList.size() - 1;
                System.out.println(newList.toString());
                System.out.println("distance: " + distance + " edges");}
            catch (NoSuchElementException e) {
                System.out.println("invalid input for shortest path algorithm");
            }



        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to open filename. Verify the file exists, is accessible, " +
                    "and meets the syntax requirements.");
        }

    }
}