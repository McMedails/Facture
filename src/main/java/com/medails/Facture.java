import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.Set;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import com.toedter.calendar.JDateChooser;


public class Facture
{

    // Vérificateur de chiffre dans JTextField
    public static boolean isValidDouble(String text)
    {   
        try 
        { 
            Double.parseDouble(text.trim());    
            return true; 
        }
        catch (NumberFormatException e) 
        { return false; } 
    }

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
        _onglet.add("Enregistrement", _scroll1);
        _onglet.add("Graphique", _scroll2);

        // Ajout des Onglets dans Fenetre
        _fen.add(_onglet, BorderLayout.CENTER);

        /************************************************************ 
                                 DONNEES 
         ***********************************************************/

        // Année et mois
        String _years[] = {"", "2024", "2025", "2026", "2027", "2028"};
        String _months[] = {"", "Janvier", "Février", "Mars", "Avril", 
                            "Mai", "Juin", "Juillet", "Août", "Septembre", 
                            "Octobre", "Novembre", "Décembre"};
    
        // Nom du fichier BDD
        File _mainFile = new File("Facture.txt");
        
        // Données pour graphique
        DefaultCategoryDataset _dataYears = new DefaultCategoryDataset();
        DefaultCategoryDataset _dataMonths = new DefaultCategoryDataset();

        /************************************************************ 
                           Onglet : Enrengistrement 
        *************************************************************/

        /************************** Facture **************************/

        // Facture
        JLabel _labFacture = new JLabel("<html><u>Facture</u></html>");
        Font _fontFacture = new Font("Arial", Font.BOLD, 18);
        _labFacture.setFont(_fontFacture);
        _gbc.gridwidth = 1;
        _gbc.gridx = 0;
        _gbc.gridy = 0;       
        _pan1.add(_labFacture, _gbc);
    
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

        // A3 - Date de paiement
        JLabel _labPay = new JLabel("Date de Paiement");
        _gbc.gridx = 2;
        _gbc.gridy = 1;
        _pan1.add(_labPay, _gbc);
        JDateChooser _datePay = new JDateChooser();
        _datePay.setPreferredSize(new Dimension(100, 18));
        _gbc.gridx = 2;
        _gbc.gridy = 2;
        _pan1.add(_datePay, _gbc);

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

        // C1 - Résultat TTC
        JLabel _labTTC = new JLabel("TTC");
        _gbc.gridx = 0;
        _gbc.gridy = 5;   
        _pan1.add(_labTTC, _gbc);
        JTextField _txtTTC = new JTextField();
        _txtTTC.setPreferredSize(new Dimension(60, 18));
        _txtTTC.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 6;
        _pan1.add(_txtTTC, _gbc); 

        // C2 - Résultat HT
        JLabel _labHT = new JLabel("HT");
        _gbc.gridx = 1;
        _gbc.gridy = 5;   
        _pan1.add(_labHT, _gbc);
        JTextField _txtHT = new JTextField();
        _txtHT.setPreferredSize(new Dimension(60, 18));
        _txtHT.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 6;
        _pan1.add(_txtHT, _gbc);

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
        _pan1.add(_boxPDF2, _gbc); 

        /************************ Enregistrer ************************/

        // I1 - Enregistrer
        JButton _btSave = new JButton("Enregistrer");
        _gbc.gridwidth = 2;
        _gbc.gridx = 0;
        _gbc.gridy = 25;
        _pan1.add(_btSave, _gbc);

        // I2 - RAZ
        JButton _btReset1 = new JButton("RAZ");
        _gbc.gridx = 1;
        _gbc.gridy = 25;
        _pan1.add(_btReset1, _gbc);

        /************************************************************ 
                           Onglet : Graphique 
        *************************************************************/

        /************************* Facture **************************/

        JLabel _labTotalFacture = new JLabel("<html><u>Facture</u></html>");
        Font _fontTotal = new Font("Arial", Font.BOLD, 18);
        _labTotalFacture.setFont(_fontTotal);
        _gbc.gridwidth = 1;
        _gbc.gridx = 0;
        _gbc.gridy = 1;       
        _pan2.add(_labTotalFacture, _gbc);

