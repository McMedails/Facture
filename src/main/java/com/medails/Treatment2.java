package com.medails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import com.medails.Display;
import com.medails.Graphic;
import com.medails.ReadFile;

    /************************************************************ 
                        TRAITEMENT DE DONNEES
    *************************************************************/

public class Treatment2
{
    /************************* Variables d'instance **************************/

    // Taux de taxe
    private final Double ACRE2024 = ((2.2 + 11.6 + 0.2) / 100);  // Année 2024 (ACRE)
    private final Double ACRE2025 = ((2.2 + 12.3 + 0.2) / 100);  // Année 2025 (ACRE)
    private final Double SANS2025 = ((2.2 + 24.6 + 0.2) / 100);  // Année 2025 (sans ACRE)
    private final Double SANS20XX = ((2.2 + 26.1 + 0.2) / 100);  // Année 2026 ou plus
    private final Double TVA = 1.2;

    // Données pour création graphique
    public static Double[][][][][] graphData = new Double[12][12][12][12][12];
    public static Double[][][][][] graphDeduction = new Double[5][5][5][5][5];

    /************* Déclarations Classes ****************/
    private Display display;
    private Graphic graphic;
    private ReadFile readFile;

    /*********** Constructeur ***************/
    public Treatment2(Display display, Graphic graphic, ReadFile readFile)
    {
        this.display = display;
        this.graphic = graphic;  
        this.readFile = readFile;  
                              
        /*********** Panel 2 ***************/
        actionJElements();
        clearListener2();     
        graphic.updateDatasets(graphData, graphic.GRAPHMONTHS);                               
    }

    public void actionJElements()
    {
        /*********** Panel 2 ***************/
        display.boxYearsTotal           .addActionListener (e -> graphicListener());
        display.cckTTC                  .addActionListener (e -> graphicListener());
        display.cckTVA                  .addActionListener (e -> graphicListener());
        display.cckHT                   .addActionListener (e -> graphicListener());
        display.cckTaxe                 .addActionListener (e -> graphicListener());
        display.cckBenefit              .addActionListener (e -> graphicListener());
        display.btReset2                .addActionListener (e -> clearListener2());
    }

    /*********************************************************** 
                              PANEL 2 
    ***********************************************************/
    
