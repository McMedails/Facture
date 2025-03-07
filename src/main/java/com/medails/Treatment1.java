package com.medails;

import java.awt.Desktop;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
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

public class Treatment1
{
    /************************* Variables d'instance **************************/
    
    // Taux de taxe
    private final Double ACRE2024 = ((2.2 + 11.6 + 0.2) / 100);  // Année 2024 (ACRE)
    private final Double ACRE2025 = ((2.2 + 12.3 + 0.2) / 100);  // Année 2025 (ACRE)
    private final Double SANS2025 = ((2.2 + 24.6 + 0.2) / 100);  // Année 2025 (sans ACRE)
    private final Double SANS20XX = ((2.2 + 26.1 + 0.2) / 100);  // Année 2026 ou plus
    private final Double TVA = 1.2;

    // Répertoire Facture 
    private final String DIRECTORY_FACTURE = "M://Multimédia/Bureau/Social/Social - Pc Bureau/01 - Professionnelle/Factures";
    private final String REP1_FACTURE = "01 - Professionnelle";
    private final String PDF1_FACTURE = "Facture";

    // Répertoire Déclaration
    private final String DIRECTORY_DECLA = "M://Multimédia/Bureau/Social/Social - Pc Bureau/00 - Gouvernement/URSSAF/Déclarations";
    private final String REP2_DECLA = "00 - Gouvernement";
    private final String PDF2_DECLA = "Déclaration";
    
    /*********** Panel 1 ***************/ 
    // Variables pour calcules
    private double TTC1 = 0.0;
    private double HT1 = 0.0;
    private double TVA1 = 0.0;
    private double Taxe1 = 0.0;
    private double Benefit1 = 0.0;

    /************* Déclarations Classes ****************/
    private Display display;
    private ReadFile readFile;

    /*********** Constructeur ***************/
    public Treatment1(Display display, ReadFile readFile)
    {
        this.display = display;
        this.readFile = readFile;  
         
        /*********** Panel 1 ***************/ 
        actionJElements();      
        correctNumber(display.txtDays);         
        correctNumber(display.txtTJM);          
        clearListener1();                                                 
    }

    public void actionJElements()
    {
        /*********** Panel 1 ***************/
        display.btOpenFacture           .addActionListener (e -> openPDF(display.boxRep1, display.boxPDF1));
        display.btOpenDecla             .addActionListener (e -> openPDF(display.boxRep2, display.boxPDF2));
        display.btTVA                   .addActionListener (e -> calculateListener());
        display.btSearchFacture         .addActionListener (e -> searchDirectory(display.boxRep1, display.boxPDF1, DIRECTORY_FACTURE));
        display.btSearchDecla           .addActionListener (e -> searchDirectory(display.boxRep2, display.boxPDF2, DIRECTORY_DECLA));
        display.btSave                  .addActionListener (e -> saveDataListener());
                                        popupListener      (display.boxRep1, display.boxPDF1, REP1_FACTURE, PDF1_FACTURE);
                                        popupListener      (display.boxRep2, display.boxPDF2, REP2_DECLA, PDF2_DECLA);
        display.btReset1                .addActionListener (e -> clearListener1());
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
            String years = (String) display.boxYears.getSelectedItem();    
            String months = (String) display.boxMonths.getSelectedItem();     
            double days1 = Double.parseDouble(display.txtDays.getText());   
            double TJM1 = Double.parseDouble(display.txtTJM.getText());    

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
}
