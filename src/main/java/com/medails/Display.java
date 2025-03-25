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
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.toedter.calendar.JDateChooser;

    /*********************************************************** 
                             MISE EN FORME 
    ***********************************************************/

public class Display
{    
    /*********** Constructeur ***************/
    public Display()
    {   
        fenPosition();
        pan1Position();
        pan2Position();
        pan3Position();
    }

    /************************* Variables d'instance **************************/
    // Principaux utilitaires graphiques
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
    public JComboBox<String>     boxYears;               { boxYears             = new JComboBox<String> ();}                          // A1 - Années
    public JComboBox<String>     boxMonths;              { boxMonths            = new JComboBox<String> ();}                          // A2 - Mois
    public JDateChooser          datePay;                { datePay              = new JDateChooser      ();}                          // A3 - Date de paiement
    public JTextField            txtDays;                { txtDays              = new JTextField        ();}                          // B1 - Jours travaillés
    public JTextField            txtTJM;                 { txtTJM               = new JTextField        ();}                          // B2 - TJM
    public JButton               butTVA;                 { butTVA               = new JButton           ("Calculer");}           // B3 - Calcule TVA (BP)
    public JTextField            txtTTC;                 { txtTTC               = new JTextField        ();}                          // C1 - Résultat TTC
    public JTextField            txtHT;                  { txtHT                = new JTextField        ();}                          // C2 - Résultat HT
    public JTextField            txtTVA;                 { txtTVA               = new JTextField        ();}                          // C3 - Différence TVA  
    public JTextField            txtTaxe;                { txtTaxe              = new JTextField        ();}                          // D1 - Taxe URSSAF
    public JTextField            txtBenefit;             { txtBenefit           = new JTextField        ();}                          // D2 - Différence Taxe
    public JButton               butOpenFacture;         { butOpenFacture       = new JButton           ("Ouvrir");}             // E1 - Ouvrir facture
    public JButton               butSearchFacture;       { butSearchFacture     = new JButton           ("Parcourir");}          // E2 - Parcourir facture
    public JButton               butRep1;                { butRep1              = new JButton           ("...");}                // ... - Renseignement du répertoire -> Facture
    public JComboBox<String>     boxRep1;                { boxRep1              = new JComboBox<String> ();}                          // F1 - Barre de recherche Facture (Réperoitre)
    public JComboBox<String>     boxPDF1;                { boxPDF1              = new JComboBox<String> ();}                          // G1 - Barre de recherche Facture (Nom du PDF) 
    public JButton               butOpenDecla;           { butOpenDecla         = new JButton           ("Ouvrir");}             // H1 - Ouvrir déclaration
    public JButton               butSearchDecla;         { butSearchDecla       = new JButton           ("Parcourir");}          // H2 - Parcourir déclaration
    public JButton               butPDF1;                { butPDF1              = new JButton           ("...");}                // ... -  Renseignement du répertoire -> Déclaration
    public JComboBox<String>     boxRep2;                { boxRep2              = new JComboBox<String> ();}                          // I1 - Barre de recherche Déclaration (Réperoitre)
    public JComboBox<String>     boxPDF2;                { boxPDF2              = new JComboBox<String> ();}                          // J1 - Barre de recherche Déclaration (Nom du PDF)
    public JButton               butSave;                { butSave              = new JButton           ("Enregistrer");}        // K1 - Enregistrer
    public JButton               butReset1;              { butReset1            = new JButton           ("RAZ");}                // K2 - RAZ

