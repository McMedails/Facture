package com.medails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

    /*********************************************************** 
                             MISE EN FORME 
    ***********************************************************/

public class Display
{    
    /************************* Variables d'instance **************************/
    
    public JFrame fen;
    public JPanel pan1;
    public JPanel pan2;
    public JPanel pan3;
    public JScrollPane scroll1;
    public JScrollPane scroll2;
    public JScrollPane scroll3;
    public JTabbedPane tabMain;
    public JTabbedPane tabGraph;    
    public JTabbedPane tabDeduction;    
    public GridBagConstraints gbc;

    /*********** Onglet 1 ***************/
    public JComboBox<String>     boxYears;               { boxYears             = new JComboBox<String> ();}    // A1 - Années
    public JComboBox<String>     boxMonths;              { boxMonths            = new JComboBox<String> ();}    // A2 - Mois
    public JDateChooser          datePay;                { datePay              = new JDateChooser      ();}    // A3 - Date de paiement
    public JTextField            txtDays;                { txtDays              = new JTextField        ();}    // B1 - Jours travaillés
    public JTextField            txtTJM;                 { txtTJM               = new JTextField        ();}    // B2 - TJM
    public JButton               btTVA;                  { btTVA                = new JButton           ();}    // B3 - Calcule TVA (BP)
    public JTextField            txtTTC;                 { txtTTC               = new JTextField        ();}    // C1 - Résultat TTC
    public JTextField            txtHT;                  { txtHT                = new JTextField        ();}    // C2 - Résultat HT
    public JTextField            txtTVA;                 { txtTVA               = new JTextField        ();}    // C3 - Différence TVA  
    public JTextField            txtTaxe;                { txtTaxe              = new JTextField        ();}    // D1 - Taxe URSSAF
    public JTextField            txtBenefit;             { txtBenefit           = new JTextField        ();}    // D2 - Différence Taxe
    public JButton               btOpenFacture;          { btOpenFacture        = new JButton           ();}    // E1 - Ouvrir facture
    public JButton               btSearchFacture;        { btSearchFacture      = new JButton           ();}    // E2 - Parcourir facture
    public JButton               btRep1;                 { btRep1               = new JButton           ();}    // ... - Renseignement du répertoire -> Facture
    public JComboBox<String>     boxRep1;                { boxRep1              = new JComboBox<String> ();}    // F1 - Barre de recherche Facture (Réperoitre)
    public JComboBox<String>     boxPDF1;                { boxPDF1              = new JComboBox<String> ();}    // G1 - Barre de recherche Facture (Nom du PDF) 
    public JButton               btOpenDecla;            { btOpenDecla          = new JButton           ();}    // H1 - Ouvrir déclaration
    public JButton               btSearchDecla;          { btSearchDecla        = new JButton           ();}    // H2 - Parcourir déclaration
    public JButton               btPDF1;                 { btPDF1               = new JButton           ();}    // ... -  Renseignement du répertoire -> Déclaration
    public JComboBox<String>     boxRep2;                { boxRep2              = new JComboBox<String> ();}    // I1 - Barre de recherche Déclaration (Réperoitre)
    public JComboBox<String>     boxPDF2;                { boxPDF2              = new JComboBox<String> ();}    // J1 - Barre de recherche Déclaration (Nom du PDF)
    public JButton               btSave;                 { btSave               = new JButton           ();}    // K1 - Enregistrer
    public JButton               btReset1;               { btReset1             = new JButton           ();}    // K2 - RAZ

