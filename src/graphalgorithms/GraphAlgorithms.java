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
public class GraphAlgorithms {
    public static void main(String[] args) {
        String csvFile = "input.csv";
        BufferedReader br = null;
        String line = "";
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
