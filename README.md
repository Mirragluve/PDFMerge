# PDFMerge
A simple console application to merge PDFs using Apache PDFBox

## Commands
|command|functionality|
|-------|-------------|
|help|lists all possible commands|
|output C:\Path\To\Output\File.pdf|defines the merged output destination including filename and extension|
|file C:\Path\To\File.pdf|the given pdf file will be added|
|folder C:\Path\To\Folder|all pdf files in this folder will be added|
|merge|generate merged pdf|
|restart|output and file or folder will be deleted|
|exit|exits the application|

_Do not use any Umlaut, Apostroph etc. in Paths_

## Installation
run `mvn clean install`
This will create the PDFMerge.jar in the target folder

On windows open command line and run the jar file `java -jar PDFMerge.jar` or create a batch file with the same command.