    /*********** Onglet 2 ***************/
    public JCheckBox             cckTTC;                 { cckTTC               = new JCheckBox         ("", true);}    // A1 - TTC
    public JCheckBox             cckTVA;                 { cckTVA               = new JCheckBox         ("", true);}    // A2 - TVA
    public JCheckBox             cckHT;                  { cckHT                = new JCheckBox         ("", true);}    // A3 - HT
    public JCheckBox             cckTaxe  ;              { cckTaxe              = new JCheckBox         ("", true);}    // A4 - Urssaf
    public JCheckBox             cckBenefit;             { cckBenefit           = new JCheckBox         ("", true);}    // A5 - Bénéfices
    public JComboBox<String>     boxYearsTotal;          { boxYearsTotal        = new JComboBox<String> ();}                          // B1 - Années
    public JTextField            txtTotalTTC;            { txtTotalTTC          = new JTextField        ();}                          // C1 - Résultat Total TTC
    public JTextField            txtTotalHT;             { txtTotalHT           = new JTextField        ();}                          // C2 - Résultat Total HT
    public JTextField            txtTotalTVA;            { txtTotalTVA          = new JTextField        ();}                          // C3 - Taxe Total
    public JTextField            txtTotalTaxe;           { txtTotalTaxe         = new JTextField        ();}                          // D1 - Taxe Total
    public JTextField            txtTotalBenefit;        { txtTotalBenefit      = new JTextField        ();}                          // D2 - Bénéfice Total
    public JButton               btReset2;               { btReset2             = new JButton           ();}                          // D3 - RAZ

    /*********** Onglet 3 ***************/  
    public JCheckBox             cckTotal;               { cckTotal               = new JCheckBox       ("", true);}    // A1 - Déduction

    // Année et mois
    private String years[] = {"", "2024", "2025", "2026", "2027", "2028"};
    private String months[] = {"", "Janvier", "Février", "Mars", "Avril", 
                                      "Mai", "Juin", "Juillet", "Août", "Septembre", 
                                      "Octobre", "Novembre", "Décembre"};
        
    /*********** Constructeur ***************/
    public Display()
    {   
        fenPosition();
        pan1Position();
        pan2Position();
        pan3Position();
    }

    public void fenPosition()
    {
        // Création Fenetre/Panel
        fen = new JFrame();
        pan1 = new JPanel();
        pan2 = new JPanel();
        pan3 = new JPanel();

        // Configuration Fenetre/Panel
        fen.setTitle("Gestionnaie de facture");
        fen.setSize(410, 610);
        fen.setLocationRelativeTo(null);
        fen.setResizable(false);
        pan1.setBackground(Color.LIGHT_GRAY);
        pan2.setBackground(Color.LIGHT_GRAY);
        pan3.setBackground(Color.LIGHT_GRAY);
        pan1.setLayout(new GridBagLayout());   
        pan2.setLayout(new GridBagLayout());   
        pan3.setLayout(new GridBagLayout());   

        // Ajout du scroll aux panels
        scroll1 = new JScrollPane(pan1);
        scroll2 = new JScrollPane(pan2);
        scroll3 = new JScrollPane(pan3);
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Onglets - utilisation les JScrollPane au lieu des JPanel
        tabMain = new JTabbedPane();
        tabMain.add("Enregistrement", scroll1);
        tabMain.add("Graphique", scroll2);
        tabMain.add("Déducttion", scroll3);
        tabGraph = new JTabbedPane();
        tabDeduction = new JTabbedPane();
        // Ajout des onglets dans Fenetre
        fen.add(tabMain, BorderLayout.CENTER);

        // Placement des composants
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 10, 10);

