
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        try{
            boolean outPut = false;
            boolean fileOrFolder = false;

            PDFMergerUtility PDFmerger = new PDFMergerUtility();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8));

            boolean terminate = false;

            System.out.println("Console application started");
            System.out.println("INFO: Dont use Umlaut in Path of any file");

            while (!terminate){
                System.out.print("> ");
                String line = reader.readLine();
                String[] inputs = line.split(" ", 2);
                String command = inputs[0];
                String input = "";
                if(inputs.length > 1){
                    input = inputs[1];
                }
                switch (command){
                    case "output":
                        PDFmerger.setDestinationFileName(input);
                        outPut = true;
                        System.out.println("ok");
                        break;
                    case "file":
                        File file = new File(input);
                        if(!file.isFile() || !file.getAbsolutePath().contains(".pdf")){
                            System.err.println("Error: Not a file: " + input);
                            break;
                        }
                        PDFmerger.addSource(file);
                        fileOrFolder = true;
                        System.out.println("ok");
                        break;
                    case "folder":
                        File dir = new File(input);
                        File[] directoryListing = dir.listFiles();
                        if (directoryListing != null) {
                            for (File child : directoryListing) {
                                if(child.getAbsolutePath().contains(".pdf"))
                                {
                                    PDFmerger.addSource(child);
                                    fileOrFolder = true;
                                }
                            }
                        }
                        System.out.println("ok");
                        break;
                    case "merge":
                        if(fileOrFolder && outPut){
                            PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
                            File f = new File(PDFmerger.getDestinationFileName());
                            System.out.println("Merge completed, output file: " + f.getAbsolutePath());
                            PDFmerger = new PDFMergerUtility();
                            outPut = false;
                            fileOrFolder = false;
                        }else {
                            if(!fileOrFolder) System.out.println("Error: No files or folder selected");
                            if(!outPut) System.out.println("Error: The output file must be defined");
                        }
                        break;
                    case "help":
                        System.out.println("output C:\\Path\\To\\output.pdf \t //where the merge will be saved (Filename must be included)");
                        System.out.println("file C:\\Path\\To\\File \t //adds a file to the output");
                        System.out.println("folder C:\\Path\\To\\Folder \t //all pdfs in this folder will be added to output");
                        System.out.println("restart \t //to restart process");
                        System.out.println("merge \t //to generate the pdf");
                        System.out.println("exit \t //exit the program");
                        break;
                    case "restart":
                        PDFmerger = new PDFMergerUtility();
                        outPut = false;
                        fileOrFolder = false;
                        System.out.println("ok");
                        break;
                    case "exit":
                        terminate = true;
                        break;
                    default:
                        System.out.println("Not a command...");
                        System.out.println("Type in help for more information");
                }
            }
        }catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Restart application...");
            main(args);
        }
    }
}
