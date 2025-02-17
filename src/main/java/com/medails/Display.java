package com.medails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;

import com.toedter.calendar.JDateChooser;

    /*********************************************************** 
                             MISE EN FORME 
    ***********************************************************/

public class Display
{    
    // Déclaration des variables de classe                                       Initilisation
    public static JComboBox<String>     boxYears;              static { boxYears             = new JComboBox<String> ();} 
    public static JComboBox<String>     boxMonths;             static { boxMonths            = new JComboBox<String> ();}
    public static JDateChooser          datePay;               static { datePay              = new JDateChooser      ();}
    public static JTextField            txtDays;               static { txtDays              = new JTextField        ();}
    public static JTextField            txtTJM;                static { txtTJM               = new JTextField        ();}
    public static JTextField            txtTTC;                static { txtTTC               = new JTextField        ();}
    public static JTextField            txtHT;                 static { txtHT                = new JTextField        ();}
    public static JTextField            txtTVA;                static { txtTVA               = new JTextField        ();}
    public static JTextField            txtTaxeUrssaf;         static { txtTaxeUrssaf        = new JTextField        ();}
    public static JTextField            txtTaxe;               static { txtTaxe              = new JTextField        ();}
    public static JComboBox<String>     boxRep1;               static { boxRep1              = new JComboBox<String> ();}     
    public static JComboBox<String>     boxPDF1;               static { boxPDF1              = new JComboBox<String> ();}
    public static JComboBox<String>     boxRep2;               static { boxRep2              = new JComboBox<String> ();}
    public static JComboBox<String>     boxPDF2;               static { boxPDF2              = new JComboBox<String> ();}
    public static JTextField            txtTotalTTC;           static { txtTotalTTC          = new JTextField        ();}
    public static JTextField            txtTotalHT;            static { txtTotalHT           = new JTextField        ();}
    public static JComboBox<String>     boxYearsTotal;         static { boxYearsTotal        = new JComboBox<String> ();}
    public static JTextField            txtTotalTaxeUrssaf;    static { txtTotalTaxeUrssaf   = new JTextField        ();}
    public static JTextField            txtTotalTaxe;          static { txtTotalTaxe         = new JTextField        ();}
    public static JButton               btOpenFacture;         static { btOpenFacture        = new JButton           ();}
    public static JButton               btTVA;                 static { btTVA                = new JButton           ();}
    public static JButton               btSearchFacture;       static { btSearchFacture      = new JButton           ();}
    public static JButton               btRep1;                static { btRep1               = new JButton           ();}
    public static JButton               btOpenDecla;           static { btOpenDecla          = new JButton           ();}
    public static JButton               btSearchDecla;         static { btSearchDecla        = new JButton           ();}
    public static JButton               btPDF1;                static { btPDF1               = new JButton           ();}
    public static JButton               btSave;                static { btSave               = new JButton           ();}
    public static JButton               btReset1;              static { btReset1             = new JButton           ();}
    public static JButton               btReset2;              static { btReset2             = new JButton           ();}
    public static JButton               btValid;               static { btValid              = new JButton           ();}
    public static JFrame fen;
    public static JFrame fenRep1;
    public static JTextField txtRep1;
     
    // Déclaration des variables d'instance
    private JPanel pan1;
    private JPanel pan2;
    private JPanel panRep1;
    private JScrollPane scroll1;
    private JScrollPane scroll2;
    private GridBagConstraints gbc;
    private GridBagConstraints gbcRep1;
    private JTabbedPane tabMain;
    private JTabbedPane tabGraph;
    private Font styleFont1 = new Font("Arial", Font.BOLD, 18);
    private Font styleFont2 = new Font("Arial", Font.BOLD, 16);

    private Graphic graphic;
    
    public Display()
    {
        // Création Fenetre/Panel
        fen = new JFrame();
        pan1 = new JPanel();
        pan2 = new JPanel();

        // Configuration Fenetre/Panel
        fen.setTitle("Gestionnaie de facture");
        fen.setSize(410, 610);
        fen.setLocationRelativeTo(null);
        fen.setResizable(false);
        pan1.setBackground(Color.LIGHT_GRAY);
        pan2.setBackground(Color.LIGHT_GRAY);
        pan1.setLayout(new GridBagLayout());   
        pan2.setLayout(new GridBagLayout());   

        // Ajout du scroll aux panels
        scroll1 = new JScrollPane(pan1);
        scroll2 = new JScrollPane(pan2);
        
        // Configuration des scrolls
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Placement des composants
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 10, 10);

        // Onglets - utiliser les JScrollPane au lieu des JPanel
        tabMain = new JTabbedPane();
        tabMain.add("Enregistrement", scroll1);
        tabMain.add("Graphique", scroll2);
        // Ajout des Onglets dans Fenetre
        fen.add(tabMain, BorderLayout.CENTER);

        pan1Position();
        pan2Position();
        graphic();
    //    rep1Position();

