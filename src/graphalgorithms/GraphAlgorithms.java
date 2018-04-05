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
    public int n;
    public String[] vert;
    public int[][] edge;
    public String[][] eString;
    public Graph(int inN, String[] inVert, String[][] inEString) {
        n = inN;
        vert = inVert;
        eString = inEString;
        fill(eString);
    }
    public void fill(String[][] in) {
        edge = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(in[i][j].equals("!") == true) {
                    edge[i][j] = Integer.MAX_VALUE;
                } else {
                    edge[i][j] = Integer.parseInt(in[i][j]);
                }
            }
        }
    }
    public void printEach() {
        
    }
}
////////////////////////////////////////////////////////////////////////////////
class GraphAlgorithms {
    public static void main(String[] args) {
        String csvFile = "input.csv";
        BufferedReader br = null;
        String line = "";
        int n = 0;    //number of columns
        String[] vert = new String[0];
        String [][] eTemp = new String[0][0];
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            n = (line.length() - (line.length()/2));    //number of columns
            vert = new String[n];
            eTemp = new String[n][n];
            System.out.println(line);
            vert = line.split(",");
            int i = 0;
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
        
        Graph g = new Graph(n, vert, eTemp);
    }
}
