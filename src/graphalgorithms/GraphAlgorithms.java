package graphalgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * Authors: Bailey Kouwenhoven and J. Beckett Sweeney
 * Date: 8 April 2018
 * Overview: This program reads in a .csv file containing an adjacency matrix
 *      for a graph, then runs several algorithms on that graph to find 
 *      different information about the graph.
 * Special Instructions: 
 *      - In the input file, represent a lack of edge as either an infinity
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
        int[][] g = iCopy(in);
        pQueue q = new pQueue(n);
        int[] taken = new int[n];   //vertices already included
        Node[] tree = new Node[n - 1];
        int iTaken = 0;     //current empty taken slot
        int iTree = 0;      //current empty tree slot
        int start = (int) (Math.random() * n);

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

    public void altKruskal(int[][] in) {
        int[][] g = iCopy(in);          //matrix
        pQueue q = new pQueue(n);       //priority queue
        int[] cu = new int[n];      //cluster u
        int icu = 0;                    //cluster u iterator
        int[] cv = new int[n];      //cluster v
        int icv = 0;                    //cluster v iterator
        Node[] tree = new Node[n - 1];
        int iTree = 0;
        Node temp;
        for (int i = 0; i < n; i++) {
            cu[i] = -2;
            cv[i] = -2;
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
            cu[icu] = temp.vs;
            icu++;
            cu = clusterSort(cu, icu);
            cv[icv] = temp.ve;
            icv++;
            cv = clusterSort(cv, icv);
            boolean same = true;
            for (int i = 0; i < n; i++) {
                if (cu[i] != cv[i]) {
                    same = false;
                }
            }
            if (same == false) {
                tree[iTree] = temp;
                iTree++;
                for (int i = 0; i < icv; i++) {
                    cu[icu] = cv[i];
                    icu++;
                }
            }
        }
    }

    public int[] clusterSort(int[] in, int max) {
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < max; j++) {
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
        return in;
    }
    
    public void kruskal(int[][] in) {
        int[][] g = iCopy(in);
        pQueue q = new pQueue(n);
        int[] taken = new int[n];   //vertices already included
        Node[] tree = new Node[n - 1];
        int iTaken = 0;     //current empty taken slot
        int iTree = 0;      //current empty tree slot
        int addS = 0;
        int addE = 0;
        int temp;
        for (int i = 0; i < n; i++) {
            taken[i] = -2;
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
        printN(tree, "tree");
        printI(taken, "taken");

        tree[iTree] = q.pop();
        taken[iTaken] = tree[iTree].vs;
        iTaken++;
        taken[iTaken] = tree[iTree].ve;
        iTaken++;

        printN(tree, "tree");
        printI(taken, "taken");
        System.out.println();

        iTree++;
        q.clear();
        while (tree[tree.length - 1] == null) { //while the tree is not full
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        if (g[i][j] > 0) {
                            q.push(new Node(g[i][j], i, j));
                        }
                    }
                }
            }

            int clear = 0;
            while (clear < iTree) { //checks for vertices already used
                clear = 0;
                System.out.println("peek: " + q.peek());

                if (q.peek() != null) {
                    for (int i = 0; i < iTree; i++) {
                        System.out.println("tree: " + tree[i]);
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
                } else {
                    clear = iTree;
                }

            }   //end of lesser while loop
            if (q.peek() != null) {
                tree[iTree] = q.pop();
            }
            addS = 0;
            addE = 0;
            for (int i = 0; i < iTaken; i++) {
                if (taken[i] != tree[iTree].vs) {
                    addS++;
                }
                if (taken[i] != tree[iTree].ve) {
                    addE++;
                }
            }
            temp = iTaken;
            if (addS == temp) {
                taken[iTaken] = tree[iTree].vs;
                iTaken++;
                addS = 0;
            }
            if (addE == temp) {
                taken[iTaken] = tree[iTree].ve;
                iTaken++;
                addE = 0;
            }

            printN(tree, "tree");
            printI(taken, "taken");
            System.out.println();

            iTree++;
            q.clear();
        }   //end of greater while loop
        System.out.println("Kruskal's Tree: ");
        for (int i = 0; i < tree.length; i++) {
            System.out.print(vert[tree[i].vs] + vert[tree[i].ve] + " ");
        }
        System.out.println();
        System.out.println();
    }   //end of kruskal

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
        //System.out.print(" ");
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
        //System.out.print(" ");
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
        /*
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
            System.out.println(line);
            vert = line.split(",");
            int i = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
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
         */

        int n = 7;    //number of columns
        String[] vert = new String[7];
        String[][] eTemp = new String[7][7];
        vert[0] = "A";
        vert[1] = "B";
        vert[2] = "C";
        vert[3] = "D";
        vert[4] = "E";
        vert[5] = "F";
        vert[6] = "G";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (eTemp[i][j] == null) {
                    String in;
                    int num = (int) (Math.random() * 100);
                    if (num < 10) {
                        in = "?";
                    } else {
                        in = Integer.toString(num);
                    }
                    eTemp[i][j] = in;
                    eTemp[j][i] = in;
                }
            }
        }

        Graph g = new Graph(n, vert, eTemp);
        g.sPrint(g.eString);
        g.iPrint(g.edge);
        g.prim(g.edge);
        g.kruskal(g.edge);
        //g.floydWarshall(g.edge);
    }
}
