package graphalgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Authors: Bailey Kouwenhoven and J. Beckett Sweeney
 * Date: 10 Apr 2018
 * Overview: This program reads in a .csv file containing an adjacency matrix
 *      for a graph, then runs several algorithms on that graph to find 
 *      different information about the graph.
 * Special Instructions: 
 *      - In the input file (input.csv), represent a lack of edge as either an infinity
 *          symbol or as a question mark.
 *      - Prim's and Kruskal's Algorithm's require a weighted undirected graph.
 *      - Floyd-Warshall's Algorithm requires either a weighted directed graph
 *          or a weighted undirected graph.
 *      - When testing, input an appropriate graph, then at the very bottom of
 *          the main method, comment out whichever algorithms are inappropriate
 *          for the input graph. (all three work for undirected, only Floyd-
 *          Warshall's works for directed)
 */
////////////////////////////////////////////////////////////////////////////////
class Node {

    int e;     //edge weight
    int vs;    //starting vertex
    int ve;    //ending vertex

    public Node(int eIn, int vsIn, int veIn) {
        e = eIn;
        vs = vsIn;
        ve = veIn;
    }
}   //end of Node class
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
        if (bottom == 0) {
            //queue is empty
        } else if (bottom == 1) {
            //only one element
        } else if (bottom >= 2) {
            if (arr[0] == null) {
                for (int i = 0; i < bottom - 1; i++) {
                    arr[i] = arr[i + 1];
                }
            }
            while (clear < bottom - 1) {
                clear = 0;
                for (int i = 0; i < bottom - 1; i++) {
                    if (arr[i].e > arr[i + 1].e) {
                        temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                    } else {
                        clear++;
                    }
                }
            }
        }
    }   //end of sort()

    public void clear() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = null;
        }
        bottom = 0;
    }

    public void print() {
        for (int i = 0; i < bottom; i++) {
            System.out.println(arr[i].e + " " + arr[i].vs + "-" + arr[i].ve);
        }
        System.out.println();
    }
}   //end of pQueue class
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
        int[][] g = iCopy(in);                  //matrix
        pQueue q = new pQueue(n);               //priority queue
        int[] taken = new int[n];               //vertices already included
        Node[] tree = new Node[n - 1];          //tree
        int iTaken = 0;                         //taken iterator
        int iTree = 0;                          //tree iterator
        int start = (int) (Math.random() * n);  //random starting vertex

        taken[iTaken] = start;
        iTaken++;
        while (tree[tree.length - 1] == null) {
            for (int i = 0; i < iTaken; i++) {
                for (int j = 0; j < n; j++) {
                    if (j != taken[i]) {
                        if (g[taken[i]][j] > 0) {
                            q.push(new Node(g[taken[i]][j], taken[i], j));
                        }
                    }
                }
            }
            int clear = 0;
            while (clear < iTree) {
                clear = 0;
                for (int i = 0; i < iTree; i++) {
                    if ((q.peek().vs == tree[i].vs && q.peek().ve == tree[i].ve) || (q.peek().vs == tree[i].ve && q.peek().ve == tree[i].vs)) {
                        q.pop();
                    } else {
                        boolean safeS = true;
                        boolean safeE = true;
                        for (int j = 0; j < iTaken; j++) {
                            if (q.peek().vs == taken[j]) {
                                safeS = false;
                            }
                            if (q.peek().ve == taken[j]) {
                                safeE = false;
                            }
                        }
                        if (safeS == false && safeE == false) {
                            q.pop();
                        } else {
                            clear++;
                        }
                    }
                }
            }
            tree[iTree] = q.pop();
            taken[iTaken] = tree[iTree].ve;
            iTree++;
            iTaken++;
            q.clear();
        }
        System.out.println("Prim's Tree: ");
        for (int i = 0; i < tree.length; i++) {
            System.out.print(vert[tree[i].vs] + vert[tree[i].ve] + " ");
        }
        System.out.println();
        System.out.println();
    }   //end of prim()

    public void kruskal(int[][] in) {
        int[][] g = iCopy(in);              //matrix
        pQueue q = new pQueue(n);           //priority queue
        Node[] tree = new Node[n - 1];      //tree
        int iTree = 0;                      //tree iterator
        Node temp;
        int[][] taken = new int[n][n + 2];  //2D array of taken vertices
        int iTaken = 0;                     //taken iterator
        for (int i = 0; i < taken.length; i++) {
            for (int j = 0; j < taken[i].length; j++) {
                taken[i][j] = -2;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    if (g[i][j] > 0) {
                        q.push(new Node(g[i][j], i, j));
                    }
                }
            }
        }
        while (tree[tree.length - 1] == null) {
            temp = q.pop();
            if (temp == null) {
                break;
            }
            int toInsert = 0;
            boolean found = false;
            for (int i = 0; i < taken.length; i++) {
                for (int j = 0; j < taken[i].length; j++) {
                    if ((taken[i][j] == temp.vs || taken[i][j] == temp.ve) && found == false) {
                        found = true;
                        toInsert = i;
                    }
                }
            }
            if (found == false && iTaken != 0) {
                iTaken++;
            } else {
                iTaken = toInsert;
            }
            boolean safeS = true;
            boolean safeE = true;
            for (int i = 0; i < taken[iTaken].length; i++) {
                if (temp.vs == taken[iTaken][i]) {
                    safeS = false;
                }
                if (temp.ve == taken[iTaken][i]) {
                    safeE = false;
                }
            }
            if (safeS == false && safeE == false) {
                //not safe
            } else if (safeS == true && safeE == true && taken[iTaken][0] != -2) {
                while (taken[iTaken][0] != -2) {
                    iTaken++;
                }
                boolean inserted = false;
                while (inserted == false) {
                    for (int i = 0; i < n; i++) {
                        if (taken[iTaken][i] == -2 && inserted == false) {
                            taken[iTaken][i] = temp.vs;
                            taken[iTaken][i + 1] = temp.ve;
                            inserted = true;
                        }
                    }
                }
                compress(taken[iTaken]);
                boolean merged = false;
                for (int i = 0; i < taken.length; i++) {
                    for (int j = 0; j < taken.length; j++) {
                        for (int m = 0; m < taken[i].length; m++) {
                            for (int n = 0; n < taken[j].length; n++) {
                                if (taken[i][m] != -2 && taken[i][m] == taken[j][n] && i != j) {
                                    merge(taken[i], taken[j]);
                                    for (int k = j; k < taken.length - 1; k++) {
                                        merge(taken[k], taken[k + 1]);
                                        merged = true;
                                        compress(taken[k]);
                                    }
                                }
                            }
                        }
                    }
                }
                if (merged == true) {
                    //iTaken--;
                }
                tree[iTree] = temp;
                iTree++;
            } else {
                boolean inserted = false;
                while (inserted == false) {
                    for (int i = 0; i < n; i++) {
                        if (taken[iTaken][i] == -2 && inserted == false) {
                            taken[iTaken][i] = temp.vs;
                            taken[iTaken][i + 1] = temp.ve;
                            inserted = true;
                        }
                    }
                }
                compress(taken[iTaken]);

                boolean merged = false;
                for (int i = 0; i < taken.length; i++) {
                    for (int j = 0; j < taken.length; j++) {
                        for (int m = 0; m < taken[i].length; m++) {
                            for (int n = 0; n < taken[j].length; n++) {
                                if (taken[i][m] != -2 && taken[i][m] == taken[j][n] && i != j) {
                                    merge(taken[i], taken[j]);
                                    for (int k = j; k < taken.length - 1; k++) {
                                        merge(taken[k], taken[k + 1]);
                                        merged = true;
                                        compress(taken[k]);
                                    }
                                }
                            }
                        }
                    }
                }
                if (merged == true) {
                    //iTaken--;
                }
                tree[iTree] = temp;
                iTree++;
            }
        }

        System.out.println("Kruskal's Tree: ");
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                System.out.print(vert[tree[i].vs] + vert[tree[i].ve] + " ");
            }
        }
        System.out.println();
        System.out.println();
    }   //end of kruskal

    public void merge(int[] inA, int[] inB) {
        int insertsNeeded = 0;
        for (int i = 0; i < inB.length; i++) {
            if (inB[i] != -2) {
                insertsNeeded++;
            }
        }
        int insertsDone = 0;
        for (int i = 0; i < inB.length; i++) {
            for (int j = 0; j < inA.length; j++) {
                if (inB[i] != -2) {
                    if (inB[i] == inA[j]) {
                        inB[i] = -2;
                    } else if (inB[i] != inA[j] && inA[j] == -2 && insertsDone < insertsNeeded) {
                        inA[j] = inB[i];
                        inB[i] = -2;
                        insertsDone++;
                    }
                }
            }
        }
    }

    public void compress(int[] in) {
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in.length - 2; j++) {
                if (in[j] != -2 && in[j + 1] != -2) {
                    if (in[j] > in[j + 1]) {
                        int temp = in[j];
                        in[j] = in[j + 1];
                        in[j + 1] = temp;
                    } else if (in[j] == in[j + 1]) {
                        if ((j + 1) >= in.length) {
                            in[j + 1] = -2;
                        } else {
                            in[j + 1] = in[j + 2];
                        }
                    }
                }
            }
        }
    }

    public void printN(Node[] in, String s) {
        System.out.print(s + ": ");
        for (int i = 0; i < in.length; i++) {
            if (in[i] != null) {
                System.out.print(in[i].vs + "" + in[i].ve + " ");
            }
        }
        System.out.println();
    }

    public void printI(int[] in, String s) {
        System.out.print(s + ": ");
        for (int i = 0; i < in.length; i++) {
            System.out.print(in[i] + " ");
        }
        System.out.println();
    }

    public void floydWarshall(int[][] in) {
        int[][] g = iCopy(in);
        for (int i = 0; i < n; i++) {
            g[i][i] = 0;
        }
        System.out.println("Initial Floyd-Warshall's Matrix:");
        iPrint(g);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (g[j][k] > g[j][i] + g[i][k] && g[j][i] != -1 && g[i][k] != -1) {
                        g[j][k] = g[j][i] + g[i][k];
                        fwPrint(g, j, k);
                    }
                }
            }
        }
        System.out.println("Final Floyd-Warshall Matrix:");
        iPrint(g);
    }   //end of floydWarshall

    public void fill(String[][] in) {
        edge = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (in[i][j].equals("?") == true) {
                    edge[i][j] = -1;
                } else {
                    edge[i][j] = Integer.parseInt(in[i][j]);
                }
            }
        }
    }

    public int[][] iCopy(int[][] in) { //creates a copy of the input array
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = in[i][j];
            }
        }
        return copy;
    }

    public String[][] sCopy(String[][] in) { //creates a copy of the input array
        String[][] copy = new String[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = in[i][j];
            }
        }
        return copy;
    }

    public void fwPrint(int[][] in, int x, int y) {
        /*print method for Floyd-Warshall's Algorithm. prints out integer
            matrix with bars on either side of the value that has changed.*/
        for (int i = 0; i < n; i++) {
            System.out.print("   " + vert[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(vert[i]);
            for (int j = 0; j < n; j++) {
                System.out.print(" ");
                if (i == x && j == y) {
                    if (in[i][j] < 10 && in[i][j] >= 0) {
                        System.out.print("|" + in[i][j] + "|");
                    } else {
                        System.out.print("|" + in[i][j] + "|");
                    }
                } else if (in[i][j] < 10 && in[i][j] >= 0) {
                    System.out.print(" " + in[i][j] + "  ");
                } else {
                    System.out.print(" " + in[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void iPrint(int[][] in) {
        for (int i = 0; i < n; i++) {
            System.out.print("   " + vert[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(vert[i]);
            for (int j = 0; j < n; j++) {
                if (in[i][j] < 10 && in[i][j] >= 0) {
                    System.out.print("  " + in[i][j] + "  ");
                } else {
                    System.out.print("  " + in[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void sPrint(String[][] in) {
        for (int i = 0; i < n; i++) {
            System.out.print("   " + vert[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(vert[i]);
            for (int j = 0; j < n; j++) {
                if (in[i][j].length() < 2) {
                    System.out.print("  " + in[i][j] + "  ");
                } else {
                    System.out.print("  " + in[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}   //end of Graph class
////////////////////////////////////////////////////////////////////////////////

class GraphAlgorithms {

    public static void main(String[] args) {
        String csvFile = "input.csv";
        BufferedReader br = null;
        String line = "";
        int n = 0;    //number of columns
        String[] vert = new String[0];
        String[][] eTemp = new String[0][0];
        try {
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            n = (line.length() - (line.length() / 2));    //number of columns
            vert = new String[n];
            eTemp = new String[n][n];
            vert = line.split(",");
            int i = 0;
            while ((line = br.readLine()) != null) {
                eTemp[i] = line.split(",");
                i++;
            }
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }   //end of try
        Graph g = new Graph(n, vert, eTemp);
        g.prim(g.edge);
        g.kruskal(g.edge);
        g.floydWarshall(g.edge);
    }
}
