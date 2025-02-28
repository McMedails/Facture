package com.medails;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    /************************************************************ 
                        LECTURE DE FICHIER
    *************************************************************/

public class ReadFile
{
    /************************* Variables de classe **************************/
    // Nom du fichier
    private static final String FILENAME = "Facture.txt";

    /************************* Variables d'instance' **************************/
    // Chemin d'accès du fichier
    public String filePath;

    // Création du fichier
    public File mainFile;

    // Ligne de lecture
    public String line = null;

    /*********** Constructeur ***************/
    public ReadFile()
    {
        this.filePath = System.getProperty("user.dir") + File.separator + FILENAME;
        this.mainFile = new File(filePath);
        existFile();
    }

    // Vérification existance fichier Facture.txt
    public void existFile()
    {
        if (!mainFile.exists()) 
        try
        {
            if (mainFile.createNewFile())
            {
                System.out.println("Fichier crée : " + mainFile.getAbsolutePath());
            }
            else
            {
                System.err.println("Erreur : Impossible de créer le fichier");
            }
        }
        catch (IOException e)
        {
            System.out.println("Erreur lors de la création du fichier : " + mainFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    // Lecture par ligne : contenant un motif spécifique
    public Set<String> readLinesContaining(String pattern)
    {
        Set<String> allLines = new HashSet<>();

        existFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(mainFile)))
        {
            while ((line = reader.readLine()) != null) 
            {
                if (line.contains(pattern))
                {
                    allLines.add(line);
                } 
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        return allLines;
    }

    // Lecture par ligne : commençant par un motif spécifique
    public Set<String> readLinesStarting(String pattern)
    {
        Set<String> allLines = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(mainFile)))
        {
            while ((line = reader.readLine()) != null )
            {
                if (line.startsWith(pattern))
                {
                    allLines.add(line);
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return allLines;
    }

    // Ecriture
    public void writeLinesContaining(List<String> linesToWrite, String outputFilename)
    {
        existFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mainFile, true )))
        {
            for (String line : linesToWrite)
            {
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
}
