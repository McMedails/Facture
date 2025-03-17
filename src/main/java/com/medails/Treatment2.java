package com.medails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import org.jfree.chart.JFreeChart;

    /************************************************************ 
                        TRAITEMENT DE DONNEES
    *************************************************************/

public class Treatment2
{
    /************************* Variables de classe **************************/

    // Données pour création graphique
    public static Double[][][][][] graphDecenal = new Double[5][5][5][5][5];
    public static Double[][][][][] graphYearMonth = new Double[12][12][12][12][12];

    /************************* Variables d'instance **************************/
    
    /************* Déclarations Classes ****************/
    private Display dp;
    private Graphic gr;
    private ReadWrite rwMain;
    private Treatment1 tr1;

    /*********** Constructeur ***************/
    public Treatment2(Display dp, Graphic gr, ReadWrite rwMain, Treatment1 tr1)
    {
        this.dp = dp;
        this.gr = gr;  
        this.rwMain = rwMain;    
        this.tr1 = tr1;
                              
        /*********** Appels Méthodes ***************/
        actionJElements(); 
        graphDecenal();
        graphYearMonth();
        gr.updateDatasets(graphYearMonth, gr.GRAPHMONTHS, gr.CATEGORIES, gr.dataYearsPan2, gr.dataMonthsPan2);                               
    }

    private void actionJElements()
    {
        /*********** Partie Graphique ***************/
        dp.sliDecadePan2           .addChangeListener (e -> graphDecenal());
        dp.sliYearMonthPan2        .addChangeListener (e -> graphYearMonth());
        dp.boxYearsTotal           .addActionListener (e -> graphYearMonth());
        dp.cckTTCPan2              .addActionListener (e -> graphYearMonth());
        dp.cckTVAPan2              .addActionListener (e -> graphYearMonth());
        dp.cckHTPan2               .addActionListener (e -> graphYearMonth());
        dp.cckTaxePan2             .addActionListener (e -> graphYearMonth());
        dp.cckBenefitPan2          .addActionListener (e -> graphYearMonth());
        dp.butReset2               .addActionListener (e -> clearListener());
    }

    /*********************************************************** 
                           Onglet Décénie
    ***********************************************************/
    