        // Fermeture de la fenetre
        fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fen.setVisible(true); 
    }

    /************************** Factorisation **************************/

    private void addComposant (JPanel panel, JComponent component, 
                               int gridx, int gridy, int gridwidth)
    {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        panel.add(component, gbc);
    }

    private JTextField createTextField (int width, int height)
    {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width, height));
        textField.setEnabled(true);
        return textField;
    }

    private JComboBox<String> createJComboBox (int width, int height)
    {
       JComboBox<String> comboBox = new JComboBox<>();
       comboBox.setPreferredSize(new Dimension(width, height));
       comboBox.setEnabled(true);
       return comboBox;
    }   
    
    // Surcharge 
    private JComboBox<String> createJComboBox (int width, int height, String[] element)
    {
       JComboBox<String> comboBox = new JComboBox<>(element);
       comboBox.setPreferredSize(new Dimension(width, height));
       comboBox.setEnabled(true);
       return comboBox;
    }

    private Font styleFont1 = new Font("Arial", Font.BOLD, 18);
    private Font styleFont2 = new Font("Arial", Font.BOLD, 16);
    
    /*********************************************************** 
                              PANEL 1 
    ***********************************************************/

    public void pan1Position()
    {
        /************************** Facture **************************/
        JLabel labFacture = new JLabel("<html><u>Facture</u></html>");
        addComposant(pan1, labFacture, 0, 1, 1);
        labFacture.setFont(styleFont1);
    
        // A1 - Années
        JLabel labYears = new JLabel("Année");
        addComposant(pan1, labYears, 0, 2, 1);
        boxYears = createJComboBox(60, 18, years);
        addComposant(pan1, boxYears, 0, 4, 1);
 
        // A2 - Mois
        JLabel labMonths = new JLabel("Mois");
        addComposant(pan1, labMonths, 1, 2, 1);
        boxMonths = createJComboBox(100, 18, months);
        addComposant(pan1, boxMonths, 1, 4, 1);

        // A3 - Date de paiement
        JLabel labPay = new JLabel("Date de Paiement");
        addComposant(pan1, labPay, 2, 2, 1);
        datePay = new JDateChooser();
        addComposant(pan1, datePay, 2, 4, 1);
        datePay.setPreferredSize(new Dimension(100, 18));
 
        // B1 - Jours travaillés
        JLabel labDays = new JLabel("Jours travaillés");                
        addComposant(pan1, labDays, 0, 6, 1);
        txtDays = createTextField(60, 18);                
        addComposant(pan1, txtDays, 0, 8, 1);

        // B2 - TJM
        JLabel labTJM = new JLabel("TJM");
        addComposant(pan1, labTJM, 1, 6, 1);
        txtTJM = createTextField(60, 18);
        addComposant(pan1, txtTJM, 1, 8, 1);

        // B3 - Calcule TVA (BP)
        btTVA = new JButton("Calculer");
        addComposant(pan1, btTVA, 2, 8, 1);

        // C1 - Résultat TTC
        JLabel labTTC = new JLabel("TTC");
        addComposant(pan1, labTTC, 0, 10, 1);
        txtTTC = createTextField(60, 18);
        addComposant(pan1, txtTTC, 0, 12, 1);

        // C2 - Résultat HT
        JLabel labHT = new JLabel("HT");
        addComposant(pan1, labHT, 1, 10, 1);
        txtHT = createTextField(60, 18);
        addComposant(pan1, txtHT, 1, 12, 1);

        // C3 - Différence TVA
        JLabel labTVA = new JLabel("TVA");
        addComposant(pan1, labTVA, 2, 10, 1);
        txtTVA = createTextField(60, 18);
        addComposant(pan1, txtTVA, 2, 12, 1);
 
        // /************************* URSSAF *************************/
        JLabel labUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        addComposant(pan1, labUrssaf, 0, 14, 1);
        labUrssaf.setFont(styleFont2);

        // D1 - Taxe URSSAF
        JLabel labTaxe = new JLabel("Taxes");
        addComposant(pan1, labTaxe, 0, 16, 1);
        txtTaxe = createTextField(60, 18);
        addComposant(pan1, txtTaxe, 0, 18, 1);

        // D2 - Différence Taxe
        JLabel labBenefit = new JLabel("Bénéfices");
        addComposant(pan1, labBenefit, 1, 16, 1);
        txtBenefit = createTextField(60, 18);
        addComposant(pan1, txtBenefit, 1, 18, 1);

        // /************************** Liens **************************/
        JLabel labLiens = new JLabel("<html><u>Liens</u></html>");
        addComposant(pan1, labLiens, 0, 20, 1);
        labLiens.setFont(styleFont2);

        // Liens vers facture
        JLabel labLienFacture = new JLabel("Facture");
        addComposant(pan1, labLienFacture, 0, 22, 1);
        
        // E1 - Ouvrir facture
        btOpenFacture = new JButton("Ouvrir");
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, btOpenFacture, 1, 22, 1);
    
        // E2 - Parcourir facture
        btSearchFacture = new JButton("Parcourir");
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, btSearchFacture, 2, 22, 1);
        
        // Renseignement du répertoire -> Facture
        btRep1 = new JButton("...");
        btRep1.setPreferredSize(new Dimension(20,18));
        gbc.insets = new Insets(0, 0, 10, 330);
        addComposant(pan1, btRep1, 0, 24, 3);

        // F1 - Barre de recherche Facture (Réperoitre)
        boxRep1 = createJComboBox(300, 18);
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, boxRep1, 0, 24, 3);
        
        // G1 - Barre de recherche Facture (Nom du PDF)   
        boxPDF1 = createJComboBox(300, 18);
        addComposant(pan1, boxPDF1, 0, 26, 3);
  
        // Liens vers déclaration
        JLabel labLienDecla = new JLabel("Déclaration");
        addComposant(pan1, labLienDecla, 0, 28, 1);

        // H1 - Ouvrir déclaration
        btOpenDecla = new JButton("Ouvrir");
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, btOpenDecla, 1, 28, 1);
        
        // H2 - Parcourir déclaration
        btSearchDecla = new JButton("Parcourir");
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, btSearchDecla, 2, 28, 1);
        
        // Renseignement du répertoire -> Déclaration
        btPDF1 = new JButton("...");
        btPDF1.setPreferredSize(new Dimension(20,18));
        gbc.insets = new Insets(0, 0, 10, 330);
        addComposant(pan1, btPDF1, 0, 30, 3);

        // I1 - Barre de recherche Déclaration (Réperoitre)
        boxRep2 = createJComboBox(300, 18);
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, boxRep2, 0, 30, 3);
        
        // J1 - Barre de recherche Déclaration (Nom du PDF)
        boxPDF2 = createJComboBox( 300, 18);
        addComposant(pan1, boxPDF2, 0, 32, 3);

        // /************************ Enregistrer ************************/
        // K1 - Enregistrer
        btSave = new JButton("Enregistrer");
        addComposant(pan1, btSave, 0, 34, 2);

        // K2 - RAZ
        btReset1 = new JButton("RAZ");
        addComposant(pan1, btReset1, 1, 34, 2);
    }

    /*********************************************************** 
                              PANEL 2 
    ***********************************************************/

    public void pan2Position()
    { 
        /************************* Facture **************************/
        // A1 - TTC
        JLabel labcckTTC = new JLabel("TTC");
        gbc.insets = new Insets(0, 0, 10, 10);
        addComposant(pan2, labcckTTC, 0, 4, 1);
        cckTTC.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, cckTTC, 0, 6, 1);

        // A2 - TVA
        JLabel labcckTVA = new JLabel("TVA");
        addComposant(pan2, labcckTVA, 1, 4, 1);
        cckTVA.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, cckTVA, 1, 6, 1);

        // A3 - HT
        JLabel labcckHT = new JLabel("HT");
        addComposant(pan2, labcckHT, 2, 4, 1);
        cckHT.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, cckHT, 2, 6, 1);

        // A4 - URSSAF
        JLabel labcckTaxe = new JLabel("URSSAF");
        addComposant(pan2, labcckTaxe, 3, 4, 1);
        cckTaxe.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, cckTaxe, 3, 6, 1);

        // A5 - Bénéfices
        JLabel labcckBenefit = new JLabel("Bénéfices");
        addComposant(pan2, labcckBenefit, 4, 4, 1);
        cckBenefit.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, cckBenefit, 4, 6, 1);
        
        JLabel labTotalFacture = new JLabel("<html><u>Facture</u></html>");
        gbc.insets = new Insets(0, 20, 10, 10);
        addComposant(pan2, labTotalFacture, 0, 8, 1);
        labTotalFacture.setFont(styleFont1);

        // B1 - Années
        boxYearsTotal = createJComboBox(60, 18, years);
        gbc.insets = new Insets(0, -50, 10, 0);
        addComposant(pan2, boxYearsTotal, 4, 8, 1);

        // C1 - Total TTC
        JLabel labTotalFactureTTC = new JLabel("Total TTC");
        gbc.insets = new Insets(0, 20, 10, 10);
        addComposant(pan2, labTotalFactureTTC, 0, 12, 1);
        txtTotalTTC = createTextField(60, 18);
        addComposant(pan2, txtTotalTTC, 0, 14, 1);

        // C2 - Total HT
        JLabel labTotalFactureHT = new JLabel("Total HT");
        gbc.insets = new Insets(0, -15, 10, 10);
        addComposant(pan2, labTotalFactureHT, 2, 12, 1);
        txtTotalHT = createTextField(60, 18);
        addComposant(pan2, txtTotalHT, 2, 14, 1);

        // C3 - Total TVA
        JLabel labTotalFactureTVA = new JLabel("Total TVA");
        gbc.insets = new Insets(0, -50, 10, 0);
        addComposant(pan2, labTotalFactureTVA, 4, 12, 1);
        txtTotalTVA = createTextField(60, 18);
        addComposant(pan2, txtTotalTVA, 4, 14, 1);
        
        /************************* URSSAF **************************/
        JLabel labTotalUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        gbc.insets = new Insets(0, 20, 10, 10);
        addComposant(pan2, labTotalUrssaf, 0, 16, 1);
        labTotalUrssaf.setFont(styleFont2);

        // D1 - Taxe Total
        JLabel labTotalTaxe = new JLabel("Total Urssaf");
        addComposant(pan2, labTotalTaxe, 0, 18, 1);
        txtTotalTaxe = createTextField(60, 18);
        addComposant(pan2, txtTotalTaxe, 0, 20, 1);
   
        // D2 - Bénéfices Total 
        JLabel labTotalBenefit = new JLabel("Total Bénéfices");
        gbc.insets = new Insets(0, -15, 10, 10);
        addComposant(pan2, labTotalBenefit, 2, 18, 1);
        txtTotalBenefit = createTextField(60, 18);
        addComposant(pan2, txtTotalBenefit, 2, 20, 1);

        // D3 - RAZ
        btReset2 = new JButton("RAZ");
        gbc.insets = new Insets(0, -50, 10, 0);
        addComposant(pan2, btReset2, 4, 20, 1);     
    }

    /*********************************************************** 
                              PANEL 3 
    ***********************************************************/

    public void pan3Position()
    {
        /********************** Déduction ***********************/
        JLabel labDeduction = new JLabel("<html><u>Déduction</u></html>");
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan3, labDeduction, 0, 4, 1);
        labDeduction.setFont(styleFont2);   
        
        // A1 - Déduction
        JLabel labcckTotal = new JLabel("Déduction");
        gbc.insets = new Insets(0, 0, 10, 10);
        addComposant(pan3, labcckTotal, 0, 6, 1);
        cckTotal.setBackground(Color.LIGHT_GRAY);
        addComposant(pan3, cckTotal, 0, 8, 1);

        JLabel labDeductionTTC = new JLabel("Montant TTC");
        addComposant(pan3, labDeductionTTC, 2, 6, 1);

        JLabel labDeductionHT = new JLabel("Montant HT");
        addComposant(pan3, labDeductionHT, 4, 6, 1);
    }
}
                        