    /*********** Onglet 2 ***************/
    public final int MINRANGE_DECADEPAN2     = 30000;     public final int MAXRANGE_DECADEPAN2     = 100000;
    public final int MINRANGE_YEARMONTHPAN2  = 10000;     public final int MAXRANGE_YEARMONTHPAN2  = 20000; 
    public JSlider               sliDecadePan2;          { sliDecadePan2        = new JSlider           (JSlider.HORIZONTAL, MINRANGE_DECADEPAN2, MAXRANGE_DECADEPAN2, MINRANGE_DECADEPAN2);}   
    public JSlider               sliYearMonthPan2;       { sliYearMonthPan2     = new JSlider           (JSlider.HORIZONTAL, MINRANGE_YEARMONTHPAN2, MAXRANGE_YEARMONTHPAN2, MINRANGE_YEARMONTHPAN2);}  
    public JToggleButton         togTotal;               { togTotal             = new JToggleButton     ();}                          // Déduction TVA           
    public JCheckBox             cckTTCPan2;             { cckTTCPan2           = new JCheckBox         ("", true);}    // A1 - TTC
    public JCheckBox             cckTVAPan2;             { cckTVAPan2           = new JCheckBox         ("", true);}    // A2 - TVA
    public JCheckBox             cckHTPan2;              { cckHTPan2            = new JCheckBox         ("", true);}    // A3 - HT
    public JCheckBox             cckTaxePan2  ;          { cckTaxePan2          = new JCheckBox         ("", true);}    // A4 - Urssaf
    public JCheckBox             cckBenefitPan2;         { cckBenefitPan2       = new JCheckBox         ("", true);}    // A5 - Bénéfices
    public JComboBox<String>     boxYearsTotal;          { boxYearsTotal        = new JComboBox<String> ();}                          // B1 - Années
    public JTextField            txtTotalTTC;            { txtTotalTTC          = new JTextField        ();}                          // C1 - Résultat Total TTC
    public JTextField            txtTotalHT;             { txtTotalHT           = new JTextField        ();}                          // C2 - Résultat Total HT
    public JTextField            txtTotalTVA;            { txtTotalTVA          = new JTextField        ();}                          // C3 - TVA Total
    public JTextField            txtTotalTaxe;           { txtTotalTaxe         = new JTextField        ();}                          // D1 - Taxe Total
    public JTextField            txtTotalBenefit;        { txtTotalBenefit      = new JTextField        ();}                          // D2 - Bénéfice Total
    public JButton               butReset2;              { butReset2            = new JButton           ("RAZ");}              // D3 - RAZ

    /*********** Onglet 3 ***************/ 
    public final int MINRANGE_DECADEPAN3     = 10000;     public final int MAXRANGE_DECADEPAN3     = 25000;
    public final int MINRANGE_YEARMONTHPAN3  = 5000;      public final int MAXRANGE_YEARMONTHPAN3  = 10000; 
    public JSlider               sliDecadePan3;          { sliDecadePan3        = new JSlider           (JSlider.HORIZONTAL, MINRANGE_DECADEPAN3, MAXRANGE_DECADEPAN3, MINRANGE_DECADEPAN3);}   
    public JSlider               sliYearMonthPan3;       { sliYearMonthPan3     = new JSlider           (JSlider.HORIZONTAL, MINRANGE_YEARMONTHPAN3, MAXRANGE_YEARMONTHPAN3, MINRANGE_YEARMONTHPAN3);}                    
    public JDateChooser          dateDeduction;          { dateDeduction        = new JDateChooser      ();}                          // A1 - Date de paiement 
    public JComboBox<String>     boxYearsDeduction;      { boxYearsDeduction    = new JComboBox<String> ();}                          // A2 - Déduction
    public JTextField            txtTTCPan3;             { txtTTCPan3           = new JTextField        ();}                          // B1 - Résultat TTC
    public JTextField            txtHTPan3;              { txtHTPan3            = new JTextField        ();}                          // B2 - Résultat HT 
    public JTextField            txtTVAPan3;             { txtTVAPan3           = new JTextField        ();}                          // B3 - Résultat TVA
    public JButton               butOpenDeduction;       { butOpenDeduction     = new JButton           ("Ouvrir");}             // C1 - Ouvrir Déduction
    public JButton               butSearchDeduction;     { butSearchDeduction   = new JButton           ("Parcourir");}          // C2 - Parcourir Déduction
    public JButton               butPDF3;                { butPDF3              = new JButton           ("...");}                // ... -  Renseignement du répertoire -> Déclaration
    public JComboBox<String>     boxRepDeduction;        { boxRepDeduction      = new JComboBox<String> ();}                          // D1 - Barre de recherche Déduction (Réperoitre)
    public JComboBox<String>     boxPDFDeduction;        { boxPDFDeduction      = new JComboBox<String> ();}                          // E1 - Barre de recherche Déduction (Nom du PDF)
    public JButton               butSaveDeduction;       { butSaveDeduction     = new JButton           ("Enregistrer");}        // F1 - Enregistrer
    public JButton               butResetDeduction;      { butResetDeduction    = new JButton           ("RAZ");}                // F2 - RAZ

