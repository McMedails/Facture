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

public class ReadWrite
{

    /************************************************************ 
                            CONSTRUCTEUR
    *************************************************************/

    public ReadWrite(String filePath)
    {
        this.file = new File(System.getProperty("user.dir") + File.separator + filePath);
        existFile();
    }    

    /************************************************************ 
                            VARIABLES
    *************************************************************/

    /************************* Variables de classe **************************/
    // Nom du fichier
    public static final String MAINFILENAME = "00_Facture.txt";
    public static final String OTHERFILENAME = "01_DeductionTVA.txt";

    /************************* Variables d'instance' **************************/
    // Création du fichier
    public File file;

    // Ligne de lecture
    public String line = null;

    /************************************************************ 
                              METHODES
    *************************************************************/

    // Permet de remplacer le filePath
    public File getFile()
    {
        return this.file;
    }


    // Vérification existance fichier 
    public void existFile()
    {
        if (!file.exists()) 
        try
        {
            if (file.createNewFile())
            {
                System.out.println("Fichier crée : " + file.getAbsolutePath());
            }
            else
            {
                System.err.println("Erreur : Impossible de créer le fichier");
            }
        }
        catch (IOException e)
        {
            System.out.println("Erreur lors de la création du fichier : " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }


    // Lecture par ligne : contenant un motif spécifique
    public Set<String> readLinesContaining(String pattern)
    {
        Set<String> allLines = new HashSet<>();
        existFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
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
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
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
    public void writeLinesContaining(List<String> linesToWrite)
    {
        existFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true )))
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
