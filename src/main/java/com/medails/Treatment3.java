package com.medails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JOptionPane;

    /************************************************************ 
                        TRAITEMENT DE DONNEES
    *************************************************************/

public class Treatment3
{
    /************************* Variables de classe **************************/

    // Données pour création graphique
    public static Double[][][] graphDecenal = new Double[5][5][5]; 
    public static Double[][][] graphYearMonth = new Double[12][12][12];      

    /************************* Variables d'instance **************************/

    // Répertoire Facture 
    private final String DIRECTORY_DEDUCTION = "M://Multimédia/Bureau/Social/Social - Pc Bureau/01 - Professionnelle/Achats";
    private final String REP_DEDUCTION = "01 - Professionnelle";
    private final String PDF_DEDUCTION = "Achats";

    /************* Déclarations Classes ****************/
    private Display dp;
    private Graphic gr;
    private ReadWrite rwOther;
    private Treatment1 tr1;
    private Treatment2 tr2;

    /*********** Constructeur ***************/
    public Treatment3(Display dp, Graphic gr, ReadWrite rwOther, Treatment1 tr1, Treatment2 tr2)
    {
        this.dp = dp;
        this.gr = gr;   
        this.rwOther = rwOther;  
        this.tr1 = tr1;
        this.tr2 = tr2;

        /*********** Appels Méthodes ***************/
        actionJElements();
        calculate();
        graphDecenal();
        graphYearMonth();   
        gr.updateDatasets(graphYearMonth, gr.GRAPHMONTHS, gr.SHORTCATEGORIES, gr.dataYearsPan3, gr.dataMonthsPan3);                     
    }

    private void actionJElements()
    {
        /*********** Partie Graphique ***************/
        dp.sliDecadePan3           .addChangeListener (e -> graphDecenal());
        dp.sliYearMonthPan3        .addChangeListener (e -> graphYearMonth());

        /*********** Partie Text ***************/
        dp.txtTTCPan3              .addActionListener (e -> calculate());
        dp.boxYearsDeduction       .addActionListener (e -> graphYearMonth());
        dp.butOpenDeduction        .addActionListener (e -> tr1.openPDF(dp.boxRepDeduction, dp.boxPDFDeduction));
        dp.butSearchDeduction      .addActionListener (e -> tr1.searchDirectory(dp.boxRepDeduction, dp.boxPDFDeduction, DIRECTORY_DEDUCTION));
        dp.butSaveDeduction        .addActionListener (e -> saveDataListener());
                                    tr1.popupListener (dp.boxRepDeduction, dp.boxPDFDeduction, REP_DEDUCTION, PDF_DEDUCTION);
        dp.butResetDeduction       .addActionListener (e -> clearListener());     
    }

    /*********************************************************** 
                           Onglet Décénie
    ***********************************************************/