        // Fermeture de la fenetre
        fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fen.setVisible(true); 
    }

    /************************** Factorisation **************************/
        
    private void addComposant (JPanel panel, JComponent component, int gridx, int gridy, int gridwidth)
    {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        panel.add(component, gbc);
    }

    private JTextField creaTextField (int width, int height)
    {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width, height));
        textField.setEnabled(true);
        return textField;
    }

    private JComboBox<String> createJComboBox (int width, int heigt)
    {
       JComboBox<String> comboBox = new JComboBox<>();
       comboBox.setPreferredSize(new Dimension(width, heigt));
       comboBox.setEnabled(true);
       return comboBox;
    }   
    // Surcharge 
    private JComboBox<String> createJComboBox (String[] element, int width, int heigt)
    {
       JComboBox<String> comboBox = new JComboBox<>(element);
       comboBox.setPreferredSize(new Dimension(width, heigt));
       comboBox.setEnabled(true);
       return comboBox;
    }

    /************************** Positionnement **************************/

    public void pan1Position()
    {
        /************************** Facture **************************/
        JLabel labFacture = new JLabel("<html><u>Facture</u></html>");
        addComposant(pan1, labFacture, 0, 1, 1);
        labFacture.setFont(styleFont1);
    
        // A1 - Années
        JLabel labYears = new JLabel("Année");
        addComposant(pan1, labYears, 0, 2, 1);
        boxYears = createJComboBox(Treatment.years, 60, 18);
        addComposant(pan1, boxYears, 0, 4, 1);
 
        // A2 - Mois
        JLabel labMonths = new JLabel("Mois");
        addComposant(pan1, labMonths, 1, 2, 1);
        boxMonths = createJComboBox(Treatment.months, 100, 18);
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
        txtDays = creaTextField(60, 18);                
        addComposant(pan1, txtDays, 0, 8, 1);

        // B2 - TJM
        JLabel labTJM = new JLabel("TJM");
        addComposant(pan1, labTJM, 1, 6, 1);
        txtTJM = creaTextField(60, 18);
        addComposant(pan1, txtTJM, 1, 8, 1);

        // B3 - Calcule TVA
        btTVA = new JButton("Calculer");
        addComposant(pan1, btTVA, 2, 8, 1);

        // C1 - Résultat TTC
        JLabel labTTC = new JLabel("TTC");
        addComposant(pan1, labTTC, 0, 10, 1);
        txtTTC = creaTextField(60, 18);
        addComposant(pan1, txtTTC, 0, 12, 1);

        // C2 - Résultat HT
        JLabel labHT = new JLabel("HT");
        addComposant(pan1, labHT, 1, 10, 1);
        txtHT = creaTextField(60, 18);
        addComposant(pan1, txtHT, 1, 12, 1);

        // C3 - Différence TVA
        JLabel labTVA = new JLabel("TVA");
        addComposant(pan1, labTVA, 2, 10, 1);
        txtTVA = creaTextField(60, 18);
        addComposant(pan1, txtTVA, 2, 12, 1);
 
        // /************************* URSSAF *************************/
        JLabel labUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        addComposant(pan1, labUrssaf, 0, 14, 1);
        labUrssaf.setFont(styleFont2);

        // // D1 - Taxe URSSAF
        JLabel labTaxeUrssaf = new JLabel("Montant taxe");
        addComposant(pan1, labTaxeUrssaf, 0, 16, 1);
        txtTaxeUrssaf = creaTextField(60, 18);
        addComposant(pan1, txtTaxeUrssaf, 0, 18, 1);

        // D2 - Différence Taxe
        JLabel labTaxe = new JLabel("Restant");
        addComposant(pan1, labTaxe, 1, 16,1);
        txtTaxe = creaTextField(60, 18);
        addComposant(pan1, txtTaxe, 1, 18, 1);

        // /************************** Liens **************************/
        JLabel labLiens = new JLabel("<html><u>Liens</u></html>");
        addComposant(pan1, labLiens, 0, 20, 1);
        labLiens.setFont(styleFont2);

        // E1-F1 - Liens vers facture
        JLabel labLienFacture = new JLabel("Facture");
        addComposant(pan1, labLienFacture, 0, 22, 1);
        
        // Ouvrir facture
        btOpenFacture = new JButton("Ouvrir");
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, btOpenFacture, 1, 22, 1);
    
        // Parcourir facture
        btSearchFacture = new JButton("Parcourir");
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, btSearchFacture, 2, 22, 1);
        
        // Renseignement du répertoire -> Facture
        btRep1 = new JButton("...");
        btRep1.setPreferredSize(new Dimension(20,18));
        gbc.insets = new Insets(0, 0, 10, 330);
        addComposant(pan1, btRep1, 0, 24, 3);

        // Barre de recherche Facture (Réperoitre)
        boxRep1 = createJComboBox(300, 18);
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, boxRep1, 0, 24, 3);
        
        // Barre de recherche Facture (Nom du PDF)   
        boxPDF1 = createJComboBox(300, 18);
        addComposant(pan1, boxPDF1, 0, 26, 3);
  
        // G1-H1 - Liens vers déclaration
        JLabel labLienDecla = new JLabel("Déclaration");
        addComposant(pan1, labLienDecla, 0, 28, 1);

        // Ouvrir déclaration
        btOpenDecla = new JButton("Ouvrir");
        gbc.insets = new Insets(0, 65, 10, 10);
        addComposant(pan1, btOpenDecla, 1, 28, 1);
        
        // Parcourir déclaration
        btSearchDecla = new JButton("Parcourir");
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, btSearchDecla, 2, 28, 1);
        
        // Renseignement du répertoire -> Déclaration
        btPDF1 = new JButton("...");
        btPDF1.setPreferredSize(new Dimension(20,18));
        gbc.insets = new Insets(0, 0, 10, 330);
        addComposant(pan1, btPDF1, 0, 30, 3);

        // Barre de recherche Déclaration (Réperoitre)
        boxRep2 = createJComboBox(300, 18);
        gbc.insets = new Insets(0, 10, 10, 10);
        addComposant(pan1, boxRep2, 0, 30, 3);
        
        // Barre de recherche Déclaration (Nom du PDF)
        boxPDF2 = createJComboBox( 300, 18);
        addComposant(pan1, boxPDF2, 0, 32, 3);

        // /************************ Enregistrer ************************/
        // I1 - Enregistrer
        btSave = new JButton("Enregistrer");
        addComposant(pan1, btSave, 0, 34, 2);

        // I2 - RAZ
        btReset1 = new JButton("RAZ");
        addComposant(pan1, btReset1, 1, 34, 2);
    }


    public void pan2Position()
    {
        /************************* Facture **************************/
        JLabel labTotalFacture = new JLabel("<html><u>Facture</u></html>");
        addComposant(pan2, labTotalFacture, 0, 1, 1);
        labTotalFacture.setFont(styleFont1);

        // A2 - Résultat Total TTC
        JLabel labTotalFactureTTC = new JLabel("Total TTC");
        addComposant(pan2, labTotalFactureTTC, 0, 2, 1);

        txtTotalTTC = creaTextField(60, 18);
        addComposant(pan2, txtTotalTTC, 0, 4, 1);

        // A1 - Résultat Total HT
        JLabel labTotalFactureHT = new JLabel("Total HT");
        addComposant(pan2, labTotalFactureHT, 1, 2, 1);

        txtTotalHT = creaTextField(60, 18);
        addComposant(pan2, txtTotalHT, 1, 4, 1);

        // A3 - Années
        JLabel labYearsTotal = new JLabel("Année");
        addComposant(pan2, labYearsTotal, 2, 2, 1);

        boxYearsTotal = createJComboBox(Treatment.years, 60, 18);
        addComposant(pan2, boxYearsTotal, 2, 4, 1);
        
        /************************* URSSAF **************************/
        JLabel labTotalUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        addComposant(pan2, labTotalUrssaf, 0, 6, 1);
        labTotalUrssaf.setFont(styleFont2);

        // B1 - Taxe Total
        JLabel labTotalFactureTaxeUrssaf = new JLabel("Total taxe");
        addComposant(pan2, labTotalFactureTaxeUrssaf, 0, 8, 1);

        txtTotalTaxeUrssaf = creaTextField(60, 18);
        addComposant(pan2, txtTotalTaxeUrssaf, 0, 10, 1);
   
        // B2 - Différence Total 
        JLabel labTotalFactureTaxe = new JLabel("Total différence");
        addComposant(pan2, labTotalFactureTaxe, 1, 8, 1);

        txtTotalTaxe = creaTextField(60, 18);
        addComposant(pan2, txtTotalTaxe, 1, 10, 1);

        // B3 - RAZ
        btReset2 = new JButton("RAZ");
        addComposant(pan2, btReset2, 2, 10, 1);     
    }

    public void graphic()
    {
        ChartPanel chartPanelYears = graphic.getchartPanelYears();
        ChartPanel chartPanelMonths = graphic.getchartPanelMonths();

        chartPanelYears.setPreferredSize(new Dimension(340,400));
        chartPanelMonths.setPreferredSize(new Dimension(340,400));

        tabGraph = new JTabbedPane(); 
        tabGraph.setTabPlacement(JTabbedPane.BOTTOM);
        tabGraph.addTab("Annuel", chartPanelYears);
        tabGraph.addTab("Mensuel", chartPanelMonths);    
        addComposant(pan2, tabGraph, 0, 0, 3);    
    }

    public void rep1Position()
    {
        fenRep1 = new JFrame("Répertoire Facture par défaut");
        fenRep1.setSize(380, 130);
        fenRep1.setLocationRelativeTo(null);
        fenRep1.setResizable(false);

        panRep1 = new JPanel();
        panRep1.setBackground(Color.LIGHT_GRAY);
        fenRep1.setContentPane(panRep1);
        panRep1.setLayout(new GridBagLayout());

        gbcRep1 = new GridBagConstraints();
        gbcRep1.insets = new Insets(0, 10, 10, 10);

        txtRep1 = creaTextField(300, 18);
        addComposant(panRep1, txtRep1, 2, 0, 1);

        btValid = new JButton("Valider");
        addComposant(panRep1, btValid, 1, 1, 1);

        fenRep1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenRep1.setVisible(true); 
    }
}
                        