    public void graphicListener()
    {
        // Initialisation des données graphiques
        graphic.clearGraph();

        // Initialisation des données
        boolean     monthAcre       = false;
        boolean     activeYear      = false;
        boolean     acre2024        = false;
        boolean     acre2025        = false;
        String      currentMonth    = null;
        String      currentYear     = null;
        Double      currentTTC      = null;
        Double      currentHT       = null;
        Double      currentTVA      = null;
        Double      currentTaxe     = null;
        Double      currentBenefit  = null;
        Double      totalTTC        = 0.0;
        Double      totalHT         = 0.0;
        Double      totalTVA        = 0.0;
        Double      totalTaxe       = 0.0;
        Double      totalBenefit    = 0.0;
        Double      totalii         = 0.0;
        Double      totaljj         = 0.0;
        Double      graphHT         = 0.0;
        Double      graphTTC        = 0.0;
        Double      graphTVA        = 0.0;
        Double      graphTaxe       = 0.0;
        Double      graphBenefit    = 0.0;
        Double      ii              = 0.0;
        Double      jj              = 0.0;

        String selectedYear = (String) display.boxYearsTotal.getSelectedItem();
        if (selectedYear.isEmpty()) 
        {
            clearListener2();         
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(readFile.filePath))) 
        {
            while ((readFile.line = reader.readLine()) != null) 
            {
                // Détection de l'année sélectionnée
                if (readFile.line.contains("Versement --> ") &&  readFile.line.contains(selectedYear))
                {
                    // Enrengistrement de l'année sélectionnée
                    activeYear = true;

                    // Année 2024 (ACRE)
                    if (readFile.line.contains("2024")) 
                    {
                        acre2024 = true;
                        acre2025 = false; 
                    }

                    // Année 2025 (ACRE)
                    else if (readFile.line.contains("2025"))
                    {
                        // Vérification mois (ACRE) 
                        if ((readFile.line.contains("janvier") ||
                             readFile.line.contains("février") ||
                             readFile.line.contains("mars") ||
                             readFile.line.contains("avril"))) 
                            { 
                                monthAcre = true;
                                ii++; 
                            }

                        if ((readFile.line.contains("mai") ||
                             readFile.line.contains("juin") ||
                             readFile.line.contains("juillet") ||
                             readFile.line.contains("août") || 
                             readFile.line.contains("septembre") || 
                             readFile.line.contains("octobre") || 
                             readFile.line.contains("novembre") || 
                             readFile.line.contains("décembre"))) 
                            {
                                monthAcre = false;
                                jj++; 
                            }
                            
                        acre2024 = false;
                        acre2025 = true;
                    }

                    // Année sans ACRE
                    else
                    {
                        acre2024 = false;
                        acre2025 = false;
                    }

                    // Recherche des mois et années de versement
                    String monthPart = readFile.line.substring(readFile.line.indexOf("Versement --> ") + 17).trim();
                    int firstSpaceIndex = monthPart.indexOf(" ");
                    if (firstSpaceIndex != -1)
                    { currentMonth = monthPart.substring(0, firstSpaceIndex).trim(); }
                      currentYear = readFile.line.substring(readFile.line.length() - 4).trim();
                }  

                // Recherche et calcules à partir du montant HT
                if (activeYear && readFile.line.contains("HT --> ")) 
                {
                    // Calcule Facture (HT + TTC) 
                    String convTotalHT = readFile.line.substring(readFile.line.indexOf("HT --> ") + 7).trim();
                    totalHT += Double.parseDouble(convTotalHT); 
                    graphHT  = Double.parseDouble(convTotalHT);
                    graphTTC = Double.parseDouble(convTotalHT) * TVA;
                    graphTVA = graphTTC - graphHT;
         
                    // Année 2024 (ACRE)
                    if (acre2024) 
                    {
                        totalTaxe = totalHT * ACRE2024;
                        graphTaxe = graphHT * ACRE2024;
                        graphBenefit = graphHT - graphTaxe;
                    }

                    // Année 2025 (ACRE avec et sans)
                    if (acre2025) 
                    {
                        totalii = (ii * (totalHT * ACRE2025));
                        totaljj = (jj * (totalHT * SANS2025));
                        totalTaxe = (totalii + totaljj) / (ii + jj);
                        if(monthAcre)
                        {
                            graphTaxe = graphHT * ACRE2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                        else
                        {
                            graphTaxe = graphHT * SANS2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                    }

                    // Année 2025 (sans ACRE) ou > 2025
                    if ((!acre2024 && !acre2025)) 
                    {
                        totalTaxe = totalHT * SANS20XX;
                        graphTaxe = graphHT * SANS20XX;
                        graphBenefit = graphHT - graphTaxe;
                    }

                    // Résultat des calcules
                    totalTTC = totalHT * TVA;
                    totalTVA = totalTTC - totalHT;
                    totalBenefit = totalHT - totalTaxe;
                      
                    /* C1 */ String resultatTTC = Double.toString(totalTTC);
                    /* C1 */ display.txtTotalTTC.setText(resultatTTC);
                    /* C2 */ String resultatHT = (String.format("%.1f", totalHT)); 
                    /* C2 */ display.txtTotalHT.setText(resultatHT);
                    /* C3 */ String resultatTVA = (String.format("%.1f", totalTVA));
                    /* C3 */ display.txtTotalTVA.setText(resultatTVA);
                    /* D1 */ String resultatTaxe = String.format("%.1f", totalTaxe);
                    /* D1 */ display.txtTotalTaxe.setText(resultatTaxe);
                    /* D2 */ String resultatBenefit = String.format("%.1f", totalBenefit);
                    /* D2 */ display.txtTotalBenefit.setText(resultatBenefit);

                    /************************* GRAPHIQUE **************************/ 

                    /* B1 */ currentTTC = graphTTC;
                    /* B2 */ currentHT = Double.parseDouble(convTotalHT.replace(",", "."));
                    /* B3 */ currentTVA = graphTVA;
                    /* C1 */ currentTaxe = graphTaxe;
                    /* C2 */ currentBenefit = graphBenefit;   

                    // Réinitialiser graphData pour éviter les valeurs résiduelles
                    for (int kk = 0; kk < graphic.GRAPHMONTHS.length; kk++) 
                    {
                        // 5 correspond au nombre de catégories (TTC, TVA, HT, etc.)
                        for (int ll = 0; ll < 5; ll++) 
                        { 
                            graphData[kk][kk][kk][kk][ll] = null;
                        }
                    }

                    // Trouve le mois correspondant et stocke la valeur dans le tableau
                    Object[][] filters = 
                    {
                        { display.cckTTC,      0,  currentTTC},
                        { display.cckTVA,      1,  currentTVA},
                        { display.cckHT,       2,  currentHT},
                        { display.cckTaxe,     3,  currentTaxe},
                        { display.cckBenefit,  4,  currentBenefit},
                    };

                    for(Object[] filter : filters)
                    {
                        JCheckBox checkBox = (JCheckBox) filter[0];
                        int index = (int) filter[1];
                        Double value = (Double) filter[2];

                        // Vérifie si la checkbox est cochée
                        if (filterGraph(checkBox))
                        {
                            for (int kk = 0; kk < graphic.GRAPHMONTHS.length; kk++)
                            {
                                graphData[kk][kk][kk][kk][index] = graphic.GRAPHMONTHS[kk].equals(currentMonth) ? value : null;
                            }
                        }
                    }

                    // Renvoie des données calculées vers le graphique
                    graphic.updateDatasets(graphData, graphic.GRAPHMONTHS);
                    
                    // Réinitialise pour le prochain mois
                    activeYear = false;
                    currentMonth = null;
                    currentTTC = null;
                    currentHT = null;
                    currentTVA = null;
                    currentTaxe = null;
                    currentBenefit = null;
                }  
            }                                    
        }
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null, "Erreur lors du calcul : " + ex.getMessage(),
                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    // CheckBox Graphique
    public boolean filterGraph(JCheckBox checkBox)
    {
        return checkBox.isSelected();
    }

    
    // B3 -> RAZ
    public void clearListener2()
    {
        /* A1 */ display.boxYearsTotal.setSelectedItem("");
        /* B1 */ display.txtTotalTTC.setText("");
        /* B2 */ display.txtTotalHT.setText("");  
        /* B3 */ display.txtTVA.setText("");
        /* C1 */ display.txtTotalTaxe.setText("");
        /* C2 */ display.txtTotalBenefit.setText("");    
        graphic.clearGraph();      
    }
}