    // Année et mois
    private String years[] = {"", "2024", "2025", "2026", "2027", "2028"};
    private String months[] = {"", "Janvier", "Février", "Mars", "Avril", 
                                      "Mai", "Juin", "Juillet", "Août", "Septembre", 
                                      "Octobre", "Novembre", "Décembre"};
        
    /************************************************************ 
                              METHODES
    *************************************************************/

    public void fenPosition()
    {
        // Création Fenetre/Panel
        fen = new JFrame();
        pan1 = new JPanel();
        pan2 = new JPanel();
        pan3 = new JPanel();

        // Configuration Fenetre/Panel
        fen.setTitle("Gestionnaie de facture");
        fen.setSize(410, 640);
        fen.setMinimumSize(new Dimension(410, 640));
        fen.setLocationRelativeTo(null);
        fen.setResizable(true);
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
                            POSITIONNEMENT 
    ***********************************************************/

    public void pan1Position()
    {
        /************************** Facture **************************/
        JLabel labFacture = new JLabel("<html><u>Facture</u></html>");
        labFacture.setFont(styleFont1);
        addComposant(pan1, labFacture, 0, 1, 1);
        
        // A1 - Années
        JLabel labYears = new JLabel("Année");
        boxYears = createJComboBox(60, 18, years);
        addComposant(pan1, labYears, 0, 2, 1);
        addComposant(pan1, boxYears, 0, 4, 1);
 
        // A2 - Mois
        JLabel labMonths = new JLabel("Mois");
        boxMonths = createJComboBox(100, 18, months);
        addComposant(pan1, labMonths, 1, 2, 1);
        addComposant(pan1, boxMonths, 1, 4, 1);

        // A3 - Date de paiement
        JLabel labPay = new JLabel("Date de Paiement");
        datePay.setPreferredSize(new Dimension(100, 18));
        addComposant(pan1, labPay, 2, 2, 1);
        addComposant(pan1, datePay, 2, 4, 1);
        
        // B1 - Jours travaillés
        JLabel labDays = new JLabel("Jours travaillés");        
        txtDays = createTextField(60, 18);           
        addComposant(pan1, labDays, 0, 6, 1);         
        addComposant(pan1, txtDays, 0, 8, 1);

        // B2 - TJM
        JLabel labTJM = new JLabel("TJM");
        txtTJM = createTextField(60, 18);
        addComposant(pan1, labTJM, 1, 6, 1);
        addComposant(pan1, txtTJM, 1, 8, 1);

        // B3 - Calcule TVA (BP)
        addComposant(pan1, butTVA, 2, 8, 1);

        // C1 - Résultat TTC
        JLabel labTTC = new JLabel("TTC");
        txtTTC = createTextField(60, 18);
        addComposant(pan1, labTTC, 0, 10, 1);
        addComposant(pan1, txtTTC, 0, 12, 1);

        // C2 - Résultat HT
        JLabel labHT = new JLabel("HT");
        txtHT = createTextField(60, 18);
        addComposant(pan1, labHT, 1, 10, 1);
        addComposant(pan1, txtHT, 1, 12, 1);

        // C3 - Différence TVA
        JLabel labTVA = new JLabel("TVA");
        txtTVA = createTextField(60, 18);
        addComposant(pan1, labTVA, 2, 10, 1);
        addComposant(pan1, txtTVA, 2, 12, 1);
 
        // /************************* URSSAF *************************/
        JLabel labUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        labUrssaf.setFont(styleFont2);
        addComposant(pan1, labUrssaf, 0, 14, 1);
        
        // D1 - Taxe URSSAF
        JLabel labTaxe = new JLabel("Taxes");
        txtTaxe = createTextField(60, 18);
        addComposant(pan1, labTaxe, 0, 16, 1);
        addComposant(pan1, txtTaxe, 0, 18, 1);

        // D2 - Différence Taxe
        JLabel labBenefit = new JLabel("Bénéfices");
        txtBenefit = createTextField(60, 18);
        addComposant(pan1, labBenefit, 1, 16, 1);
        addComposant(pan1, txtBenefit, 1, 18, 1);

        // /************************** Liens **************************/
        JLabel labLiens = new JLabel("<html><u>Liens</u></html>");
        labLiens.setFont(styleFont2);
        addComposant(pan1, labLiens, 0, 20, 1);
        
        // Liens vers facture
        JLabel labLienFacture = new JLabel("Facture");
        addComposant(pan1, labLienFacture, 0, 22, 1);
        
        // E1 - Ouvrir facture
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, butOpenFacture, 1, 22, 1);
    
        // E2 - Parcourir facture
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, butSearchFacture, 2, 22, 1);
        