        // A2 - Résultat Total TTC
        JLabel _labTotalFactureTTC = new JLabel("Total TTC");
        _gbc.gridx = 0;
        _gbc.gridy = 2;   
        _pan2.add(_labTotalFactureTTC, _gbc);
        JTextField _txtTotalTTC = new JTextField();
        _txtTotalTTC.setPreferredSize(new Dimension(60, 18));
        _txtTotalTTC.setEnabled(true);
        _gbc.gridx = 0;
        _gbc.gridy = 3;
        _pan2.add(_txtTotalTTC, _gbc); 

        // A1 - Résultat Total HT
        JLabel _labTotalFactureHT = new JLabel("Total HT");
        _gbc.gridx = 1;
        _gbc.gridy = 2;   
        _pan2.add(_labTotalFactureHT, _gbc);
        JTextField _txtTotalHT = new JTextField();
        _txtTotalHT.setPreferredSize(new Dimension(60, 18));
        _txtTotalHT.setEnabled(true);
        _gbc.gridx = 1;
        _gbc.gridy = 3;
        _pan2.add(_txtTotalHT, _gbc);

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
        JLabel _labTotalFactureTaxeUrssaf = new JLabel("Total taxe");
        _gbc.gridx = 0;
        _gbc.gridy = 5;   
        _pan2.add(_labTotalFactureTaxeUrssaf, _gbc);
        JTextField _txtTotalTaxeUrssaf = new JTextField();
        _txtTotalTaxeUrssaf.setPreferredSize(new Dimension(60, 18));
        _txtTotalTaxeUrssaf.setEnabled(true);       
        _gbc.gridx = 0;
        _gbc.gridy = 6;
        _pan2.add(_txtTotalTaxeUrssaf, _gbc);

        // B2 - Différence Total 
        JLabel _labTotalFactureTaxe = new JLabel("Total différence");
        _gbc.gridx = 1;
        _gbc.gridy = 5;   
        _pan2.add(_labTotalFactureTaxe, _gbc);
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
                              Onglet : Enrengistrement 
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

        /************************* FACTURE **************************/ 