    public void graphDecenal()
    {
        // Mise à jour des graphiques avec la nouvelle échelle
        tr2.slideRange(gr.chartDecadePan3, dp.sliDecadePan3); 

        // Vérification existance du fichier
        rwOther.existFile();

        // Initialisation des données graphiques
        gr.dataDecadePan3.clear();

        // Initialisation des données
        String currentMonth = null;
        String currentYear  = null;
        String currentTTC   = null;
        String currentHT    = null;
        String currentTVA   = null;
        double graphTTC     = 0.0;
        double graphHT      = 0.0;
        double graphTVA     = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(rwOther.getFile()))) 
        {
            while ((rwOther.line = reader.readLine()) != null) 
            {
                // Recherche des mois de versement
                if (rwOther.line.contains("Date --> "))
                {    
                    String monthPart = rwOther.line.substring(rwOther.line.indexOf("Date --> ") + 12).trim();
                    int firstSpaceIndex = monthPart.indexOf(" ");
                    if (firstSpaceIndex != -1)
                    { currentMonth = monthPart.substring(0, firstSpaceIndex).trim(); }
                    currentYear = rwOther.line.substring(rwOther.line.length() - 4).trim();
                }
    
                // Recherche de l'ensemble des informations 
                if (rwOther.line.contains("Deduction TTC --> "))
                {    
                    currentTTC = rwOther.line.substring(rwOther.line.indexOf("Deduction TTC --> ") + 18).trim();
                    graphTTC = Double.parseDouble(currentTTC);
                }
                if (rwOther.line.contains("Deduction HT --> "))
                {
                    currentHT  = rwOther.line.substring(rwOther.line.indexOf("Deduction HT --> " ) + 17).trim();
                    graphHT  = Double.parseDouble(currentHT);
                }
                if (rwOther.line.contains("Deduction TVA --> "))               
                {
                    currentTVA = rwOther.line.substring(rwOther.line.indexOf("Deduction TVA --> ") + 18).trim();
                    graphTVA = Double.parseDouble(currentTVA);
                }

                /************************* GRAPHIQUE **************************/ 
                // Réinitialise graphData pour éviter les valeurs résiduelles           
                for (int kk = 0; kk < gr.GRAPHYEARS.length; kk++)
                {
                    for (int ll = 0; ll < gr.SHORTCATEGORIES.length; ll++) 
                    { 
                        graphDecenal[kk][kk][ll] = null; 
                    }
                }

                // Trouve le mois correspondant et stocke la valeur dans le tableau
                for (int ii = 0; ii < gr.GRAPHYEARS.length; ii++)
                {
                    if (gr.GRAPHYEARS[ii].equals(currentYear))
                    {
                        graphDecenal[ii][ii][0] = graphTTC;   
                        graphDecenal[ii][ii][1] = graphTVA;        
                        graphDecenal[ii][ii][2] = graphHT; 
                    }
                }
                
                // Renvoie des données calculées vers le graphique
                gr.updateDatasets(graphDecenal, gr.GRAPHYEARS, gr.SHORTCATEGORIES, gr.dataDecadePan3); 
            }

            // Réinitialise pour le prochain mois
            currentMonth   = null;
            graphTTC       = 0.0;
            graphHT        = 0.0;
            graphTTC       = 0.0;
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
        tr2.slideRange(gr.chartYearsPan3, dp.sliYearMonthPan3);
        tr2.slideRange(gr.chartMonthsPan3, dp.sliYearMonthPan3);  

        // Vérification existance du fichier
        rwOther.existFile();

        // Initialisation des données graphiques
        gr.dataYearsPan3.clear();
        gr.dataMonthsPan3.clear();

        // Initialisation des données
        String currentMonth = null;
        String currentYear  = null;
        String currentTTC   = null;
        String currentHT    = null;
        String currentTVA   = null;
        double graphTTC     = 0.0;
        double graphHT      = 0.0;
        double graphTVA     = 0.0;

        String selectedYear = (String) dp.boxYearsDeduction.getSelectedItem();
        if (selectedYear.isEmpty()) { clearListener(); return;}
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(rwOther.getFile()))) 
            {
                while ((rwOther.line = reader.readLine()) != null) 
                {
                    // Recherche des mois de versement
                    if (rwOther.line.contains("Date --> ") &&  rwOther.line.contains(selectedYear))
                    {    
                        String monthPart = rwOther.line.substring(rwOther.line.indexOf("Date --> ") + 12).trim();
                        int firstSpaceIndex = monthPart.indexOf(" ");
                        if (firstSpaceIndex != -1)
                        { currentMonth = monthPart.substring(0, firstSpaceIndex).trim(); }
                        currentYear = rwOther.line.substring(rwOther.line.length() - 4).trim();
                    }
        
                    // Recherche de l'ensemble des informations 
                    if (rwOther.line.contains("Deduction TTC --> "))
                    {    
                        currentTTC = rwOther.line.substring(rwOther.line.indexOf("Deduction TTC --> ") + 18).trim();
                        graphTTC = Double.parseDouble(currentTTC);
                    }
                    if (rwOther.line.contains("Deduction HT --> "))
                    {
                        currentHT  = rwOther.line.substring(rwOther.line.indexOf("Deduction HT --> " ) + 17).trim();
                        graphHT  = Double.parseDouble(currentHT);
                    }
                    if (rwOther.line.contains("Deduction TVA --> "))               
                    {
                        currentTVA = rwOther.line.substring(rwOther.line.indexOf("Deduction TVA --> ") + 18).trim();
                        graphTVA = Double.parseDouble(currentTVA);
                    }

                    /************************* GRAPHIQUE **************************/ 
                    // Réinitialise graphData pour éviter les valeurs résiduelles           
                    for (int kk = 0; kk < gr.GRAPHMONTHS.length; kk++)
                    {
                        for (int ll = 0; ll < gr.SHORTCATEGORIES.length; ll++) 
                        { 
                            graphYearMonth[kk][kk][ll] = null; 
                        }
                    }

                    // Trouve le mois correspondant et stocke la valeur dans le tableau
                    for (int ii = 0; ii < gr.GRAPHYEARS.length; ii++)
                    {
                        if (gr.GRAPHYEARS[ii].equals(currentYear))
                        {
                            for (int kk = 0; kk < gr.GRAPHMONTHS.length; kk++)
                            {
                                if (gr.GRAPHMONTHS[kk].equals(currentMonth)) 
                                {
                                    graphYearMonth[kk][kk][0] = graphTTC;   
                                    graphYearMonth[kk][kk][1] = graphTVA;        
                                    graphYearMonth[kk][kk][2] = graphHT; 
                                }
                            }
                        }
                    }
                    
                    // Renvoie des données calculées vers le graphique
                    gr.updateDatasets(graphYearMonth, gr.GRAPHMONTHS, gr.SHORTCATEGORIES, gr.dataYearsPan3, gr.dataMonthsPan3); 
                }

                // Réinitialise pour le prochain mois
                currentMonth   = null;
                graphTTC       = 0.0;
                graphHT        = 0.0;
                graphTTC       = 0.0;
            }
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(null, "Erreur lors du calcul : " + ex.getMessage(),
                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /*********************************************************** 
                          Autres Méthodes
    ***********************************************************/

    // Calcule HT/TVA
    private void calculate()
    {
        // Vérification chiffre dans champ saisie 
        tr1.correctNumber(dp.txtTTCPan3);
        tr1.correctNumber(dp.txtHTPan3);
        tr1.correctNumber(dp.txtTVAPan3);

        if (!dp.txtTTCPan3.getText().isEmpty())
        {
            try
            {
                double TTC = Double.parseDouble(dp.txtTTCPan3.getText()); 
                double HT  = TTC / 1.2;
                double TVA = TTC - HT;

                dp.txtHTPan3.setText(String.valueOf(HT));
                dp.txtTVAPan3.setText(String.valueOf(TVA));
            }
            catch (NumberFormatException e)
            {
                // Si le text n'est pas un nombre valide
                tr1.correctNumber(dp.txtTTCPan3);
                dp.txtTTCPan3.setText("");
                dp.txtHTPan3.setText("");
                dp.txtTVAPan3.setText("");
            }
        }
        else
        {
            // Efface si le champ TTC est vide
            dp.txtHTPan3.setText("");
            dp.txtTVAPan3.setText("");           
        }         
    }


   // F1 -> Enrengistrer
    public void saveDataListener()
    {
        // Vérification cellule non-vide : Déduction
        if (dp.dateDeduction.getDate() == null ||
            dp.txtTTCPan3.getText().isEmpty() || dp.txtHTPan3.getText().isEmpty()  || dp.txtTVAPan3.getText().isEmpty()  || 
            dp.boxRepDeduction.getSelectedItem() == null || dp.boxPDFDeduction.getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(null, "Veuillez compléter tous les champs avant de continuer",
                                          "Champs vides", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Date getPay = dp.dateDeduction.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

        /* A1 */  String slctDate = (String)             "Date --> " + dateFormat.format(getPay);
        /* B1 */  String slctTTC = (String)              "Deduction TTC --> " + dp.txtTTCPan3.getText();
        /* B2 */  String slctHT = (String)               "Deduction HT --> " + dp.txtHTPan3.getText();
        /* B3 */  String slctTVA = (String)              "Deduction TVA --> " + dp.txtTVAPan3.getText();
        /* D1 */  String slctDeductionRep = (String)      dp.boxRepDeduction.getSelectedItem();
        /* E1 */  String slctDeductionPDF = (String)      dp.boxPDFDeduction.getSelectedItem();
                  String seperator = "<->";

        // Vérification de l'existence du fichier
        rwOther.existFile();

        // Vérification si la ligne existe déjà
        Set<String> setContains = rwOther.readLinesContaining(slctDeductionPDF);

        if (setContains.contains(slctDeductionPDF))   
        {
            JOptionPane.showMessageDialog(null, "Un fichier PDF avec le même nom existe déjà",
                                                                  "Doublon", JOptionPane.INFORMATION_MESSAGE);   
            return;
        }

        // Création de la liste des lignes à écrire
        List<String> linesToWrite = Arrays.asList(slctDate, slctTTC, slctHT, slctTVA, slctDeductionRep, slctDeductionPDF, seperator);

        // Utilisation de la méthode d'écriture
        rwOther.writeLinesContaining(linesToWrite);
        JOptionPane.showMessageDialog(null, "Le programme a bien été enregistré",
                                                    "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
    }
    

    // F2 -> RAZ
    private void clearListener()
    {
        /* A1 */ dp.dateDeduction.setDate(null); 
        /* B1 */ dp.txtTTCPan3.setText(""); 
        /* B2 */ dp.txtHTPan3.setText(""); 
        /* B3 */ dp.txtTVAPan3.setText(""); 
        /* D1 */ dp.boxRepDeduction.setSelectedItem("");
        /* D2 */ dp.boxPDFDeduction.setSelectedItem("");
    } 
}