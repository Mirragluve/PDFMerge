import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.*;
import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

public class PdfMerge {
    private int errorCount;
    private boolean outPut;
    private boolean fileOrFolder;
    private boolean terminate;
    private PDFMergerUtility PDFmerger;

    public PdfMerge(){
        outPut = false;
        fileOrFolder = false;
        terminate = false;
        PDFmerger = new PDFMergerUtility();
        errorCount = 0;
    }

    public void run(){
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        terminate = false;

        System.out.println("Console application started");
        System.out.println("INFO: Dont use Umlaut in Path of any file");

        while(!terminate && errorCount < 3){
            System.out.print("> ");
            String line = null;
            try {
                line = reader.readLine();
                String[] inputs = line.split(" ", 2);
                if(inputs.length == 1){
                    handleCommands(inputs[0]);
                }else{
                    handleCommands(inputs[0], inputs[1]);
                }
            } catch (IOException e) {
                printErrorMessage("Could not read line: " + e.getMessage());
                errorCount++;
            }
        }

        if(errorCount == 3){
            printErrorMessage("Could not read any Line, shutdown initiated");
            System.out.println("5");
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("4");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("3");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("2");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCommands(String command){
        switch (command){
            case "merge":
                mergePdfs();
                break;
            case "help":
                printHelp();
                break;
            case "restart":
                restart();
                System.out.println("ok");
                break;
            case "exit":
                terminate = true;
                break;
            default:
                defaultOutput();
                break;
        }
    }

    private void handleCommands(String command, String input){
        switch (command) {
            case "output":
                PDFmerger.setDestinationFileName(input);
                outPut = true;
                System.out.println("ok");
                break;
            case "file":
                if(addFile(input))
                System.out.println("ok");
                break;
            case "folder":
                addFolder(input);
                break;
            default:
                defaultOutput();
                break;

        }
    }

    private void defaultOutput(){
        System.out.println("Not a command...");
        System.out.println("Type in help for more information");
    }

    private void restart(){
        PDFmerger = new PDFMergerUtility();
        outPut = false;
        fileOrFolder = false;
    }

    private boolean addFolder(String folderPath){
        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        int pdfCount = 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getAbsolutePath().contains(".pdf")) {
                    try {
                        PDFmerger.addSource(child);
                        pdfCount++;
                    } catch (FileNotFoundException e) {
                        printErrorMessage("Could not add file \"" + child.getAbsolutePath() + "\"" + "\nReason: " + e.getMessage());
                    }
                }
            }
        }
        if(pdfCount > 0 ){
            fileOrFolder = true;
            System.out.println(pdfCount + " PDF files added");
            return true;
        }
        System.out.println("Folderpath wrong: " + folderPath);
        return false;
    }

    private boolean addFile(String filePath){
        File file = new File(filePath);
        if (!file.isFile() || !file.getAbsolutePath().contains(".pdf")) {
            printErrorMessage("Not a file: " + filePath);
            return false;
        }
        try{
            PDFmerger.addSource(file);
        }catch (Exception e){
            printErrorMessage(e.getMessage());
            return false;
        }

        fileOrFolder = true;
        return true;
    }


    private void printErrorMessage(String message){
        System.err.println("Error: " + message);
    }

    private void printHelp(){
        System.out.println("output C:\\Path\\To\\output.pdf \t //where the merge will be saved (Filename must be included)");
        System.out.println("file C:\\Path\\To\\File \t //adds a file to the output");
        System.out.println("folder C:\\Path\\To\\Folder \t //all pdfs in this folder will be added to output");
        System.out.println("restart \t //to restart process");
        System.out.println("merge \t //to generate the pdf");
        System.out.println("exit \t //exit the program");
    }

    private boolean mergePdfs()  {
        if(fileOrFolder && outPut){
            try {
                PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            } catch (IOException e) {
                printErrorMessage(e.getMessage());
                return false;
            }
            File f = new File(PDFmerger.getDestinationFileName());
            System.out.println("Merge completed, output file: " + f.getAbsolutePath());
            PDFmerger = new PDFMergerUtility();
            outPut = false;
            fileOrFolder = false;
            return true;
        }else {
            if(!fileOrFolder) printErrorMessage("No files or folder selected");
            if(!outPut) printErrorMessage("The output file must be defined");
            return false;
        }
    }
}
