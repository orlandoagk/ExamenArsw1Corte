package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private ProcessThreadMoney[] arregloDeHilos;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public ProcessThreadMoney[] getHilos(){
        return arregloDeHilos;
    }

    public void aumentarArchivosProcesados(){
        amountOfFilesProcessed.incrementAndGet();
    }

    public void processTransactionData() {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();

        int numHilos = 5;

        amountOfFilesTotal = transactionFiles.size();

        arregloDeHilos = new ProcessThreadMoney[numHilos];
        for(int i = 0;i<numHilos;i++){
            arregloDeHilos[i] = new ProcessThreadMoney(transactionReader,transactionAnalyzer,this);
        }
        int round = 0;

        for (File f: transactionFiles){
            arregloDeHilos[round].addFile(f);
            if(round == numHilos-1){
                round = 0;
            } else {
                round++;
            }
        }
        for (ProcessThreadMoney hilo: arregloDeHilos){
            hilo.start();
        }

        for (ProcessThreadMoney hilo: arregloDeHilos){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        boolean enterPresionado = true;
        while(true){
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit")) {
                break;
            } else if (line.equals("") && enterPresionado){
                for(ProcessThreadMoney hilo: moneyLaundering.getHilos()){
                    hilo.pausarHilo();
                }
                moneyLaundering.imprimirMensaje();
                enterPresionado = false;
            } else if (line.equals("")){
                enterPresionado = true;
                for(ProcessThreadMoney hilo: moneyLaundering.getHilos()){
                    hilo.reanudarHilo();
                }
            } else if (enterPresionado){
                moneyLaundering.imprimirMensaje();
            } else {
                System.out.println("El programa esta pausado hasta que vuelvas a undir ENTER");
            }

        }
    }
    public void imprimirMensaje(){
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, amountOfFilesProcessed.get(), amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }


}
