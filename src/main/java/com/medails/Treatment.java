package com.medails;

import java.awt.Desktop;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

    /************************************************************ 
                        TRAITEMENT DE DONNEES
    *************************************************************/

public class Treatment
{
    /************************* Variables d'instance **************************/

    // Répertoire Facture 
    private final String DIRECTORY_FACTURE = "M://Multimédia/Bureau/Social/Social - Pc Bureau/01 - Professionnelle/Factures";
    private final String REP1_FACTURE = "01 - Professionnelle";
    private final String PDF1_FACTURE = "Facture";

    // Répertoire Déclaration
    private final String DIRECTORY_DECLA = "M://Multimédia/Bureau/Social/Social - Pc Bureau/00 - Gouvernement/URSSAF/Déclarations";
    private final String REP2_DECLA = "00 - Gouvernement";
    private final String PDF2_DECLA = "Déclaration";
        
    // Taux de taxe
    private final Double ACRE2024 = ((2.2 + 11.6 + 0.2) / 100);  // Année 2024 (ACRE)
    private final Double ACRE2025 = ((2.2 + 12.3 + 0.2) / 100);  // Année 2025 (ACRE)
    private final Double SANS2025 = ((2.2 + 24.6 + 0.2) / 100);  // Année 2025 (sans ACRE)
    private final Double SANS20XX = ((2.2 + 26.1 + 0.2) / 100);  // Année 2026 ou plus
    private final Double TVA = 1.2;

    /*********** Panel 1 ***************/ 
    // Variables pour calcules
    private double TTC1 = 0.0;
    private double HT1 = 0.0;
    private double TVA1 = 0.0;
    private double Taxe1 = 0.0;
    private double Benefit1 = 0.0;

    // Données pour création graphique
    public static Double[][][][][] graphData = new Double[12][12][12][12][12];

    /************* Déclarations Classes ****************/
    private Display display;
    private Graphic graphic;
    private ReadFile readFile;

    /*********** Constructeur ***************/
    public Treatment(Display display, Graphic graphic, ReadFile readFile)
    {
        this.display = display;
        this.graphic = graphic;  
        this.readFile = readFile;  
        actionJElements();  
        /*********** Panel 1 ***************/      
        correctNumber(display.txtDays);         
        correctNumber(display.txtTJM);          
        clearListener1();                      
        /*********** Panel 2 ***************/
        clearListener2();     
        graphic.updateDatasets(graphData);                 
    }

    public void actionJElements()
    {
        /*********** Panel 1 ***************/
        display.btOpenFacture.addActionListener(e -> openPDF(display.boxRep1, display.boxPDF1));
        display.btOpenDecla.addActionListener(e -> openPDF(display.boxRep2, display.boxPDF2));
        display.btTVA.addActionListener(e -> calculateListener());
        display.btSearchFacture.addActionListener(e -> searchDirectory(display.boxRep1, display.boxPDF1, DIRECTORY_FACTURE));
        display.btSearchDecla.addActionListener(e -> searchDirectory(display.boxRep2, display.boxPDF2, DIRECTORY_DECLA));
        display.btSave.addActionListener(e -> saveDataListener());
        popupListener(display.boxRep1, display.boxPDF1, REP1_FACTURE, PDF1_FACTURE);
        popupListener(display.boxRep2, display.boxPDF2, REP2_DECLA, PDF2_DECLA);
        display.btReset1.addActionListener(e -> clearListener1());
        /*********** Panel 2 ***************/
        display.boxYearsTotal.addActionListener(e -> graphicListener());
        display.btReset2.addActionListener(e -> clearListener2());
    }

    /*********************************************************** 
                              PANEL 1 
    ***********************************************************/
    
    // Vérification chiffre dans champ saisie 
    private void correctNumber(JTextField textField) 
    {
        textField.addFocusListener(new FocusAdapter() 
        {
            @Override
            public void focusLost(FocusEvent e) 
            {
                String text = textField.getText().trim();
                try
                {
                    Double.parseDouble(text);                     
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(display.fen, "Veuillez entrer un nombre valide",
                                                                "Erreur", JOptionPane.ERROR_MESSAGE);
                                                                       textField.setText("");
                } 
            }
        });
    }


