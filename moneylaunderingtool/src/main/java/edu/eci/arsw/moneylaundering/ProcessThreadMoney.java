package edu.eci.arsw.moneylaundering;

import sun.awt.Mutex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessThreadMoney extends Thread {
    ArrayList<File> archivos;
    TransactionReader transactionReader;
    TransactionAnalyzer transactionAnalyzer;
    MoneyLaundering main;
    boolean estaPausado;
    Mutex m;
    public ProcessThreadMoney(ArrayList<File> archivos, TransactionReader lector, TransactionAnalyzer analizador, MoneyLaundering main){
        this.archivos = archivos;
        this.transactionReader = lector;
        transactionAnalyzer = analizador;
        this.main = main;
        estaPausado = false;
        m = new Mutex();
    }

    @Override
    public void run() {
        for(File transactionFile : archivos){
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions){
                while(estaPausado){
                    synchronized (m){
                        try {
                            m.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                transactionAnalyzer.addTransaction(transaction);
            }
            main.aumentarArchivosProcesados();
        }
    }

    public void pausarHilo(){
        estaPausado = true;
    }

    public void reanudarHilo(){
        estaPausado = false;
        synchronized (m){
            m.notify();
        }
    }

}