    public void graphDecenal()
    {
        // Mise à jour des graphiques avec la nouvelle échelle
        slideRange(gr.chartDecadePan2, dp.sliDecadePan2);     

        // Vérification existance du fichier
        rwMain.existFile();

        // Initialisation des données graphiques
        gr.dataDecadePan2.clear();

        // Initialisation des données
        boolean     monthAcre       = false;
        boolean     acre2024        = false;
        boolean     acre2025        = false;
        String      currentYear     = null;
        String      lastYear        = null;
        double      graphHT         = 0.0;
        double      graphTTC        = 0.0;
        double      graphTVA        = 0.0;
        double      graphTaxe       = 0.0;
        double      graphBenefit    = 0.0;
        double      ii              = 0.0;
        double      jj              = 0.0;
        int         refreshYear     = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(rwMain.getFile()))) 
        {
            while ((rwMain.line = reader.readLine()) != null) 
            {
                // Détection de l'année sélectionnée
                if (rwMain.line.contains("Versement --> "))
                {
                    // Recherche des années de versement
                    currentYear = rwMain.line.substring(rwMain.line.length() - 4).trim();

                    // Année 2024 (ACRE)
                    if (rwMain.line.contains("2024")) 
                    { acre2024 = true; acre2025 = false; }

                    // Année 2025 (ACRE)
                    else if (rwMain.line.contains("2025")) 
                    {
                        // Vérification mois (ACRE) 
                        if ((rwMain.line.contains("janvier") || rwMain.line.contains("février") ||
                             rwMain.line.contains("mars")    || rwMain.line.contains("avril"))) 
                            { monthAcre = true; ii++; }

                        if ((rwMain.line.contains("mai")       || rwMain.line.contains("juin") ||
                             rwMain.line.contains("juillet")   || rwMain.line.contains("août") || 
                             rwMain.line.contains("septembre") || rwMain.line.contains("octobre") || 
                             rwMain.line.contains("novembre")  || rwMain.line.contains("décembre"))) 
                            { monthAcre = false; jj++; }
               
                        acre2024 = false; acre2025 = true;
                    }

                    // Année sans ACRE
                    else { acre2024 = false; acre2025 = false; }
                }  
    
                // Recherche et calcules à partir du montant HT
                if (rwMain.line.contains("HT --> ")) 
                {
                    // Vérification changement d'année
                    if (refreshYear == 0)
                    { lastYear = currentYear; refreshYear++; }

                    // Réinitialise si changement d'année
                    if (!lastYear.equals(currentYear))
                    { refreshYear = 0; graphTTC = 0.0; graphHT = 0.0; }

                    // Calcule Facture (HT + TTC) 
                    String convTotalHT = rwMain.line.substring(rwMain.line.indexOf("HT --> ") + 7).trim();
                    graphTTC += Double.parseDouble(convTotalHT) * tr1.TVA;
                    graphHT += Double.parseDouble(convTotalHT);
                    graphTVA = graphTTC - graphHT;

                    // Année 2024 (ACRE)
                    if (acre2024) 
                    {
                        graphTaxe = graphHT * tr1.ACRE2024;
                        graphBenefit = graphHT - graphTaxe;
                    }

                    // Année 2025 (ACRE avec et sans)
                    if (acre2025) 
                    {
                        if(monthAcre)
                        {
                            graphTaxe = graphHT * tr1.ACRE2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                        else
                        {
                            graphTaxe = graphHT * tr1.SANS2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                    }

                    // Année 2025 (sans ACRE) ou > 2025
                    if ((!acre2024 && !acre2025)) 
                    {
                        graphTaxe = graphHT * tr1.SANS20XX;
                        graphBenefit = graphHT - graphTaxe;
                    }
                      
                    /************************* GRAPHIQUE **************************/                     
                    // Trouve le mois correspondant et stocke la valeur dans le tableau
                    for (int kk = 0; kk < gr.GRAPHYEARS.length; kk++)
                    {
                        if (gr.GRAPHYEARS[kk].equals(currentYear)) 
                        {
                            graphDecenal[kk][kk][kk][kk][0] = graphTTC;      
                            graphDecenal[kk][kk][kk][kk][1] = graphTVA;     
                            graphDecenal[kk][kk][kk][kk][2] = graphHT; 
                            graphDecenal[kk][kk][kk][kk][3] = graphTaxe;   
                            graphDecenal[kk][kk][kk][kk][4] = graphBenefit;  
                        }
                    }
                        
                    // Renvoie des données calculées vers le graphique
                    gr.updateDatasets(graphDecenal, gr.GRAPHYEARS, gr.CATEGORIES, gr.dataDecadePan2); 

                    // Réinitialise pour le prochain mois
                    currentYear    = null;
                    graphTVA       = 0.0;
                    graphTaxe      = 0.0;
                    graphBenefit   = 0.0;
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

    /*********************************************************** 
                    Onglet Annuel / Mensuel
    ***********************************************************/
    
    public void graphYearMonth()
    {
        // Mise à jour des graphiques avec la nouvelle échelle
        slideRange(gr.chartYearsPan2, dp.sliYearMonthPan2);
        slideRange(gr.chartMonthsPan2, dp.sliYearMonthPan2);

        // Vérification existance du fichier
        rwMain.existFile();

        // Initialisation des données graphiques
        gr.dataYearsPan2.clear();
        gr.dataMonthsPan2.clear();

        // Initialisation des données
        boolean     monthAcre       = false;
        boolean     activeYear      = false;
        boolean     acre2024        = false;
        boolean     acre2025        = false;
        String      currentMonth    = null;
        double      totalTTC        = 0.0;
        double      totalHT         = 0.0;
        double      totalTVA        = 0.0;
        double      totalTaxe       = 0.0;
        double      totalBenefit    = 0.0;
        double      totalii         = 0.0;
        double      totaljj         = 0.0;
        double      graphTTC        = 0.0;
        double      graphTVA        = 0.0;
        double      graphHT         = 0.0;
        double      graphTaxe       = 0.0;
        double      graphBenefit    = 0.0;
        double      ii              = 0.0;
        double      jj              = 0.0;
        int         cleartxt        = 0;

        String selectedYear = (String) dp.boxYearsTotal.getSelectedItem();
        if (selectedYear.isEmpty()) { clearListener(); return;}

        try (BufferedReader reader = new BufferedReader(new FileReader(rwMain.getFile()))) 
        {
            while ((rwMain.line = reader.readLine()) != null) 
            {
                // Détection de l'année sélectionnée
                if (rwMain.line.contains("Versement --> ") &&  rwMain.line.contains(selectedYear))
                {
                    // Enrengistrement de l'année sélectionnée
                    activeYear = true;

                    // Recherche des mois de versement
                    String monthPart = rwMain.line.substring(rwMain.line.indexOf("Versement --> ") + 17).trim();
                    int firstSpaceIndex = monthPart.indexOf(" ");
                    if (firstSpaceIndex != -1)
                    { currentMonth = monthPart.substring(0, firstSpaceIndex).trim(); }

                    // Année 2024 (ACRE)
                    if (rwMain.line.contains("2024")) 
                    { acre2024 = true; acre2025 = false; }

                    // Année 2025 (ACRE)
                    else if (rwMain.line.contains("2025")) 
                    {
                        // Vérification mois (ACRE) 
                        if ((rwMain.line.contains("janvier") || rwMain.line.contains("février") ||
                             rwMain.line.contains("mars")    || rwMain.line.contains("avril"))) 
                            { monthAcre = true; ii++; }

                        if ((rwMain.line.contains("mai")       || rwMain.line.contains("juin") ||
                             rwMain.line.contains("juillet")   || rwMain.line.contains("août") || 
                             rwMain.line.contains("septembre") || rwMain.line.contains("octobre") || 
                             rwMain.line.contains("novembre")  || rwMain.line.contains("décembre"))) 
                            { monthAcre = false; jj++; }
               
                        acre2024 = false; acre2025 = true;
                    }

                    // Année sans ACRE
                    else { acre2024 = false; acre2025 = false; }
                }  
            
                // Recherche et calcules à partir du montant HT
                if (activeYear && rwMain.line.contains("HT --> ")) 
                {
                    // Calcule Facture (HT + TTC) 
                    String convTotalHT = rwMain.line.substring(rwMain.line.indexOf("HT --> ") + 7).trim();
                    totalHT += Double.parseDouble(convTotalHT); 
                    graphTTC = Double.parseDouble(convTotalHT) * tr1.TVA;
                    graphHT  = Double.parseDouble(convTotalHT);
                    graphTVA = graphTTC - graphHT;
         
                    // Année 2024 (ACRE)
                    if (acre2024) 
                    {
                        totalTaxe = totalHT * tr1.ACRE2024;
                        graphTaxe = graphHT * tr1.ACRE2024;
                        graphBenefit = graphHT - graphTaxe;
                    }

                    // Année 2025 (ACRE avec et sans)
                    if (acre2025) 
                    {
                        totalii = (ii * (totalHT * tr1.ACRE2025));
                        totaljj = (jj * (totalHT * tr1.SANS2025));
                        totalTaxe = (totalii + totaljj) / (ii + jj);
                        if(monthAcre)
                        {
                            graphTaxe = graphHT * tr1.ACRE2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                        else
                        {
                            graphTaxe = graphHT * tr1.SANS2025;
                            graphBenefit = graphHT - graphTaxe;
                        }
                    }

                    // Année 2025 (sans ACRE) ou > 2025
                    if ((!acre2024 && !acre2025)) 
                    {
                        totalTaxe = totalHT * tr1.SANS20XX;
                        graphTaxe = graphHT * tr1.SANS20XX;
                        graphBenefit = graphHT - graphTaxe;
                    }

                    // Résultat des calcules
                    totalTTC = totalHT * tr1.TVA;
                    totalTVA = totalTTC - totalHT;
                    totalBenefit = totalHT - totalTaxe;
                      
                    /************************* TEXTFIELD **************************/ 
                    /* C1 */ String resultatTTC = Double.toString(totalTTC);
                    /* C1 */ dp.txtTotalTTC.setText(resultatTTC);
                    /* C2 */ String resultatHT = (String.format("%.1f", totalHT)); 
                    /* C2 */ dp.txtTotalHT.setText(resultatHT);
                    /* C3 */ String resultatTVA = (String.format("%.1f", totalTVA));
                    /* C3 */ dp.txtTotalTVA.setText(resultatTVA);
                    /* D1 */ String resultatTaxe = String.format("%.1f", totalTaxe);
                    /* D1 */ dp.txtTotalTaxe.setText(resultatTaxe);
                    /* D2 */ String resultatBenefit = String.format("%.1f", totalBenefit);
                    /* D2 */ dp.txtTotalBenefit.setText(resultatBenefit);

                    /************************* GRAPHIQUE **************************/ 
                    // Réinitialise graphYearMonth pour éviter les valeurs résiduelles
                    for (int kk = 0; kk < gr.GRAPHMONTHS.length; kk++) 
                    {
                        for (int ll = 0; ll < gr.CATEGORIES.length; ll++) 
                        { 
                            graphYearMonth[kk][kk][kk][kk][ll] = null;
                        }
                    }

                    // Trouve le mois correspondant et stocke la valeur dans le tableau
                    Object[][] filters = 
                    {
                        { dp.cckTTCPan2,        0,  graphTTC},
                        { dp.cckTVAPan2,        1,  graphTVA},
                        { dp.cckHTPan2,         2,  graphHT},
                        { dp.cckTaxePan2,       3,  graphTaxe},
                        { dp.cckBenefitPan2,    4,  graphBenefit},
                    };

                    for(Object[] filter : filters)
                    {
                        JCheckBox checkBox = (JCheckBox) filter[0];
                        int index = (int) filter[1];
                        Double value = (Double) filter[2];

                        // Vérifie si la checkbox est cochée
                        if (filterGraph(checkBox))
                        {
                            for (int kk = 0; kk < gr.GRAPHMONTHS.length; kk++)
                            {
                               // Equivalent : graphYearMonth[kk][kk][kk][kk][index] = gr.GRAPHMONTHS[kk].equals(currentMonth) ? value : null;
                               if (gr.GRAPHMONTHS[kk].equals(currentMonth)) 
                               {
                                    graphYearMonth[kk][kk][kk][kk][index] = value;
                                    cleartxt++;
                               } 
                               else 
                               {
                                    graphYearMonth[kk][kk][kk][kk][index] = null;
                               }
                            }
                        }
                    }

                    // Renvoie des données calculées vers le graphique
                    gr.updateDatasets(graphYearMonth, gr.GRAPHMONTHS, gr.CATEGORIES, gr.dataYearsPan2, gr.dataMonthsPan2);
                    
                    // Réinitialise pour le prochain mois
                    activeYear     = false;
                    currentMonth   = null;
                    graphTTC       = 0.0;
                    graphTVA       = 0.0;
                    graphHT        = 0.0;
                    graphTaxe      = 0.0;
                    graphBenefit   = 0.0;
                }  
            } 

            // Nettoie les champs de saisie si graphique vide
            if (cleartxt == 0) { clearListener(); } 

        }
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null, "Erreur lors du calcul : " + ex.getMessage(),
                                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /*********************************************************** 
                          Autres Méthodes
    ***********************************************************/

    // Mise à jour des graphiques avec la nouvelle échelle
    public void slideRange(JFreeChart chart, JSlider slide)
    {
        int range = slide.getValue();
        gr.updatChartRange(chart, range);
    }


    // CheckBox Graphique
    public boolean filterGraph(JCheckBox checkBox)
    { return checkBox.isSelected(); }


    // D3 -> RAZ
    public void clearListener()
    {
        /* B1 */ dp.boxYearsTotal.setSelectedItem("");
        /* C1 */ dp.txtTotalTTC.setText("");
        /* C2 */ dp.txtTotalHT.setText("");  
        /* C3 */ dp.txtTotalTVA.setText("");
        /* D1 */ dp.txtTotalTaxe.setText("");
        /* D2 */ dp.txtTotalBenefit.setText("");    
    }
}