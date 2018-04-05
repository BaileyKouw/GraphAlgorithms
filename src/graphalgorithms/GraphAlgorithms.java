package graphalgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Authors: Bailey Kouwenhoven and J. Beckett Sweeney
 * Date: 
 * Overview: 
 */
class Graph {
    public Graph(){};
    public int[][] fill(String[][] stringIn, int[][] intIn) {
        
        return 
    }
}
////////////////////////////////////////////////////////////////////////////////
class GraphAlgorithms {
    public static void main(String[] args) {
        String csvFile = "input.csv";
        BufferedReader br = null;
        String line = "";
        Graph g = new Graph();
        int i = 0;
        String[] vert = new String[0];          //array of vertices (A, B, etc.)
        int[][] edge = new int[0][0];           //array of edges (1, 12, etc.)
        String[][] eTemp = new String[0][0];    //array of edges as Strings
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            int n = (line.length() - (line.length()/2));    //number of columns
            vert = new String[n];
            edge = new int[n][n];
            eTemp = new String[n][n];
            System.out.println(line);
            vert = line.split(",");
            while((line = br.readLine()) != null) {
                System.out.println(line);
                eTemp[i] = line.split(",");
                i++;
            }
        } catch(FileNotFoundException err) {
            err.printStackTrace();
        } catch(IOException err) {
            err.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(IOException err) {
                    err.printStackTrace();
                }
            }
        }   //end of try
        
        edge = g.fill(eTemp, edge);
    }
}
