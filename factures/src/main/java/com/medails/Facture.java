import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale.Category;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.DecimalFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;


public class Facture
{
    // Vérificateur de chiffre dans JTextField
    public static boolean isValidDouble(String text)
    {   
        try 
        {   
            Double.parseDouble(text.trim());    return true; 
        }
        catch (NumberFormatException e) 
        {   
            return false; 
        } 
    }

    // Variable de lecture du .txt
    public static String line;
    public static DefaultCategoryDataset _defaultDataset = new DefaultCategoryDataset();

    public static void main (String[]args)
    {

        /************************************************************ 
                                 MISE EN FORME 
         ***********************************************************/
        
        // Création Fenetre/Panel
        JFrame _fen = new JFrame();
        JPanel _pan1 = new JPanel();
        JPanel _pan2 = new JPanel();

        // Configuration Fenetre/Panel
        _fen.setTitle("Gestionnaie de facture");
        _fen.setSize(410, 610);
        _fen.setLocationRelativeTo(null);
        _fen.setResizable(false);
        _pan1.setBackground(Color.LIGHT_GRAY);
        _pan2.setBackground(Color.LIGHT_GRAY);
        _pan1.setLayout(new GridBagLayout());     
        _pan2.setLayout(new GridBagLayout());

        // Ajout du scroll aux panels
        JScrollPane _scroll1 = new JScrollPane(_pan1);
        JScrollPane _scroll2 = new JScrollPane(_pan2);
        
        // Configuration des scrolls
        _scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        _scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Placement des composants
        GridBagConstraints _gbc = new GridBagConstraints();
        _gbc.insets = new Insets(0, 10, 10, 10);

        // Onglets - utiliser les JScrollPane au lieu des JPanel
        JTabbedPane _onglet = new JTabbedPane();
        _onglet.add("Mensuel", _scroll1);
        _onglet.add("Annuel", _scroll2);

        // Ajout des Onglets dans Fenetre
        _fen.add(_onglet, BorderLayout.CENTER);

        // Année et mois
        String _years[] = {"", "2024", "2025", "2026", "2027", "2028"};
        String _months[] = {"", "Janvier", "Février", "Mars", "Avril", 
                            "Mai", "Juin", "Juillet", "Août", "Septembre", 
                            "Octobre", "Novembre", "Décembre"};

        /************************************************************ 
                           Onglet : Mensuel 
        *************************************************************/

        /************************** Impôts **************************/

        // Impôts
        JLabel _labImpots = new JLabel("<html><u>Impôts</u></html>");
        Font _fontImpots = new Font("Arial", Font.BOLD, 18);
        _labImpots.setFont(_fontImpots);
        _gbc.gridwidth = 1;
        _gbc.gridx = 0;
        _gbc.gridy = 0;       
        _pan1.add(_labImpots, _gbc);

        // A1 - Années
        JLabel _labYears = new JLabel("Année");
        _gbc.gridx = 0;
        _gbc.gridy = 1;       
        _pan1.add(_labYears, _gbc);
        JComboBox<String> _boxYears = new JComboBox<>(_years);
        _boxYears.setPreferredSize(new Dimension(60, 18));
        _boxYears.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 2;
        _pan1.add(_boxYears, _gbc);

        // A2 - Mois
        JLabel _labMonths = new JLabel("Mois");
        _gbc.gridx = 1;
        _gbc.gridy = 1;   
        _pan1.add(_labMonths, _gbc);
        JComboBox<String> _boxMonths = new JComboBox<>(_months);
        _boxMonths.setPreferredSize(new Dimension(100, 18));
        _boxMonths.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 2;
        _pan1.add(_boxMonths, _gbc); 

        // A3 - RAZ
        JButton _btReset1 = new JButton("RAZ");
        _gbc.gridx = 2;
        _gbc.gridy = 2;
        _pan1.add(_btReset1, _gbc);

        // B1 - Jours travaillés
        JLabel _labDays = new JLabel("Jours travaillés");
        _gbc.gridx = 0;
        _gbc.gridy = 3;   
        _pan1.add(_labDays, _gbc);
        JTextField _txtDays = new JTextField();
        _txtDays.setPreferredSize(new Dimension(60, 18));
        _gbc.gridx = 0;
        _gbc.gridy = 4;
        _pan1.add(_txtDays, _gbc); 

        // B2 - TJM
        JLabel _labTJM = new JLabel("TJM");
        _gbc.gridx = 1;
        _gbc.gridy = 3;   
        _pan1.add(_labTJM, _gbc);
        JTextField _txtTJM = new JTextField();
        _txtTJM.setPreferredSize(new Dimension(60, 18));
        _gbc.gridx = 1;
        _gbc.gridy = 4;
        _pan1.add(_txtTJM, _gbc);

        // B3 - Calcule TVA
        JButton _btTVA = new JButton("Calculer");
        _gbc.gridx = 2;
        _gbc.gridy = 4;
        _pan1.add(_btTVA, _gbc);

        // C1 - Résultat HT
        JLabel _labHT = new JLabel("HT");
        _gbc.gridx = 0;
        _gbc.gridy = 5;   
        _pan1.add(_labHT, _gbc);
        JTextField _txtHT = new JTextField();
        _txtHT.setPreferredSize(new Dimension(60, 18));
        _txtHT.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 6;
        _pan1.add(_txtHT, _gbc);

        // C2 - Résultat TTC
        JLabel _labTTC = new JLabel("TTC");
        _gbc.gridx = 1;
        _gbc.gridy = 5;   
        _pan1.add(_labTTC, _gbc);
        JTextField _txtTTC = new JTextField();
        _txtTTC.setPreferredSize(new Dimension(60, 18));
        _txtTTC.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 6;
        _pan1.add(_txtTTC, _gbc); 

        // C3 - Différence TVA
        JLabel _labTVA = new JLabel("TVA");
        _gbc.gridx = 2;
        _gbc.gridy = 5;   
        _pan1.add(_labTVA, _gbc);
        JTextField _txtTVA = new JTextField();
        _txtTVA.setPreferredSize(new Dimension(60, 18));
        _txtTVA.setEnabled(true);
        _gbc.gridx = 2;
        _gbc.gridy = 6;
        _pan1.add(_txtTVA, _gbc);

        /************************* URSSAF *************************/

        // URSSAF
        JLabel _labUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        Font _fontUrssaf = new Font("Arial", Font.BOLD, 16);
        _labUrssaf.setFont(_fontUrssaf);
        _gbc.gridx = 0;
        _gbc.gridy = 8;       
        _pan1.add(_labUrssaf, _gbc);

        // D1 - Taxe URSSAF
        JLabel _labTaxeUrssaf = new JLabel("Montant taxe");
        _gbc.gridx = 0;
        _gbc.gridy = 9;   
        _pan1.add(_labTaxeUrssaf, _gbc);
        JTextField _txtTaxeUrssaf = new JTextField();
        _txtTaxeUrssaf.setPreferredSize(new Dimension(60, 18));
        _gbc.gridx = 0;
        _gbc.gridy = 10;
        _pan1.add(_txtTaxeUrssaf, _gbc);

        // D2 - Différence Taxe
        JLabel _labTaxe = new JLabel("Restant");
        _gbc.gridx = 1;
        _gbc.gridy = 9;   
        _pan1.add(_labTaxe, _gbc);
        JTextField _txtTaxe = new JTextField();
        _txtTaxe.setPreferredSize(new Dimension(60, 18));
        _txtTaxe.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 10;
        _pan1.add(_txtTaxe, _gbc);

        /************************** Liens **************************/

        // Liens
        JLabel _labLiens = new JLabel("<html><u>Liens</u></html>");
        Font _fontLiens = new Font("Arial", Font.BOLD, 16);
        _labLiens.setFont(_fontLiens);
        _gbc.gridx = 0;
        _gbc.gridy = 18;       
        _pan1.add(_labLiens, _gbc);

        // E1-F1 - Liens vers facture
        JLabel _labLienFacture = new JLabel("Facture");
        _gbc.gridx = 0;
        _gbc.gridy = 19;  
        _gbc.gridwidth = 1; 
        _pan1.add(_labLienFacture, _gbc);
        // Ouvrir facture
        JButton _btOpenFacture = new JButton("Ouvrir");
        _gbc.insets = new Insets(0, 65, 10, 10);
        _gbc.gridx = 1;
        _gbc.gridy = 19;
        _gbc.gridwidth = 1;
        _pan1.add(_btOpenFacture, _gbc);
        // Parcourir facture
        JButton _btSearchFacture = new JButton("Parcourir");
        _gbc.insets = new Insets(0, 10, 10, 10);
        _gbc.gridx = 2;
        _gbc.gridy = 19;
        _gbc.gridwidth = 1; 
        _pan1.add(_btSearchFacture, _gbc);
        // Barre de recherche Facture (Réperoitre)
        JComboBox<String> _boxRep1 = new JComboBox<>();
        _boxRep1.setPreferredSize(new Dimension(300, 18));
        _boxRep1.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 20;
        _gbc.gridwidth = 3;
        _pan1.add(_boxRep1, _gbc);   
        // Barre de recherche Facture (Nom du PDF)   
        JComboBox<String> _boxPDF1 = new JComboBox<>();
        _boxPDF1.setPreferredSize(new Dimension(300, 18));
        _boxPDF1.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 21;
        _gbc.gridwidth = 3;
        _pan1.add(_boxPDF1, _gbc);    

        // G1-H1 - Liens vers déclaration
        JLabel _labLienDecla = new JLabel("Déclaration");
        _gbc.gridx = 0;
        _gbc.gridy = 22;  
        _gbc.gridwidth = 1;
        _pan1.add(_labLienDecla, _gbc);
        // Ouvrir déclaration
        JButton _btOpenDecla = new JButton("Ouvrir");
        _gbc.insets = new Insets(0, 65, 10, 10);
        _gbc.gridx = 1;
        _gbc.gridy = 22;
        _gbc.gridwidth = 1;
        _pan1.add(_btOpenDecla, _gbc);
        // Parcourir déclaration
        JButton _btSearchDecla = new JButton("Parcourir");
        _gbc.insets = new Insets(0, 10, 10, 10);
        _gbc.gridx = 2;
        _gbc.gridy = 22;
        _gbc.gridwidth = 1; 
        _pan1.add(_btSearchDecla, _gbc);
        // Barre de recherche Déclaration (Réperoitre)
        JComboBox<String> _boxRep2 = new JComboBox<>();
        _boxRep2.setPreferredSize(new Dimension(300, 18));
        _boxRep2.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 23;
        _gbc.gridwidth = 3; 
        _pan1.add(_boxRep2, _gbc);     
        // Barre de recherche Déclaration (Nom du PDF)
        JComboBox<String> _boxPDF2 = new JComboBox<>();
        _boxPDF2.setPreferredSize(new Dimension(300, 18));
        _boxPDF2.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 24;
        _gbc.gridwidth = 3;
        _pan1.add(_boxPDF2, _gbc); 

        /************************ Enregistrer ************************/

        // I1 - Enregistrer
        JButton _btSave = new JButton("Enregistrer");
        _gbc.gridx = 1;
        _gbc.gridy = 25;
        _gbc.gridwidth = 1;
        _pan1.add(_btSave, _gbc);

        /************************************************************ 
                           Onglet : Annuel 
        *************************************************************/

        /************************* Impôts **************************/

        JLabel _labTotalImpots = new JLabel("<html><u>Impôts</u></html>");
        Font _fontTotal = new Font("Arial", Font.BOLD, 18);
        _labTotalImpots.setFont(_fontTotal);
        _gbc.gridwidth = 1;
        _gbc.gridx = 0;
        _gbc.gridy = 1;       
        _pan2.add(_labTotalImpots, _gbc);

        // A1 - Résultat Total HT
        JLabel _labTotalImpotsHT = new JLabel("Total HT");
        _gbc.gridx = 0;
        _gbc.gridy = 2;   
        _pan2.add(_labTotalImpotsHT, _gbc);
        JTextField _txtTotalHT = new JTextField();
        _txtTotalHT.setPreferredSize(new Dimension(60, 18));
        _txtTotalHT.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 3;
        _pan2.add(_txtTotalHT, _gbc);

        // A2 - Résultat Total TTC
        JLabel _labTotalImpotsTTC = new JLabel("Total TTC");
        _gbc.gridx = 1;
        _gbc.gridy = 2;   
        _pan2.add(_labTotalImpotsTTC, _gbc);
        JTextField _txtTotalTTC = new JTextField();
        _txtTotalTTC.setPreferredSize(new Dimension(60, 18));
        _txtTotalTTC.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 3;
        _pan2.add(_txtTotalTTC, _gbc); 

        // A3 - Années
        JLabel _labYearsTotal = new JLabel("Année");
        _gbc.gridx = 2;
        _gbc.gridy = 2;       
        _pan2.add(_labYearsTotal, _gbc);
        JComboBox<String> _boxYearsTotal = new JComboBox<>(_years);
        _boxYearsTotal.setPreferredSize(new Dimension(60, 18));
        _boxYearsTotal.setEnabled(true);
        _gbc.gridx = 2;
        _gbc.gridy = 3;
        _pan2.add(_boxYearsTotal, _gbc);
        
        /************************* URSSAF **************************/

        JLabel _labTotalUrssaf = new JLabel("<html><u>URSSAF</u></html>");
        _labTotalUrssaf.setFont(_fontUrssaf);
        _gbc.gridx = 0;
        _gbc.gridy = 4;       
        _pan2.add(_labTotalUrssaf, _gbc);

        // B1 - Taxe Total
        JLabel _labTotalImpotsTaxeUrssaf = new JLabel("Total taxe");
        _gbc.gridx = 0;
        _gbc.gridy = 5;   
        _pan2.add(_labTotalImpotsTaxeUrssaf, _gbc);
        JTextField _txtTotalTaxeUrssaf = new JTextField();
        _txtTotalTaxeUrssaf.setPreferredSize(new Dimension(60, 18));
        _txtTotalTaxeUrssaf.setEnabled(true);       
        _gbc.gridx = 0;
        _gbc.gridy = 6;
        _pan2.add(_txtTotalTaxeUrssaf, _gbc);

        // B2 - Différence Total 
        JLabel _labTotalImpotsTaxe = new JLabel("Total différence");
        _gbc.gridx = 1;
        _gbc.gridy = 5;   
        _pan2.add(_labTotalImpotsTaxe, _gbc);
        JTextField _txtTotalTaxe = new JTextField();
        _txtTotalTaxe.setPreferredSize(new Dimension(60, 18));
        _txtTotalTaxe.setEnabled(true); 
        _gbc.gridx = 1;
        _gbc.gridy = 6;
        _pan2.add(_txtTotalTaxe, _gbc);

        // B3 - RAZ
        JButton _btReset2 = new JButton("RAZ");
        _gbc.gridx = 2;
        _gbc.gridy = 6;
        _pan2.add(_btReset2, _gbc);

        /************************************************************ 
                                 Programme
         ***********************************************************/

        /************************************************************ 
                              Onglet : Mensuel 
        *************************************************************/

        // Vérification cellule non-vide : Jours travaillés
        _txtDays.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                if(! _txtDays.getText().isEmpty() && !isValidDouble(_txtDays.getText()))
                {
                    JOptionPane.showMessageDialog(_fen, "Veuillez entrer un nombre valide" ,
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        _txtDays.setText("");
                }
            }
        });

        // Vérification cellule non-vide : TJM
        _txtTJM.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                if(! _txtTJM.getText().isEmpty() && !isValidDouble(_txtTJM.getText()))
                {
                    JOptionPane.showMessageDialog(_fen, "Veuillez entrer un nombre valide" ,
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        _txtTJM.setText("");
                }
            }
        });

        // Facture -> Parcourir 
        _btSearchFacture.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Instruction pour parcourir les fichiers
                JFileChooser _fileChooser = new JFileChooser();
                String _directory = "M://Multimédia/Bureau/Social/Social - Pc Bureau/01 - Professionnelle/Factures";
                _fileChooser.setCurrentDirectory(new File(_directory));
                _fileChooser.setAcceptAllFileFilterUsed(false);
                _fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                int _result = _fileChooser.showOpenDialog(_fen);
                if(_result == JFileChooser.APPROVE_OPTION)
                {
                    // Report du répertoire dans champ de saisie (Répertoire)
                    File _selectedRep = _fileChooser.getSelectedFile();
                    String _parentDirectory = _selectedRep.getParent();
                    _boxRep1.removeAllItems();
                    _boxRep1.addItem(_parentDirectory);
                    // Report du répertoire dans champ de saisie (Nom du PDF)
                    File _selectedPDF = _fileChooser.getSelectedFile();
                    String _namePDF = _selectedPDF.getName();
                    _boxPDF1.removeAllItems();
                    _boxPDF1.addItem(_namePDF);
                }
            }   
        });

        // Déclaration -> Parcourir 
        _btSearchDecla.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Instruction pour parcourir les fichiers
                JFileChooser _fileChooser = new JFileChooser();
                String _directory = "M://Multimédia/Bureau/Social/Social - Pc Bureau/00 - Gouvernement/URSSAF/Déclarations";
                _fileChooser.setCurrentDirectory(new File(_directory));
                _fileChooser.setAcceptAllFileFilterUsed(false);
               _fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                int _result = _fileChooser.showOpenDialog(_fen);
                if(_result == JFileChooser.APPROVE_OPTION)
                {
                    // Report du répertoire dans champ de saisie (Répertoire)
                    File _selectedRep = _fileChooser.getSelectedFile();
                    String _parentDirectory = _selectedRep.getParent();
                    _boxRep2.removeAllItems();
                    _boxRep2.addItem(_parentDirectory);
                    // Report du répertoire dans champ de saisie (Nom du PDF)
                    File _selectedPDF = _fileChooser.getSelectedFile();
                    String _namePDF = _selectedPDF.getName();
                    _boxPDF2.removeAllItems();
                    _boxPDF2.addItem(_namePDF);
                }
            }   
        });
        
        // Onglet Mensuel -> RAZ
        _btReset1.addActionListener(new ActionListener()   
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    /* A1 */ _boxYears.setSelectedItem("");
                    /* A2 */ _boxMonths.setSelectedItem("");
                    /* B1 */ _txtDays.setText("");  
                    /* B2 */ _txtTJM.setText("");
                    /* C2 */ _txtHT.setText("");
                    /* C2 */ _txtTTC.setText("");
                    /* C3 */ _txtTVA.setText("");
                    /* D1 */ _txtTaxeUrssaf.setText("");
                    /* D2 */ _txtTaxe.setText("");
                    /* E1 */ _boxRep1.removeAllItems();
                    /* F1 */ _boxPDF1.removeAllItems();
                    /* G1 */ _boxRep2.removeAllItems();
                    /* H1 */ _boxPDF2.removeAllItems(); 
            }
        });

        // Onglet Mensuel -> Calculer
        _btTVA.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent evt)
          {
            //____ Init varibles ___\
            double _TTC1 = 0.0;
            double _HT1 = 0.0;
            double _TVA1 = 0.0;
            double _TaxeUrssaf1 = 0.0;
            double _Taxe1 = 0.0;
        
            try
            {       
                String _years = (String) _boxYears.getSelectedItem();
                String _months = (String) _boxMonths.getSelectedItem();

                // Vérification cellule non-vide + Calcule : Année + Mois
                if ((_years == "") || (_months == "")) 
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer une date (année + mois)",
                                            "Demande d'informations", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                double _days1 = Double.parseDouble(_txtDays.getText());
                double _TJM1 = Double.parseDouble(_txtTJM.getText()); 
                _TTC1 = _days1 * _TJM1;
                _HT1 = _TTC1 * 1.2;
                _TVA1 = _HT1 - _TTC1;

                //____ Calcule Impôts (HT + TTC) ___\
                if (! _txtDays.getText().isEmpty() && ! _txtTJM.getText().isEmpty())
                {
                    String _HT1String = Double.toString(_HT1);
                    _txtHT.setText(_HT1String);                   
                    String _TTC1String = Double.toString(_TTC1);
                    _txtTTC.setText(_TTC1String);
                    String _TVA1String = Double.toString(_TVA1);
                    _txtTVA.setText(_TVA1String);
                }

                //____ Calcule URSSAF (Taxe) ___\
                // Année 2024 (ACRE)
                if
                (_years == "2024")
                {
                    _TaxeUrssaf1 = _TTC1 * ((2.2 + 11.6 + 0.2) / 100);
                }
                // Année 2025 (ACRE)
                else if 
                ((_years == "2025") && 
                 (_months == "Janvier" ||
                  _months == "Février"||
                  _months == "Mars" ||
                  _months == "Avril")) 
                {
                    _TaxeUrssaf1 = _TTC1 * ((2.2 + 12.3 + 0.2) / 100);
                }
                // Année 2025 (sans ACRE)
                else
                {
                    _TaxeUrssaf1 = _TTC1 * ((2.2 + 24.6 + 0.2) / 100);
                }
                // Résultat de calcul
                String _decimal = String.format("%.1f", _TaxeUrssaf1);
                _txtTaxeUrssaf.setText(_decimal);

                //____ Calcul URSSAF (Différence) ___\
                _Taxe1 = _TTC1 - _TaxeUrssaf1;
                String _Taxe1String = Double.toString(_Taxe1);
                _txtTaxe.setText(_Taxe1String);
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Veuillez entrez des nombres valides",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            }
          }  
        });

        // Onglet Mensuel -> Enrengistrer
         _btSave.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _filePath = System.getProperty("user.dir") + File.separator +("Facture.txt");
                File _file = new File(_filePath);

                /* A1 */  String _slctYears = (String)           "Année --> " + _boxYears.getSelectedItem();
                /* A2 */  String _slctMonths = (String)          "Mois --> " + _boxMonths.getSelectedItem();
                /* B1 */  String _slctDays = (String)            "Jours --> " + _txtDays.getText();
                /* B2 */  String _slctTJM = (String)             "TJM --> " + _txtTJM.getText();
                /* C1 */  String _slctHT = (String)              "HT --> " + _txtHT.getText();
                /* C2 */  String _slctTTC = (String)             "TTC --> " + _txtTTC.getText();
                /* C3 */  String _slctTVA = (String)             "TVA --> " + _txtTVA.getText();
                /* D1 */  String _slctTaxeUrssaf = (String)      "Urssaf --> " + _txtTaxeUrssaf.getText();
                /* D2 */  String _slctTaxe = (String)            "Restant --> " + _txtTaxe.getText(); 
                /* G1 */  String _slctFactureRep = (String)      _boxRep1.getSelectedItem();
                /* H1 */  String _slctFacturePDF = (String)      _boxPDF1.getSelectedItem();
                /* I1 */  String _slctDeclaraRep = (String)      _boxRep2.getSelectedItem();
                /* J1 */  String _slctDeclaraPDF = (String)      _boxPDF2.getSelectedItem();
                String _seperator = "<->";

                // Vérification cellule non-vide : Facture + Déclaration
                if ((_boxRep1.getSelectedItem()) == null || (_boxPDF1.getSelectedItem() == null) ||
                    (_boxRep2.getSelectedItem()) == null || (_boxPDF2.getSelectedItem() == null))
                {
                    JOptionPane.showMessageDialog(null, "Veuillez compléter les champs avant de continuer",
                                                  "Champs vides", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                else
                {
                    try
                    {
                        // Vérification de l'existence du fichier
                        if (!_file.exists())
                        {
                            _file.createNewFile();
                        }

                        // Vérifier si la ligne existe déjà dans le fichier
                        List<String> _lines = Files.readAllLines(Paths.get(_filePath));

                        // Si la ligne n'existe pas déjà, on l'ajoute
                        if (! _lines.contains(_slctFacturePDF))
                        {
                            // Ajoute la ligne dans le fichier
                            Files.write(Paths.get(_filePath),
                            ( /* A1 */ _slctYears + System.lineSeparator() +
                              /* A2 */ _slctMonths + System.lineSeparator() +
                              /* B1 */ _slctDays + System.lineSeparator() +
                              /* B2 */ _slctTJM + System.lineSeparator() +
                              /* C2 */ _slctHT + System.lineSeparator() +
                              /* C2 */ _slctTTC + System.lineSeparator() +
                              /* C3 */ _slctTVA + System.lineSeparator() +
                              /* D1 */ _slctTaxeUrssaf + System.lineSeparator() +
                              /* D2 */ _slctTaxe + System.lineSeparator() +
                              /* E1 */ _slctFactureRep + System.lineSeparator() + 
                              /* F1 */ _slctFacturePDF + System.lineSeparator() +
                              /* G1 */ _slctDeclaraRep + System.lineSeparator() +
                              /* H1 */ _slctDeclaraPDF + System.lineSeparator() +
                                       _seperator + System.lineSeparator()).
                              getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                            JOptionPane.showMessageDialog(null, "Le programme a bien été enregistré",
                                                        "Enregistrement", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Un fichier avec le même PDF existe déjà",
                            "Doublon", JOptionPane.INFORMATION_MESSAGE);                           
                        }
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                
                }
            }
        });
    
        /************************************************************ 
                           Onglet : Annuel 
        *************************************************************/
        
        // Onglet Annuel -> Année (Popup)
        _boxYearsTotal.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Vérification si année de sélection vide
                String selectedYear = (String) _boxYearsTotal.getSelectedItem();
                if (selectedYear.isEmpty()) 
                {
                    /* A1 */ _txtTotalHT.setText("");  
                    /* A2 */ _txtTotalTTC.setText("");
                    /* A3 */ _boxYearsTotal.setSelectedItem("");
                    /* B1 */ _txtTotalTaxeUrssaf.setText("");
                    /* B2 */ _txtTotalTaxe.setText("");           
                    return;
                }

                boolean _currentYear = false;
                String _currentMonth = null;
                Double _currentHT = null;
                boolean _acre2024 = false;
                boolean _acre2025 = false;
                Double _totalHT = 0.0;
                Double _ii = 0.0;
                Double _jj = 0.0;
                Double[] _graphTotauxHT = new Double[12];
                String[] _graphMonths = {"Janvier", "Février", "Mars", "Avril", 
                                         "Mai", "Juin", "Juillet", "Août", "Septembre", 
                                         "Octobre", "Novembre", "Décembre"};

                /************************************************************ 
                                        LECTURE .TXT
                *************************************************************/

                try (BufferedReader reader = new BufferedReader(new FileReader("Facture.txt"))) 
                {
                    while ((line = reader.readLine()) != null) 
                    {
                        // Ligne vide, on arrête
                        if (line.trim().isEmpty()) 
                        {
                            break;
                        }
                        
                        /************************************************************ 
                                                 CALCULES
                        *************************************************************/

                        // Détection début de l'année sélectionnée
                        if (line.contains("Année --> " + selectedYear)) 
                        {
                            _currentYear = true;  
                            // Année 2024 (ACRE)
                            if (line.contains("Année --> 2024")) 
                            {
                                _acre2024 = true;
                                _acre2025 = false;
                                continue;
                            }
                            // Année 2025 (ACRE)
                            else if (line.contains("Année --> 2025"))
                            {
                                _acre2024 = false;
                                _acre2025 = true;
                                continue;
                            }
                             // Année sans ACRE
                            else
                            {
                                _acre2024 = false;
                                _acre2025 = false;
                                continue;
                            }                                          
                        }
                        
                        // Vérification 2025 (ACRE) 
                        if ( _acre2025)
                            {
                                if ((line.contains("Mois --> Janvier") ||
                                     line.contains("Mois --> Février") ||
                                     line.contains("Mois --> Mars") ||
                                     line.contains("Mois --> Avril"))) 
                                     {
                                        _ii++;
                                     }
                                if ((line.contains("Mois --> Mai") ||
                                     line.contains("Mois --> Juin") ||
                                     line.contains("Mois --> Juillet") ||
                                     line.contains("Mois --> Août") || 
                                     line.contains("Mois --> Septembre") || 
                                     line.contains("Mois --> Octobre") || 
                                     line.contains("Mois --> Novembre") || 
                                     line.contains("Mois --> Décembre"))) 
                                    {
                                        _jj++;
                                    }
                            }
  
                        // Chercher le mois
                        if (line.contains("Mois --> ")) 
                        {   
                            _currentMonth = line.substring(line.indexOf("Mois --> ") + 9).trim();
                        }

                        // Détection d'une autre année, on arrête si on était dans notre année
                        if (line.contains("Année --> ") && _currentYear) 
                        {
                            break;
                        }

                        /************************* CALCULES **************************/  

                        // Si on est dans la bonne année, on additionne les HT
                        if (_currentYear && line.contains("HT --> ")) 
                        {
                            //____ Calcule Impôts (HT + TTC) ___\
                            String _convTotalHT = line.substring(line.indexOf("HT --> ") + 7).trim();
                            _totalHT += Double.parseDouble(_convTotalHT); 
                            // Caclul TVA
                            double _totalTTC = _totalHT / 1.2;
                            String convTotalTTC = Double.toString(_totalTTC);
                            _txtTotalTTC.setText(convTotalTTC);

                                //____ Calcule Total URSSAF (Taxe) ___\
                                double _totalUrssaf = 0.0;
                                double _totalTaxe = 0.0;
                                double _totalii = 0.0;
                                double _totaljj = 0.0;

                                // Année 2024 (ACRE)
                                if (_acre2024) 
                                {
                                    _totalUrssaf = _totalTTC * ((2.2 + 11.6 + 0.2) / 100);
                                }
                                // Année 2025 (ACRE avec et sans)
                                if (_acre2025) 
                                {
                                    _totalii = (_ii * (_totalTTC * ((2.2 + 12.3 + 0.2) / 100)));
                                    _totaljj = (_jj * (_totalTTC * ((2.2 + 24.6 + 0.2) / 100)));
                                    _totalUrssaf = (_totalii + _totaljj) / (_ii + _jj);
                                }
                                // Année 2025 (sans ACRE) ou > 2025
                                if ((!_acre2024 && !_acre2025)) 
                                {
                                    _totalUrssaf = _totalTTC * ((2.2 + 24.6 + 0.2) / 100);
                                }

                                /************************* GRAPHIQUE **************************/ 

                                try 
                                {
                                    _currentHT = Double.parseDouble(_convTotalHT.replace(",", "."));
                                } 
                                catch (NumberFormatException exc) 
                                {
                                    _currentHT = null;
                                }
    
                                if (_currentMonth != null && _currentHT != null) 
                                {
                                    // Trouver le mois correspondant et stocker la valeur HT
                                    for (int ii = 0; ii < _graphMonths.length; ii++) 
                                    {
                                        if (_graphMonths[ii].equalsIgnoreCase(_currentMonth)) 
                                        {
                                            _graphTotauxHT[ii] = _currentHT;
                                            break; 
                                        }
                                    }
                                }

                                // Résultat de calcul
                                _totalTaxe = _totalHT - _totalUrssaf;
                                String _resultUrssaf = String.format("%.1f", _totalUrssaf);
                                String _resultTaxe = String.format("%.1f", _totalTaxe);
                                _txtTotalTaxeUrssaf.setText(_resultUrssaf);
                                _txtTotalTaxe.setText(_resultTaxe);

                                // Réinitialiser pour le prochain mois
                                _currentMonth = null;
                                _currentHT = null;
                        }  
                        
                        /************************* GRAPHIQUE **************************/

                        // Remplir le dataset avec les données des mois et des totaux HT
                        for (int ii = 0; ii < _graphMonths.length; ii++)
                        {
                            // Ajouter les valeurs (totaux HT) au dataset pour chaque mois
                            if (_graphTotauxHT[ii] != null)
                            {
                                _defaultDataset.addValue(_graphTotauxHT[ii], "Hors Taxe", _graphMonths[ii]);
                            }
                        }    
                    }
  
                    /************************************************************ 
                                             AFFICHGAGE
                    *************************************************************/

                    // Affichage du résultat sans décimales
                    _txtTotalHT.setText(String.format("%.0f", _totalHT));

                    // Si année non renseigné : RAZ
                    String _nothing = (String) _txtTotalHT.getText();
                     if (_nothing.equals("0")) 
                    {
                        /* A1 */ _txtTotalHT.setText("");  
                        /* A2 */ _txtTotalTTC.setText("");
                        /* A3 */ _boxYearsTotal.setSelectedItem("");
                        /* B1 */ _txtTotalTaxeUrssaf.setText("");
                        /* B2 */ _txtTotalTaxe.setText("");           
                        return;
                    }           
                }
                catch (IOException ex) 
                {
                    JOptionPane.showMessageDialog(null, 
                        "Erreur lors du calcul : " + ex.getMessage(),
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                }
            }
        });

        // Onglet Annuel -> RAZ
        _btReset2.addActionListener(new ActionListener()   
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    /* A1 */ _txtTotalHT.setText("");  
                    /* A2 */ _txtTotalTTC.setText("");
                    /* A3 */ _boxYearsTotal.setSelectedItem("");
                    /* B1 */ _txtTotalTaxeUrssaf.setText("");
                    /* B2 */ _txtTotalTaxe.setText("");
            }
        });   

        /************************************************************ 
                                GRAPHIQUE
        *************************************************************/

        // Création du graphique à barres
        JFreeChart _chart = ChartFactory.createBarChart("Montant HT",    /* Titre du graphique */
                                                        null,            /* Axe des abscisses */ 
                                                        null,            /* Axe des ordonnées */  
                            _defaultDataset, PlotOrientation.HORIZONTAL, true,   /*Légende */ 
                                                                         true,   /*Info tooltips */ 
                                                                         false); /* URL */

        // Personnalisation du graphique
        CategoryPlot _catPlot = _chart.getCategoryPlot();
        _catPlot.setDomainGridlinesVisible(true);
        _catPlot.setRangeGridlinesVisible(true);
        _catPlot.setDomainGridlinePaint(Color.BLACK);
        _catPlot.setRangeGridlinePaint(Color.BLACK);

        // Positionnement des éléments
        _gbc.gridwidth = 3;
        _gbc.gridx = 0;
        _gbc.gridy = 0;

        // Création du graphique à barres
        ChartPanel _chartPanel = new ChartPanel(_chart);
        _chartPanel.setPreferredSize(new Dimension(340,400));
        
        JTabbedPane _tabHT = new JTabbedPane();       
        _tabHT.addTab("Total HT", _chartPanel);
        _pan2.add(_tabHT, _gbc);

        /************************************************************ 
                                 LANCEMENT 
        *************************************************************/

        // Fermeture de la fenetre
        _fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _fen.setVisible(true); 
    }
}

/*
        JTabbedPane _tabTTC = new JTabbedPane();
        _tabTTC.addTab("Total TCC", BarChartPanel._chartPanelTTC);
        _pan2.add(_tabTTC, _gbc);
*/