        // Facture -> Ouvrir
        _btOpenFacture.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _getRep1 = (String) _boxRep1.getSelectedItem();
                String _getPDF1 = (String) _boxPDF1.getSelectedItem();
                File _fileFacture = new File(_getRep1 + "/" + _getPDF1);
                try
                {
                    if (_fileFacture.exists())
                    {
                        Desktop.getDesktop().open(_fileFacture);
                    }
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(_fen, "Le fichier PDF : " + _fileFacture.getAbsolutePath() + " n'a pas été trouvé",
                                                          "Erreur", JOptionPane.WARNING_MESSAGE);
                    _boxRep1.setSelectedItem("");
                    _boxPDF1.setSelectedItem("");
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
                String _directoryFacture = "M://Multimédia/Bureau/Social/Social - Pc Bureau/01 - Professionnelle/Factures";
                _fileChooser.setCurrentDirectory(new File(_directoryFacture));
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

        // Utilisation de Sets pour éviter les doublons
        Set<String> _setRep1 = new HashSet<>();
        Set<String> _setPDF1 = new HashSet<>();

        // Facture -> Onglet 
        _boxRep1.addPopupMenuListener(new PopupMenuListener()
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                if (!_mainFile.exists()) 
                {
                    try 
                    {
                        _mainFile.createNewFile();
                    } 
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                    return;
                }

                try 
                {
                    BufferedReader reader = new BufferedReader(new FileReader(_mainFile));
                    String line;

                    // Vide les Sets
                    _setRep1.clear();
                    _setPDF1.clear();

                    // Utilisation de ArrayList pour pouvoir trier
                    ArrayList<String> _arrayRep1 = new ArrayList<>();
                    ArrayList<String> _arrayPDF1 = new ArrayList<>();

                    while ((line = reader.readLine()) != null) 
                    {
                        if (line.contains("01 - Professionnelle"))
                        {
                            String _getRep1 = line;
                            if (!_setRep1.contains(_getRep1)) 
                            {
                                _setRep1.add(_getRep1);
                                _arrayRep1.add(_getRep1);
                            }
                        } 
                        else if (line.startsWith("Facture"))
                        {
                            String _getPDF1 = line;
                            if (!_setPDF1.contains(_getPDF1)) 
                            {
                                _setPDF1.add(_getPDF1);
                                _arrayPDF1.add(_getPDF1);
                            }
                        }
                    }
                    reader.close();

                    // Tri des listes par ordre alphabétique
                    Collections.sort(_arrayRep1);
                    Collections.sort(_arrayPDF1);

                    // Mise à jour des ComboBox
                    _boxRep1.removeAllItems();
                    _boxRep1.addItem("");
                    for (String _getRep1 : _arrayRep1) 
                    {
                        _boxRep1.addItem(_getRep1);
                    }
                    _boxPDF1.removeAllItems();
                    _boxPDF1.addItem("");
                    for (String _getPDF1 : _arrayPDF1) 
                    {
                        _boxPDF1.addItem(_getPDF1);
                    }
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        /************************* DECLARATION **************************/ 

        // Déclaration -> Ouvrir
        _btOpenDecla.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _getRep2 = (String) _boxRep2.getSelectedItem();
                String _getPDF2 = (String) _boxPDF2.getSelectedItem();
                File _fileDecla = new File(_getRep2 + "/" + _getPDF2);
                try
                {
                    if (_fileDecla.exists())
                    {
                        Desktop.getDesktop().open(_fileDecla);
                    }
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(_fen, "Le fichier PDF : " + _fileDecla.getAbsolutePath() + " n'a pas été trouvé",
                                                          "Erreur", JOptionPane.WARNING_MESSAGE);
                    _boxRep2.setSelectedItem("");
                    _boxPDF2.setSelectedItem("");
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
                String _directoryDecla = "M://Multimédia/Bureau/Social/Social - Pc Bureau/00 - Gouvernement/URSSAF/Déclarations";
                _fileChooser.setCurrentDirectory(new File(_directoryDecla));
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
  
        // Utilisation de Sets pour éviter les doublons
        Set<String> _setRep2 = new HashSet<>();
        Set<String> _setPDF2 = new HashSet<>();

        // Facture -> Onglet 
        _boxRep2.addPopupMenuListener(new PopupMenuListener()
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                if (!_mainFile.exists()) 
                {
                    try 
                    {
                        _mainFile.createNewFile();
                    } 
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                    return;
                }

                try 
                {
                    BufferedReader reader = new BufferedReader(new FileReader(_mainFile));
                    String line;

                    // Vide les Sets
                    _setRep2.clear();
                    _setPDF2.clear();

                    // Utilisation de ArrayList pour pouvoir trier
                    ArrayList<String> _arrayRep2 = new ArrayList<>();
                    ArrayList<String> _arrayPDF2 = new ArrayList<>();

                    while ((line = reader.readLine()) != null) 
                    {
                        if (line.contains("00 - Gouvernement"))
                        {
                            String _getRep2 = line;
                            if (!_setRep2.contains(_getRep2)) 
                            {
                                _setRep2.add(_getRep2);
                                _arrayRep2.add(_getRep2);
                            }
                        } 
                        else if (line.startsWith("Déclaration"))
                        {
                            String _getPDF2 = line;
                            if (!_setPDF2.contains(_getPDF2)) 
                            {
                                _setPDF2.add(_getPDF2);
                                _arrayPDF2.add(_getPDF2);
                            }
                        }
                    }
                    reader.close();

                    // Tri des listes par ordre alphabétique
                    Collections.sort(_arrayRep2);
                    Collections.sort(_arrayPDF2);

                    // Mise à jour des ComboBox
                    _boxRep2.removeAllItems();
                    _boxRep2.addItem("");
                    for (String _getRep2 : _arrayRep2) 
                    {
                        _boxRep2.addItem(_getRep2);
                    }
                    _boxPDF2.removeAllItems();
                    _boxPDF2.addItem("");
                    for (String _getPDF2 : _arrayPDF2) 
                    {
                        _boxPDF2.addItem(_getPDF2);
                    }
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        /************************* AUTRES **************************/ 

        // Onglet Mensuel -> RAZ
        _btReset1.addActionListener(new ActionListener()   
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    /* A1 */ _boxYears.setSelectedItem("");
                    /* A2 */ _boxMonths.setSelectedItem("");
                    /* A3 */ _datePay.setDate(null); 
                    /* B1 */ _txtDays.setText("");  
                    /* B2 */ _txtTJM.setText("");
                    /* C1 */ _txtTTC.setText("");
                    /* C2 */ _txtHT.setText("");
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

                // Récupération des valeurs JTextField
                double _days1 = Double.parseDouble(_txtDays.getText());
                double _TJM1 = Double.parseDouble(_txtTJM.getText()); 
                _HT1 = _days1 * _TJM1;
                _TTC1 = _HT1 * 1.2;
                _TVA1 = _TTC1 - _HT1;

                //____ Calcule Facture (HT + TTC) ___\
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
                    _TaxeUrssaf1 = _HT1 * ((2.2 + 11.6 + 0.2) / 100);
                }
                // Année 2025 (ACRE)
                else if 
                ((_years == "2025") && 
                 (_months == "Janvier" ||
                  _months == "Février"||
                  _months == "Mars" ||
                  _months == "Avril")) 
                {
                    _TaxeUrssaf1 = _HT1 * ((2.2 + 12.3 + 0.2) / 100);
                }
                // Année 2025 (sans ACRE)
                else
                {
                    _TaxeUrssaf1 = _HT1 * ((2.2 + 24.6 + 0.2) / 100);
                }
                // Résultat de calcul
                String _decimal = String.format("%.1f", _TaxeUrssaf1);
                _txtTaxeUrssaf.setText(_decimal);

                //____ Calcul URSSAF (Différence) ___\
                _Taxe1 = _HT1 - _TaxeUrssaf1;
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
                Date _getPay = _datePay.getDate();
                SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

                /* A1 */  String _slctYears = (String)           "Année --> " + _boxYears.getSelectedItem();
                /* A2 */  String _slctMonths = (String)          "Mois --> " + _boxMonths.getSelectedItem();
                /* A3 */  String _slctPay = (String)             "Versement --> " + _dateFormat.format(_getPay);
                /* B1 */  String _slctDays = (String)            "Jours --> " + _txtDays.getText();
                /* B2 */  String _slctTJM = (String)             "TJM --> " + _txtTJM.getText();
                /* C1 */  String _slctTTC = (String)             "TTC --> " + _txtTTC.getText();
                /* C2 */  String _slctHT = (String)              "HT --> " + _txtHT.getText();
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
                              /* A3 */ _slctPay + System.lineSeparator() +
                              /* B1 */ _slctDays + System.lineSeparator() +
                              /* B2 */ _slctTJM + System.lineSeparator() +
                              /* C1 */ _slctTTC + System.lineSeparator() +
                              /* C2 */ _slctHT + System.lineSeparator() +
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
                           Onglet : Graphique 
        *************************************************************/
        
        // Onglet Annuel -> Année (Popup)
        _boxYearsTotal.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Réinitialisation des tableaux
                _dataYears.clear();
                _dataMonths.clear();

                // Vérification si année de sélection vide
                String _selectedYear = (String) _boxYearsTotal.getSelectedItem();
                boolean _monthAcre = false;
                boolean _acre2024 = false;
                boolean _acre2025 = false;
                boolean _currentYear = false;
                String _currentMonth = null;
                Double _currentHT = null;
                Double _currentTTC = null;
                Double _currentTaxe = null;
                Double _currentUrssaf = null;
                Double _totalHT = 0.0;
                Double _graphHT = 0.0;
                Double _graphTTC = 0.0;
                Double _graphTaxe = 0.0;
                Double _graphUrssaf = 0.0;
                Double _ii = 0.0;
                Double _jj = 0.0;
                Double[][][][] _graphTotauxYears = new Double[12][12][12][12];
                Double[][][][] _graphTotauxMonths = new Double[12][12][12][12];
                String[] _graphMonths = {"janvier", "février", "mars", "avril", 
                                         "mai", "juin", "juillet", "août", "septembre", 
                                         "octobre", "novembre", "décembre"};
                                         
                if (_selectedYear.isEmpty()) 
                {
                    /* A1 */ _txtTotalHT.setText("");  
                    /* A2 */ _txtTotalTTC.setText("");
                    /* A3 */ _boxYearsTotal.setSelectedItem("");
                    /* B1 */ _txtTotalTaxeUrssaf.setText("");
                    /* B2 */ _txtTotalTaxe.setText("");          
                    return;
                }

                /************************************************************ 
                                        LECTURE .TXT
                *************************************************************/

                try (BufferedReader reader = new BufferedReader(new FileReader("Facture.txt"))) 
                {
                    String line;
                    while ((line = reader.readLine()) != null) 
                    {
                        // Ligne vide, on arrête
                        if (line.trim().isEmpty()) 
                        { break; }

                        /************************************************************ 
                                                 CALCULES
                        *************************************************************/

                        // Détection de l'année sélectionnée
                        if (line.contains("Versement --> ") &&  line.contains(_selectedYear))
                        {
                            // Enrengistrement de l'année sélectionnée
                            _currentYear = true;
                            // Année 2024 (ACRE)
                            if (line.contains("2024")) 
                            {
                                _acre2024 = true;
                                _acre2025 = false; 
                            }
                            // Année 2025 (ACRE)
                            else if (line.contains("2025"))
                            {
                                // Vérification mois (ACRE) 
                                if ((line.contains("janvier") ||
                                    line.contains("février") ||
                                    line.contains("mars") ||
                                    line.contains("avril"))) 
                                    { 
                                        _monthAcre = true;
                                        _ii++; 
                                    }
                                if ((line.contains("mai") ||
                                    line.contains("juin") ||
                                    line.contains("juillet") ||
                                    line.contains("août") || 
                                    line.contains("septembre") || 
                                    line.contains("octobre") || 
                                    line.contains("novembre") || 
                                    line.contains("décembre"))) 
                                    {
                                        _monthAcre = false;
                                        _jj++; 
                                    }
                                    
                                _acre2024 = false;
                                _acre2025 = true;
                            }
                            // Année sans ACRE
                            else
                            {
                                _acre2024 = false;
                                _acre2025 = false;
                            }

                            // Recherche des mois de versement
                            String _monthPart = line.substring(line.indexOf("Versement --> ") + 17).trim();
                            int _firstSpaceIndex = _monthPart.indexOf(" ");
                            if (_firstSpaceIndex != -1)
                            {
                                _currentMonth = _monthPart.substring(0, _firstSpaceIndex).trim();  
                            }
                        } 

                        /************************* CALCULES **************************/  

                        if (_currentYear && line.contains("HT --> ")) 
                        {
                            //____ Calcule Facture (HT + TTC) ___\
                            String _convTotalHT = line.substring(line.indexOf("HT --> ") + 7).trim();
                            _totalHT += Double.parseDouble(_convTotalHT); 
                            _graphHT = Double.parseDouble(_convTotalHT);
                            _graphTTC = Double.parseDouble(_convTotalHT) * 1.2;

                            // Caclule TVA
                            double _totalTTC = _totalHT * 1.2;
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
                                _totalUrssaf = _totalHT * ((2.2 + 11.6 + 0.2) / 100);
                                _graphUrssaf = _graphHT * ((2.2 + 11.6 + 0.2) / 100);
                                _graphTaxe = _graphHT - _graphUrssaf;
                            }

                            // Année 2025 (ACRE avec et sans)
                            if (_acre2025) 
                            {
                                _totalii = (_ii * (_totalHT * ((2.2 + 12.3 + 0.2) / 100)));
                                _totaljj = (_jj * (_totalHT * ((2.2 + 24.6 + 0.2) / 100)));
                                _totalUrssaf = (_totalii + _totaljj) / (_ii + _jj);
                                if(_monthAcre)
                                {
                                    _graphUrssaf = _graphHT * ((2.2 + 12.3 + 0.2) / 100);
                                    _graphTaxe = _graphHT - _graphUrssaf;
                                }
                                else
                                {
                                    _graphUrssaf = _graphHT * ((2.2 + 24.6 + 0.2) / 100);
                                    _graphTaxe = _graphHT - _graphUrssaf;
                                }
                            }

                            // Année 2025 (sans ACRE) ou > 2025
                            if ((!_acre2024 && !_acre2025)) 
                            {
                                _totalUrssaf = _totalHT * ((2.2 + 24.6 + 0.2) / 100);
                                _graphUrssaf = _graphHT * ((2.2 + 24.6 + 0.2) / 100);
                                _graphTaxe = _graphHT - _graphUrssaf;
                            }

                            // Résultat de calcul
                            _totalTaxe = _totalHT - _totalUrssaf;
                            String _resultUrssaf = String.format("%.1f", _totalUrssaf);
                            String _resultTaxe = String.format("%.1f", _totalTaxe);
                            _txtTotalTaxeUrssaf.setText(_resultUrssaf);
                            _txtTotalTaxe.setText(_resultTaxe);
 
                            /************************* GRAPHIQUE **************************/ 

                            try 
                            {
                                _currentTTC = _graphTTC;
                                _currentHT = Double.parseDouble(_convTotalHT.replace(",", "."));
                                _currentTaxe = _graphTaxe;
                                _currentUrssaf = _graphUrssaf;
                            } 
                            catch (NumberFormatException exc) 
                            {
                                _currentTTC = null;
                                _currentHT = null;
                                _currentTaxe = null;
                                _currentUrssaf = null;
                            }
    
                            if (_currentMonth != null && _currentTTC != null && _currentHT != null && _currentTaxe != null && _currentUrssaf != null) 
                            {
                                // Trouve le mois correspondant et stocke la valeur HT dans le tableau
                                for (int ii = 0; ii < _graphMonths.length; ii++) 
                                {
                                    if (_graphMonths[ii].equals(_currentMonth)) 
                                    {
                                        _graphTotauxYears[ii][ii][ii][0] = _currentTTC;
                                        _graphTotauxYears[ii][ii][ii][1] = _currentHT; 
                                        _graphTotauxYears[ii][ii][ii][2] = _currentTaxe;  
                                        _graphTotauxYears[ii][ii][ii][3] = _currentUrssaf;  
                                        _graphTotauxMonths[ii][ii][ii][0] = _currentTTC;
                                        _graphTotauxMonths[ii][ii][ii][1] = _currentHT; 
                                        _graphTotauxMonths[ii][ii][ii][2] = _currentTaxe;     
                                        _graphTotauxMonths[ii][ii][ii][3] = _currentUrssaf;     
                                    }
                                }
                            }

                            // Réinitialise pour le prochain mois
                            _currentYear = false;
                            _currentMonth = null;
                            _currentTTC = null;
                            _currentHT = null;
                            _currentTaxe = null;
                            _currentUrssaf = null;
                        }  
                        
                        // Remplir le dataset avec les données des mois et des totaux HT
                        for (int ii = 0; ii < _graphMonths.length; ii++)
                        {
                            /************************* ANNUEL **************************/

                            // Ajouter les valeurs (totaux TTC) au dataset pour chaque mois 
                            if (_graphTotauxYears[ii][ii][ii][0] != null)
                            {
                                _dataYears.addValue(_graphTotauxYears[ii][ii][ii][0], "TTC", _graphMonths[ii]);
                            }
                            else
                            {
                                _graphTotauxYears[ii][ii][ii][0] = 0.0;
                            }

                            // Ajouter les valeurs (totaux HT) au dataset pour chaque mois
                            if (_graphTotauxYears[ii][ii][ii][1] != null)
                            {
                                _dataYears.addValue(_graphTotauxYears[ii][ii][ii][1], "HT", _graphMonths[ii]);
                            }
                            else
                            {
                                _graphTotauxYears[ii][ii][ii][1] = 0.0;
                            }

                            // Ajouter les valeurs (restant) au dataset pour chaque mois
                            if (_graphTotauxYears[ii][ii][ii][2] != null)
                            {
                                _dataYears.addValue(_graphTotauxYears[ii][ii][ii][2], "Restant", _graphMonths[ii]);
                            }
                            else
                            {
                                _graphTotauxYears[ii][ii][ii][2] = 0.0;
                            }

                            // Ajouter les valeurs (Urssaf) au dataset pour chaque mois
                            if (_graphTotauxYears[ii][ii][ii][3] != null)
                            {
                                _dataYears.addValue(_graphTotauxYears[ii][ii][ii][3], "URSSAF", _graphMonths[ii]);
                            }
                            else
                            {
                                _graphTotauxYears[ii][ii][ii][3] = 0.0;
                            }

                            /************************* MENSUEL **************************/ 

                            // Ajouter les valeurs (totaux TTC) au dataset pour chaque mois 
                            if (_graphTotauxMonths[ii][ii][ii][0] != null)
                            {
                                _dataMonths.addValue(_graphTotauxMonths[ii][ii][ii][0], "TTC", _graphMonths[ii]);
                            }

                            // Ajouter les valeurs (totaux HT) au dataset pour chaque mois 
                            if (_graphTotauxMonths[ii][ii][ii][1] != null)
                            {
                                _dataMonths.addValue(_graphTotauxMonths[ii][ii][ii][1], "HT", _graphMonths[ii]); 
                            }

                            // Ajouter les valeurs (restant) au dataset pour chaque mois 
                            if (_graphTotauxMonths[ii][ii][ii][2] != null)
                            {
                                _dataMonths.addValue(_graphTotauxMonths[ii][ii][ii][2], "Restant", _graphMonths[ii]);
                            }

                            // Ajouter les valeurs (Urssaf) au dataset pour chaque mois 
                            if (_graphTotauxMonths[ii][ii][ii][3] != null)
                            {
                                _dataMonths.addValue(_graphTotauxMonths[ii][ii][ii][3], "URSSAF", _graphMonths[ii]);
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

        // Création du graphique à barres (Annuel)
        JFreeChart _chartYears = ChartFactory.createBarChart(null,                 /* Titre du graphique */
                                                            null,                  /* Axe des abscisses */ 
                                                           "Résultats",            /* Axe des ordonnées */  
                                _dataYears, PlotOrientation.HORIZONTAL, true,      /*Légende */ 
                                                                        true,      /*Info tooltips */ 
                                                                        false);    /* URL */

        // Création du graphique à barres (Mensuel)
        JFreeChart _chartMonths = ChartFactory.createBarChart(null,                /* Titre du graphique */
                                                              null,                /* Axe des abscisses */ 
                                                             "Résultats",          /* Axe des ordonnées */  
                                 _dataMonths, PlotOrientation.HORIZONTAL, true,    /*Légende */ 
                                                                          true,    /*Info tooltips */ 
                                                                          false);  /* URL */

        // Personnalisation du graphique (Annuel)
        CategoryPlot _catPlotYears = _chartYears.getCategoryPlot();
        _catPlotYears.setDomainGridlinesVisible(true);
        _catPlotYears.setRangeGridlinesVisible(true);
        _catPlotYears.setDomainGridlinePaint(Color.BLACK);
        _catPlotYears.setRangeGridlinePaint(Color.BLACK);
        NumberAxis _rangeAxisYears = (NumberAxis) _catPlotYears.getRangeAxis();
        _rangeAxisYears.setRange(0, 10000);

        // Personnalisation du graphique (Mensuel)
        CategoryPlot _catPlotMonths = _chartMonths.getCategoryPlot();
        _catPlotMonths.setDomainGridlinesVisible(true);
        _catPlotMonths.setRangeGridlinesVisible(true);
        _catPlotMonths.setDomainGridlinePaint(Color.BLACK);
        _catPlotMonths.setRangeGridlinePaint(Color.BLACK);
        NumberAxis _rangeAxisMonths = (NumberAxis) _catPlotMonths.getRangeAxis();
        _rangeAxisMonths.setRange(0, 10000);

        // Création du graphique à barres
        ChartPanel _chartPanelYears = new ChartPanel(_chartYears);
        ChartPanel _chartPanelMonths = new ChartPanel(_chartMonths);
        _chartPanelYears.setPreferredSize(new Dimension(340,400));
        _chartPanelMonths.setPreferredSize(new Dimension(340,400));
        
        JTabbedPane _tabGraph = new JTabbedPane(); 
        _tabGraph.setTabPlacement(JTabbedPane.BOTTOM);
        _tabGraph.addTab("Annuel", _chartPanelYears);
        _tabGraph.addTab("Mensuel", _chartPanelMonths);

        // Positionnement des éléments
        _gbc.gridwidth = 3;
        _gbc.gridx = 0;
        _gbc.gridy = 0;
        _pan2.add(_tabGraph, _gbc);

        /************************************************************ 
                                 LANCEMENT 
        *************************************************************/

        // Fermeture de la fenetre
        _fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _fen.setVisible(true); 
    }
}
