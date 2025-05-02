package com.ps;
import java.io.*;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.FileReader;
//import java.io.BufferedReader;

public class managingTransaction {

    public static void writeTransaction(Transactions tx) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            bw.write(tx.toString());
            bw.newLine();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


