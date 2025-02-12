package com.medails;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Reader
{

    // Chemin d'accès du fichier
    public static String filePath = System.getProperty("user.dir") + File.separator + ("Facture.txt");

    public static File mainFile = new File(filePath);

    // Méthode factorisée pour lire un fichier et retourner ses lignes en liste
    public static List<String> read (String filePath)
    {
        List<String> listLine = new ArrayList<>();
    
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null)
            {
                listLine.add(line);
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Lecture impossible : " + filePath);
            return Collections.emptyList();
        }
        return listLine;
    }
}