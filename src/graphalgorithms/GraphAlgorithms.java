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
////////////////////////////////////////////////////////////////////////////////
class Node {
    int pe; //prim's edge
    int pv; //prim's vertex
    public Node(int e, int v) {
        pe = e;
        pv = v;
    }
}
////////////////////////////////////////////////////////////////////////////////
class pQueue {
    Node[] arr;
    int bottom = 0;
    public pQueue(int size) {
        arr = new Node[size * size];
    }
    public void push(Node in) {
        arr[bottom] = in;
        bottom++;
        sort();
    }
    public Node pop() {
        Node n = arr[0];
        arr[0] = null;
        sort();
        bottom--;
        return n;
    }
    public Node peek() {
        return arr[0];
    }
    public void sort() {
        Node temp;
        int clear = 0;
        if(bottom == 0) {
            //queue is empty
        } else if(bottom == 1) {
            //sorted
        } else if(bottom >= 2) {
            if(arr[0] == null) {
                for(int i = 0; i < bottom; i++) {
                    arr[i] = arr[i + 1];
                }
            }
            while(clear < bottom - 1) {
                clear = 0;
                for(int i = 0; i < bottom - 1; i++) {
                    if(arr[i].pe > arr[i + 1].pe) {
                        temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    } else {
                        clear++;
                    }
                }
            }
        }
    }
    public void print() {
        for(int i = 0; i < bottom; i++) {
            System.out.println(arr[i].pe + " " + arr[i].pv);
        }
    }
}
////////////////////////////////////////////////////////////////////////////////
class Graph {
    public int n;
    public String[] vert;
    public int[][] edge;
    public String[][] eString;
    public Graph(int dim, String[] inVert, String[][] inEString) {
        n = dim;
        vert = inVert;
        eString = inEString;
        fill(eString);
    }
    public void prim(int[][] in) {
        int[][] g = iCopy(edge);
        
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
    public int[][] iCopy(int[][] in) { //creates a copy of the input array
        int[][] copy = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                copy[i][j] = in[i][j];
            }
        }
        return copy;
    }
    public String[][] sCopy(String[][] in) { //creates a copy of the input array
        String[][] copy = new String[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                copy[i][j] = in[i][j];
            }
        }
        return copy;
    }
    public void printEach() {
        for(int i = 0; i < n; i++) {
            System.out.print("   " + vert[i]);
        }
        System.out.println();
        for(int i = 0; i < n; i++) {
            System.out.print(vert[i]);
            for(int j = 0; j < n; j++) {
                System.out.print("  " + eString[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.print(" ");
        for(int i = 0; i < n; i++) {
            System.out.print("  " + vert[i]);
        }
        System.out.println();
        for(int i = 0; i < n; i++) {
            System.out.print(vert[i]);
            for(int j = 0; j < n; j++) {
                System.out.print("  " + edge[i][j]);
            }
            System.out.println();
        }
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
        g.printEach();
        pQueue q = new pQueue(4);
        q.push(new Node(6, 1));
        q.push(new Node(5, 2));
        q.push(new Node(3, 5));
        q.push(new Node(4, 8));
        q.push(new Node(1, 3));
        q.print();
    }
}
