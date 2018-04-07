package graphalgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Authors: Bailey Kouwenhoven and J. Beckett Sweeney
 * Date: 7 April 2018
 * Overview: This program reads in a .csv file containing an adjacency matrix
 *      for a graph, then runs several algorithms on that graph to find 
 *      different information about the graph.
 * Special Instructions: 
 *      - In the input file, represent a lack of edge as either an infinity
 *          symbol or as a question mark.
 *      - Do not input a graph that has any vertices that
 *          connect to themselves.
 *      - Prim's and Kruskal's Algorithm's require a weighted undirected graph.
 *      - Floyd-Warshall's Algorithm requires a weighted directed graph.
 *      - When testing, input an appropriate graph, then at the very bottom of
 *          the main method, comment out whichever algorithms are inappropriate
 *          for the input graph.
 */
////////////////////////////////////////////////////////////////////////////////
class Node {
    int pe;     //prim's edge weight
    int pvs;    //prim's starting vertex
    int pve;    //prim's ending vertex
    public Node(int e, int vs, int ve) {
        pe = e;
        pvs = vs;
        pve = ve;
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
            //only one element
        } else if(bottom >= 2) {
            if(arr[0] == null) {
                for(int i = 0; i < bottom - 1; i++) {
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
    public void clear() {
        for(int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
        bottom = 0;
    }
    public void print() {
        for(int i = 0; i < bottom; i++) {
            System.out.println(arr[i].pe + " " + arr[i].pvs + "-" + arr[i].pve);
        }
        System.out.println();
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
        pQueue q = new pQueue(n);
        int[] taken = new int[n];   //vertices already included
        Node[] tree = new Node[n - 1];
        int iTaken = 0;     //current empty taken slot
        int iTree = 0;      //current empty tree slot
        int start = (int)(Math.random() * n);
        
        taken[iTaken] = start;
        iTaken++;
        while(tree[tree.length - 1] == null) {
            for(int i = 0; i < iTaken; i++) {
                for(int j = 0; j < n; j++) {
                    if(j != taken[i]) {
                        q.push(new Node(g[taken[i]][j], taken[i], j));
                    }
                }
            }
            int clear = 0;
            while(clear < iTree) {
                clear = 0;
                for(int i = 0; i < iTree; i++) {
                    if((q.peek().pvs == tree[i].pvs && q.peek().pve == tree[i].pve) || (q.peek().pvs == tree[i].pve && q.peek().pve == tree[i].pvs)) {
                        q.pop();
                    } else {
                        clear++;
                    }
                }
            }
            tree[iTree] = q.pop();
            taken[iTaken] = tree[iTree].pve;
            iTree++;
            iTaken++;
            q.clear();
        }
        System.out.println("Prim's Tree: ");
        for(int i = 0; i < tree.length; i++) {
            System.out.print(vert[tree[i].pvs] + vert[tree[i].pve] + " ");
        }
        System.out.println();
    }
    public void fill(String[][] in) {
        edge = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(in[i][j].equals("?") == true) {
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
        System.out.println();
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
        g.prim(g.edge);
    }
}
