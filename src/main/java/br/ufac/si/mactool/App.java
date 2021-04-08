/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufac.si.mactool;

import br.ufac.si.mactool.util.ReadCSVDataset;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HelenoCampos
 */
public class App {
    public static void main(String[] args){
        if (args.length == 3) {
            String datasetPath = args[0];
            String reposPath = args[1];
            String outputPath = args[2];

            ReadCSVDataset dataset = new ReadCSVDataset(datasetPath);
            float currentIndex = 0;
            for (String project : dataset.getMergeList().keySet()) {
                float percentage = (currentIndex++ / (float) dataset.getMergeList().size()) * 100;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy/ HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();  
                System.out.println(dtf.format(now) + "### " + String.format("%.2f", percentage) + "% of projects processed. Processing repo "+project);
                try {
                    String repositoryPath = reposPath + "/" + project;
                    File repositoryFolder = new File(repositoryPath);
                    if (repositoryFolder.exists()) {
                        if (!isAlreadyCollected(outputPath, project)) {
                            MACTool tool = new MACTool(dataset.getMergeList().get(project), outputPath, repositoryPath);
                            tool.runAnalysis();
                            tool = null;
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("\tInvalid repository!");
                }
            }
            System.out.println("Finished\n");
        } else {
            System.out.println("Wrong arguments... first argument: datasetcsv \t second argument: repositories_path \t third argument: output_path \t");
        }
    }
    
    
    public static boolean isAlreadyCollected(String outputPath, String project) {
        String projectName = project.split("/")[1];
        String outputFilePath = outputPath + "/" + projectName + "-general.csv";
        File outputFile = new File(outputFilePath);
        return outputFile.exists();
    }
}
