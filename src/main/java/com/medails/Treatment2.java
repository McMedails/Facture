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
    /************************* Variables d'instance **************************/

    // Données pour création graphique
    public static Double[][][][][] graphData = new Double[12][12][12][12][12];

    /************* Déclarations Classes ****************/
    private Display dp;
    private Graphic gr;
    private ReadWrite rw;
    private Treatment1 tr1;

    /*********** Constructeur ***************/
    public Treatment2(Display dp, Graphic gr, ReadWrite rw, Treatment1 tr1)
    {
        this.dp = dp;
        this.gr = gr;  
        this.rw = rw;  
        this.tr1 = tr1;
                              
        /*********** Panel 2 ***************/
        actionJElements();  
        gr.updateDatasets(graphData, gr.GRAPHMONTHS, gr.CATEGORIES, gr.dataYears, gr.dataMonths);                               
    }

    private void actionJElements()
    {
        /*********** Panel 2 ***************/
        dp.sliPan2                 .addChangeListener (e -> graphListener());
        dp.boxYearsTotal           .addActionListener (e -> graphListener());
        dp.cckTTCPan2              .addActionListener (e -> graphListener());
        dp.cckTVAPan2              .addActionListener (e -> graphListener());
        dp.cckHTPan2               .addActionListener (e -> graphListener());
        dp.cckTaxePan2             .addActionListener (e -> graphListener());
        dp.cckBenefitPan2          .addActionListener (e -> graphListener());
        dp.btReset2                .addActionListener (e -> clearListener());
    }

    /*********************************************************** 
                              PANEL 2 
    ***********************************************************/
    
    public void graphListener()
    {
        // Mise à jour des graphiques avec la nouvelle échelle
        slideRange(gr.chartYears, dp.sliPan2);
        slideRange(gr.chartMonths, dp.sliPan2);

        // Vérification existance du fichier
        rw.existFile();

        // Initialisation des données graphiques
        gr.dataYears.clear();
        gr.dataMonths.clear();

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

        try (BufferedReader reader = new BufferedReader(new FileReader(rw.getFile()))) 
        {
            while ((rw.line = reader.readLine()) != null) 
            {
                // Détection de l'année sélectionnée
                if (rw.line.contains("Versement --> ") &&  rw.line.contains(selectedYear))
                {
                    // Enrengistrement de l'année sélectionnée
                    activeYear = true;

                    // Recherche des mois de versement
                    String monthPart = rw.line.substring(rw.line.indexOf("Versement --> ") + 17).trim();
                    int firstSpaceIndex = monthPart.indexOf(" ");
                    if (firstSpaceIndex != -1)
                    { currentMonth = monthPart.substring(0, firstSpaceIndex).trim(); }

                    // Année 2024 (ACRE)
                    if (rw.line.contains("2024")) 
                    {
                        acre2024 = true;
                        acre2025 = false; 
                    }

                    // Année 2025 (ACRE)
                    else if (rw.line.contains("2025")) 
                    {
                        // Vérification mois (ACRE) 
                        if ((rw.line.contains("janvier") ||
                             rw.line.contains("février") ||
                             rw.line.contains("mars") ||
                             rw.line.contains("avril"))) 
                            { 
                                monthAcre = true;
                                ii++; 
                            }

                        if ((rw.line.contains("mai") ||
                             rw.line.contains("juin") ||
                             rw.line.contains("juillet") ||
                             rw.line.contains("août") || 
                             rw.line.contains("septembre") || 
                             rw.line.contains("octobre") || 
                             rw.line.contains("novembre") || 
                             rw.line.contains("décembre"))) 
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
                }  
            

                // Recherche et calcules à partir du montant HT
                if (activeYear && rw.line.contains("HT --> ")) 
                {
                    // Calcule Facture (HT + TTC) 
                    String convTotalHT = rw.line.substring(rw.line.indexOf("HT --> ") + 7).trim();
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
                    // Réinitialise graphData pour éviter les valeurs résiduelles
                    for (int kk = 0; kk < gr.GRAPHMONTHS.length; kk++) 
                    {
                        for (int ll = 0; ll < gr.CATEGORIES.length; ll++) 
                        { 
                            graphData[kk][kk][kk][kk][ll] = null;
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
                               // Equivalent : graphData[kk][kk][kk][kk][index] = gr.GRAPHMONTHS[kk].equals(currentMonth) ? value : null;
                               if (gr.GRAPHMONTHS[kk].equals(currentMonth)) 
                               {
                                    graphData[kk][kk][kk][kk][index] = value;
                                    cleartxt++;
                               } 
                               else 
                               {
                                    graphData[kk][kk][kk][kk][index] = null;
                               }
                            }
                        }
                    }

                    // Renvoie des données calculées vers le graphique
                    gr.updateDatasets(graphData, gr.GRAPHMONTHS, gr.CATEGORIES, gr.dataYears, gr.dataMonths);
                    
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