        // Renseignement du répertoire -> Facture
        gbc.insets = new Insets(0, 0, 10, 330);
        butRep1.setPreferredSize(new Dimension(20,18));
        addComposant(pan1, butRep1, 0, 24, 3);

        // F1 - Barre de recherche Facture (Réperoitre)
        gbc.insets = new Insets(0, 10, 10, 10);
        boxRep1 = createJComboBox(300, 18);
        addComposant(pan1, boxRep1, 0, 24, 3);
        
        // G1 - Barre de recherche Facture (Nom du PDF)   
        boxPDF1 = createJComboBox(300, 18);
        addComposant(pan1, boxPDF1, 0, 26, 3);
  
        // Liens vers déclaration
        JLabel labLienDecla = new JLabel("Déclaration");
        addComposant(pan1, labLienDecla, 0, 28, 1);

        // H1 - Ouvrir déclaration
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, butOpenDecla, 1, 28, 1);
        
        // H2 - Parcourir déclaration
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, butSearchDecla, 2, 28, 1);
        
        // Renseignement du répertoire -> Déclaration
        gbc.insets = new Insets(0, 0, 10, 330);
        butPDF1.setPreferredSize(new Dimension(20,18));
        addComposant(pan1, butPDF1, 0, 30, 3);

        // I1 - Barre de recherche Déclaration (Réperoitre)
        gbc.insets = new Insets(0, 10, 10, 10);
        boxRep2 = createJComboBox(300, 18);
        addComposant(pan1, boxRep2, 0, 30, 3);
        
        // J1 - Barre de recherche Déclaration (Nom du PDF)
        boxPDF2 = createJComboBox( 300, 18);
        addComposant(pan1, boxPDF2, 0, 32, 3);

        // /************************ Enregistrer ************************/
        // K1 - Enregistrer
        addComposant(pan1, butSave, 0, 34, 2);

        // K2 - RAZ
        addComposant(pan1, butReset1, 1, 34, 2);
    }

    /*********************************************************** 
                              PANEL 2 
    ***********************************************************/

    public void pan2Position()
    { 
        /************************* Facture **************************/

        // XX - Slide
        gbc.insets = new Insets(0, 10, 10, 10);
        JLabel labSliDecadePan2 = new JLabel("Décénie : ");
        JLabel labSliYearMonthPan2 = new JLabel("Annuel/Mensuel : ");
        addComposant(pan2, labSliDecadePan2, 0, 0, 1);
        addComposant(pan2, labSliYearMonthPan2, 0, 1, 1);
        addComposant(pan2, sliDecadePan2, 1, 0, 4); 
        addComposant(pan2, sliYearMonthPan2, 1, 1, 4); 

        // Déduction TVA
        gbc.insets = new Insets(400, 10, 0, 30);
        addComposant(pan2, togTotal, 4, 2, 1);  

        // A1 - TTC
        gbc.insets = new Insets(0, -10, 10, 10);
        JLabel labcckTTCPan2 = new JLabel("TTC");
        cckTTCPan2.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, labcckTTCPan2, 0, 4, 1);
        addComposant(pan2, cckTTCPan2, 0, 6, 1);

        // A2 - TVA
        JLabel labcckTVAPan2 = new JLabel("TVA");
        cckTVAPan2.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, labcckTVAPan2, 1, 4, 1);
        addComposant(pan2, cckTVAPan2, 1, 6, 1);

        // A3 - HT
        JLabel labcckHTPan2 = new JLabel("HT");
        cckHTPan2.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, labcckHTPan2, 2, 4, 1);
        addComposant(pan2, cckHTPan2, 2, 6, 1);

        // A4 - URSSAF
        JLabel labcckTaxePan2 = new JLabel("URSSAF");
        cckTaxePan2.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, labcckTaxePan2, 3, 4, 1);
        addComposant(pan2, cckTaxePan2, 3, 6, 1);

        // A5 - Bénéfices
        JLabel labcckBenefitPan2 = new JLabel("Bénéfices");
        cckBenefitPan2.setBackground(Color.LIGHT_GRAY);
        addComposant(pan2, labcckBenefitPan2, 4, 4, 1);
        addComposant(pan2, cckBenefitPan2, 4, 6, 1);
        
        gbc.insets = new Insets(0, 20, 10, 10);
        JLabel labTotalFacture = new JLabel("<html><u>Facture</u></html>");
        labTotalFacture.setFont(styleFont1);
        addComposant(pan2, labTotalFacture, 0, 8, 1);
    
        // B1 - Années
        gbc.insets = new Insets(0, -50, 10, 0);
        boxYearsTotal = createJComboBox(60, 18, years);
        addComposant(pan2, boxYearsTotal, 4, 8, 1);

        // C1 - Total TTC
        gbc.insets = new Insets(0, 20, 10, 10);
        JLabel labTotalFactureTTC = new JLabel("Total TTC");
        txtTotalTTC = createTextField(60, 18);
        addComposant(pan2, labTotalFactureTTC, 0, 12, 1);
        addComposant(pan2, txtTotalTTC, 0, 14, 1);

        // C2 - Total HT
        gbc.insets = new Insets(0, -15, 10, 10);
        JLabel labTotalFactureHT = new JLabel("Total HT");
        txtTotalHT = createTextField(60, 18);
        addComposant(pan2, labTotalFactureHT, 2, 12, 1);
        addComposant(pan2, txtTotalHT, 2, 14, 1);

        // C3 - Total TVA
        gbc.insets = new Insets(0, -50, 10, 0);
        JLabel labTotalFactureTVA = new JLabel("Total TVA");
        txtTotalTVA = createTextField(60, 18);
        addComposant(pan2, labTotalFactureTVA, 4, 12, 1);
        addComposant(pan2, txtTotalTVA, 4, 14, 1);
        
        /************************* URSSAF **************************/
        gbc.insets = new Insets(0, 20, 10, 10);
        JLabel labTotalUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        labTotalUrssaf.setFont(styleFont2);
        addComposant(pan2, labTotalUrssaf, 0, 16, 1);
        
        // D1 - Taxe Total
        JLabel labTotalTaxe = new JLabel("Total Urssaf");
        txtTotalTaxe = createTextField(60, 18);
        addComposant(pan2, labTotalTaxe, 0, 18, 1);
        addComposant(pan2, txtTotalTaxe, 0, 20, 1);
   
        // D2 - Bénéfices Total 
        gbc.insets = new Insets(0, -15, 10, 10);
        JLabel labTotalBenefit = new JLabel("Total Bénéfices");
        txtTotalBenefit = createTextField(60, 18);
        addComposant(pan2, labTotalBenefit, 2, 18, 1);
        addComposant(pan2, txtTotalBenefit, 2, 20, 1);

        // D3 - RAZ
        gbc.insets = new Insets(0, -50, 10, 0);
        addComposant(pan2, butReset2, 4, 20, 1);     
    }

    /*********************************************************** 
                              PANEL 3 
    ***********************************************************/

    public void pan3Position()
    {
        /********************** Déduction ***********************/
    
        // XX - Slide
        gbc.insets = new Insets(0, 10, 10, 10);
        JLabel labSliDecadePan3 = new JLabel("Décénie : ");
        JLabel labSliYearMonthPan3 = new JLabel("Annuel/Mensuel : ");
        addComposant(pan3, labSliDecadePan3, 0, 0, 1);
        addComposant(pan3, labSliYearMonthPan3, 0, 1, 1);
        addComposant(pan3, sliDecadePan3, 1, 0, 3); 
        addComposant(pan3, sliYearMonthPan3, 1, 1, 3); 

        // A1 - Date de paiement
        JLabel labDateDeduction = new JLabel("Date d'achat");
        dateDeduction.setPreferredSize(new Dimension(100, 18));
        addComposant(pan3, labDateDeduction, 0, 6, 1);
        addComposant(pan3, dateDeduction, 0, 8, 1);

        // A2 - Années
        JLabel labYearsDeduction = new JLabel("Année");
        boxYearsDeduction = createJComboBox(60, 18, years);
        addComposant(pan3, labYearsDeduction, 2, 6, 1);
        addComposant(pan3, boxYearsDeduction, 2, 8, 1);    
     
        // B1 - TTC
        JLabel labTTCPan3 = new JLabel("TTC");
        txtTTCPan3 = createTextField(60, 18); 
        addComposant(pan3, labTTCPan3, 0, 10, 1);
        addComposant(pan3, txtTTCPan3, 0, 12, 1);

        // B2 - HT
        JLabel labHTPan3 = new JLabel("HT");
        txtHTPan3 = createTextField(60, 18); 
        addComposant(pan3, labHTPan3, 1, 10, 1);
        addComposant(pan3, txtHTPan3, 1, 12, 1);

        // B3 - TVA
        JLabel labTVAPan3 = new JLabel("TVA");
        txtTVAPan3 = createTextField(60, 18); 
        addComposant(pan3, labTVAPan3, 2, 10, 1);
        addComposant(pan3, txtTVAPan3, 2, 12, 1);

        JLabel labDeduction = new JLabel("<html><u>Déduction</u></html>");
        labDeduction.setFont(styleFont2);
        addComposant(pan3, labDeduction, 0, 14, 1);

        // C1 - Ouvrir Déduction
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan3, butOpenDeduction, 1, 16, 1);

        // C2 - Parcourir Déduction
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan3, butSearchDeduction, 2, 16, 1);

        // Renseignement du répertoire -> Déduction
        gbc.insets = new Insets(0, 0, 10, 330);
        butPDF3.setPreferredSize(new Dimension(20,18));
        addComposant(pan3, butPDF3, 0, 18, 3);

        // D1 - Barre de recherche Déduction (Réperoitre)
        gbc.insets = new Insets(0, 10, 10, 10);
        boxRepDeduction = createJComboBox(300, 18);
        addComposant(pan3, boxRepDeduction, 0, 18, 3);

        // E1 - Barre de recherche Déduction (Nom du PDF)
        boxPDFDeduction = createJComboBox(300, 18);
        addComposant(pan3, boxPDFDeduction, 0, 20, 3);
    
        // /************************ Enregistrer ************************/
        // F1 - Enregistrer
        addComposant(pan3, butSaveDeduction, 0, 22, 2);

        // F2 - RAZ
        addComposant(pan3, butResetDeduction, 1, 22, 2);
    }
}
                        