    // B3 -> Calculer
    private void calculateListener() 
    {
        try
        {    
            String years = (String) display.boxYears.getSelectedItem();     // Pan1 - A1
            String months = (String) display.boxMonths.getSelectedItem();   // Pan1 - A2   
            double days1 = Double.parseDouble(display.txtDays.getText());   // Pan1 - B1 
            double TJM1 = Double.parseDouble(display.txtTJM.getText());     // Pan1 - B2

            // Vérification cellule non-vide + Calcule : Année + Mois
            if ((years == "") || (months == "")) 
            {
                JOptionPane.showMessageDialog(null, "Veuillez entrer une date (année + mois)",
                                                    "Demande d'informations", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Récupération des valeurs 
            HT1 = days1 * TJM1;
            TTC1 = HT1 * TVA;
            TVA1 = TTC1 - HT1;

            // Calcule Facture (HT + TTC)
            if (! display.txtDays.getText().isEmpty() && ! display.txtTJM.getText().isEmpty())
            {
                String HT1String = Double.toString(HT1);
                display.txtHT.setText(HT1String);                   
                String TTC1String = Double.toString(TTC1);
                display.txtTTC.setText(TTC1String);
                String TVA1String = Double.toString(TVA1);
                display.txtTVA.setText(TVA1String);
            }

            // Calcule URSSAF (Taxe) 
            // Année 2024 (ACRE)
            if (years == "2024")
            {
                Taxe1 = HT1 * ACRE2024;
            }

            // Année 2025 (ACRE)
            else if ((years == "2025") && 
                     (months == "Janvier" ||
                      months == "Février"||
                      months == "Mars" ||
                      months == "Avril")) 
            {
                Taxe1 = HT1 * ACRE2025;
            }

            // Année 2025 (sans ACRE)
            else if ((years == "2025") && 
                     (months == "Mai" ||
                      months == "Juin"||
                      months == "Juillet" ||
                      months == "Août" ||                    
                      months == "Septembre" ||                    
                      months == "Novembre" ||                    
                      months == "Décembre"))                    
            {
                Taxe1 = HT1 * SANS2025;
            }

            // Année 2026 ou plus
            else
            {
                Taxe1 = HT1 * SANS20XX;
            }              

            // Résultat de calcul
            String decimal = String.format("%.1f", Taxe1);
            display.txtTaxe.setText(decimal);

            // Calcul URSSAF (Différence)
            Benefit1 = HT1 - Taxe1;
            String Benefit1toString = Double.toString(Benefit1);
            display.txtBenefit.setText(Benefit1toString);
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(null, "Veuillez entrez des nombres valides",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Méthode générique pour ouvrir un PDF
    private void openPDF(JComboBox<String> boxRep, JComboBox<String> boxPDF)
    {
        String selectedRep = (String) boxRep.getSelectedItem();
        String selectedPDF = (String) boxPDF.getSelectedItem();

        if (selectedRep == null || selectedPDF == null || selectedRep.isEmpty() || selectedPDF.isEmpty())
        {
            JOptionPane.showMessageDialog(display.fen, "Veuillez sélectionner un fichier PDF dans l'onglet lien",
                                                        "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File file = new File(selectedRep + "/" + selectedPDF);
        try
        {
            if (file.exists())
            {
                Desktop.getDesktop().open(file);
            }
            else
            {
                throw new IOException("Fichier introuvable");
            }
        }
        catch(IOException ex)
        {
            JOptionPane.showMessageDialog(display.fen, "Le fichier PDF : " + file.getAbsolutePath() + "est introuvable",
                                                                            "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    // Méthode générique pour ouvrir un répertoire
    private void searchDirectory(JComboBox<String> boxRep, JComboBox<String> boxPDF, String directory)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(directory));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Type : .PDF", "pdf"));

        int result = fileChooser.showOpenDialog(display.fen);
        File selectedRep = fileChooser.getSelectedFile();
        
        if (result == JFileChooser.APPROVE_OPTION)
        {
            if (selectedRep != null)
            {
                // MAJ des répertoires
                String parentDirectory = selectedRep.getParent();
                boxRep.removeAllItems();
                boxRep.addItem(parentDirectory);

                // MAJ des fichiers PDF
                String namePDF = selectedRep.getName();
                boxPDF.removeAllItems();
                boxPDF.addItem(namePDF);
            }
        }
    }


    // Méthode générique les onglets répertoires
    private void popupListener(JComboBox<String> boxRep, JComboBox<String> boxPDF, String searchRep, String searchPDF) 
    {
        boxRep.addPopupMenuListener(new PopupMenuListener() 
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                readFile.existFile();
    
                // Lecture des fichiers et extraction des données
                Set<String> setRep = readFile.readLinesContaining(searchRep);
                Set<String> setPDF = readFile.readLinesStarting(searchPDF);
    
                // Conversion en List pour le tri
                List<String> arrayRep = new ArrayList<>(setRep);
                List<String> arrayPDF = new ArrayList<>(setPDF);
    
                // Tri des listes
                Collections.sort(arrayRep);
                Collections.sort(arrayPDF);
    
                // Mise à jour des ComboBox
                updateComboBox(boxRep, arrayRep);
                updateComboBox(boxPDF, arrayPDF);
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
    
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    private void updateComboBox (JComboBox<String> comboBox, List<String> allItems)
    {
        comboBox.removeAllItems();
        comboBox.addItem("");
        for (String item : allItems)
        {
            comboBox.addItem(item);
        }
    }


    // K1 -> Enrengistrer
    public void saveDataListener()
    {
        // Vérification cellule non-vide : Facture + Déclaration
        if ((display.boxRep1.getSelectedItem()) == null || (display.boxPDF1.getSelectedItem() == null) ||
            (display.boxRep2.getSelectedItem()) == null || (display.boxPDF2.getSelectedItem() == null || display.datePay.getDate() == null))
        {
            JOptionPane.showMessageDialog(null, "Veuillez compléter tous les champs avant de continuer",
                                          "Champs vides", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Date getPay = display.datePay.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

        /* A1 */  String slctYears = (String)           "Année --> " + display.boxYears.getSelectedItem();
        /* A2 */  String slctMonths = (String)          "Mois --> " + display.boxMonths.getSelectedItem();
        /* A3 */  String slctPay = (String)             "Versement --> " + dateFormat.format(getPay);
        /* B1 */  String slctDays = (String)            "Jours --> " + display.txtDays.getText();
        /* B2 */  String slctTJM = (String)             "TJM --> " + display.txtTJM.getText();
        /* C1 */  String slctTTC = (String)             "TTC --> " + display.txtTTC.getText();
        /* C2 */  String slctHT = (String)              "HT --> " + display.txtHT.getText();
        /* C3 */  String slctTVA = (String)             "TVA --> " + display.txtTVA.getText();
        /* D2 */  String slctTaxe = (String)            "Taxe --> " + display.txtTaxe.getText(); 
        /* D1 */  String slctBenefit = (String)         "Benefice --> " + display.txtBenefit.getText();
        /* G1 */  String slctFactureRep = (String)      display.boxRep1.getSelectedItem();
        /* H1 */  String slctFacturePDF = (String)      display.boxPDF1.getSelectedItem();
        /* I1 */  String slctDeclaraRep = (String)      display.boxRep2.getSelectedItem();
        /* J1 */  String slctDeclaraPDF = (String)      display.boxPDF2.getSelectedItem();
                  String seperator = "<->";

        // Vérification de l'existence du fichier
        readFile.existFile();

        // Vérification si la ligne existe déjà
        Set<String> setContains = readFile.readLinesContaining(slctFacturePDF);

        if (setContains.contains(slctFacturePDF))   
        {
            JOptionPane.showMessageDialog(null, "Un fichier PDF avec le même nom existe déjà",
                                                                  "Doublon", JOptionPane.INFORMATION_MESSAGE);   
            return;
        }

        // Création de la liste des lignes à écrire
        List<String> linesToWrite = Arrays.asList(slctYears, slctMonths, slctPay, slctDays, slctTJM,
                                                  slctTTC, slctHT, slctTVA, slctTaxe, slctBenefit,
                                                  slctFactureRep, slctFacturePDF, slctDeclaraRep,slctDeclaraPDF, seperator);

        // Utilisation de la méthode d'écriture
        readFile.writeLinesContaining(linesToWrite, readFile.filePath);
        JOptionPane.showMessageDialog(null, "Le programme a bien été enregistré",
                                                    "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
    }


    // K2 -> RAZ
    public void clearListener1()
    {
        /* A1 */ display.boxYears.setSelectedItem("");
        /* A2 */ display.boxMonths.setSelectedItem("");
        /* A3 */ display.datePay.setDate(null); 
        /* B1 */ display.txtDays.setText("");  
        /* B2 */ display.txtTJM.setText("");
        /* C1 */ display.txtTTC.setText("");
        /* C2 */ display.txtHT.setText("");
        /* C3 */ display.txtTVA.setText("");
        /* D1 */ display.txtTaxe.setText("");
        /* D2 */ display.txtBenefit.setText("");
        /* F1 */ display.boxRep1.removeAllItems();
        /* G1 */ display.boxPDF1.removeAllItems();
        /* I1 */ display.boxRep2.removeAllItems();
        /* J1 */ display.boxPDF2.removeAllItems(); 
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
                      
                    /* B1 */ String resultatTTC = Double.toString(totalTTC);
                    /* B1 */ display.txtTotalTTC.setText(resultatTTC);
                    /* B2 */ String resultatHT = (String.format("%.1f", totalHT)); 
                    /* B2 */ display.txtTotalHT.setText(resultatHT);
                    /* B3 */ String resultatTVA = (String.format("%.1f", totalTVA));
                    /* B3 */ display.txtTotalTVA.setText(resultatTVA);
                    /* C1 */ String resultatTaxe = String.format("%.1f", totalTaxe);
                    /* C1 */ display.txtTotalTaxe.setText(resultatTaxe);
                    /* C2 */ String resultatBenefit = String.format("%.1f", totalBenefit);
                    /* C2 */ display.txtTotalBenefit.setText(resultatBenefit);

                    /************************* GRAPHIQUE **************************/ 

                    /* B1 */ currentTTC = graphTTC;
                    /* B2 */ currentHT = Double.parseDouble(convTotalHT.replace(",", "."));
                    /* B3 */ currentTVA = graphTVA;
                    /* C1 */ currentTaxe = graphTaxe;
                    /* C2 */ currentBenefit = graphBenefit;        

                    boolean filterTTC = false;
                    boolean filterTVA = false;
                    boolean filterHT = false;
                    boolean filterTaxe = false;
                    boolean filterBenefit = false;

                    // Trouve le mois correspondant et stocke la valeur HT dans le tableau
                    for (int kk = 0; kk < graphic.GRAPHMONTHS.length; kk++) 
                    {
                        if (graphic.GRAPHMONTHS[kk].equals(currentMonth)) 
                        { 
                            graphData[kk][kk][kk][kk][0] = currentTTC;       // TTC      
                            graphData[kk][kk][kk][kk][1] = currentTVA;       // TVA           
                            graphData[kk][kk][kk][kk][2] = currentHT;        // HT       
                            graphData[kk][kk][kk][kk][3] = currentTaxe;      // URSSAF   
                            graphData[kk][kk][kk][kk][4] = currentBenefit;   // Bénéfices 
                        }
                        else
                        {
                            graphData[kk][kk][kk][kk][0] = null;  // TTC        
                            graphData[kk][kk][kk][kk][1] = null;  // TVA        
                            graphData[kk][kk][kk][kk][2] = null;  // HT         
                            graphData[kk][kk][kk][kk][3] = null;  // URSSAF    
                            graphData[kk][kk][kk][kk][4] = null;  // Bénéfices 
                        }
                    }
                    // Renvoie des données calculées vers le graphique
                    graphic.updateDatasets(graphData);
                    
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