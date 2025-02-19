package com.medails;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

    /************************************************************ 
                        TRAITEMENT DE DONNEES
    *************************************************************/

public class Treatment
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

    // Nom du fichier
    private static final String FILENAME = "Facture.txt";

    // Chemin d'accès du fichier
    public static String filePath = System.getProperty("user.dir") + File.separator + FILENAME;

    // Création du fichier
    public static File mainFile = new File(filePath);

    // Ligne de lecture
    public static String line = null;

    // Année et mois
    public static String years[] = {"", "2024", "2025", "2026", "2027", "2028"};
    public static String months[] = {"", "Janvier", "Février", "Mars", "Avril", 
                                      "Mai", "Juin", "Juillet", "Août", "Septembre", 
                                      "Octobre", "Novembre", "Décembre"};

    public static String[] _graphMonths = {"janvier", "février", "mars", "avril", 
                                           "mai", "juin", "juillet", "août", "septembre", 
                                           "octobre", "novembre", "décembre"};

    public static Double[][][][] _graphTotauxYears = new Double[12][12][12][12];
    public static Double[][][][] _graphTotauxMonths = new Double[12][12][12][12];

    private Display display;
    private Graphic graphic;

    public Treatment(Display display, Graphic graphic)
    {
        System.out.println("Ordre de traitement : 3");

        this.display = display;
        this.graphic = graphic;
        
        graphic.updateDatasets(_graphTotauxYears, _graphTotauxMonths);
        display.setupListeners();       
        initListeners();
    }

        /************************************************************ 
                             Onglet : Enrengistrement 
        *************************************************************/

    private void initListeners()
    {
        // Vérification cellule non-vide : Jours travaillés
        Display.txtDays.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                String text = Display.txtDays.getText();
                if(! text.isEmpty() && !isValidDouble(text))
                {
                    JOptionPane.showMessageDialog(Display.fen, "Veuillez entrer un nombre valide" ,
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                                        Display.txtDays.setText("");
                }
            }
        });

        // Vérification cellule non-vide : TJM
        Display.txtTJM.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                String text = Display.txtDays.getText();
                if(! text.isEmpty() && !isValidDouble(text))
                {
                    JOptionPane.showMessageDialog(Display.fen, "Veuillez entrer un nombre valide" ,
                                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    Display.txtTJM.setText("");
                }
            }
        });

        /************************* FACTURE **************************/ 

        // Facture -> Ouvrir
        Display.btOpenFacture.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _getRep1 = (String) Display.boxRep1.getSelectedItem();
                String _getPDF1 = (String) Display.boxPDF1.getSelectedItem();
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
                    JOptionPane.showMessageDialog(Display.fen, "Le fichier PDF : " + _fileFacture.getAbsolutePath() + " n'a pas été trouvé",
                                                        "Erreur", JOptionPane.WARNING_MESSAGE);
                    Display.boxRep1.setSelectedItem("");
                    Display.boxPDF1.setSelectedItem("");
                }
            }
        });

        // Facture -> Parcourir 
        Display.btSearchFacture.addActionListener(new ActionListener()
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
                int _result = _fileChooser.showOpenDialog(Display.fen);
                if(_result == JFileChooser.APPROVE_OPTION)
                {
                    // Report du répertoire dans champ de saisie (Répertoire)
                    File _selectedRep = _fileChooser.getSelectedFile();
                    String _parentDirectory = _selectedRep.getParent();
                    Display.boxRep1.removeAllItems();
                    Display.boxRep1.addItem(_parentDirectory);
                    // Report du répertoire dans champ de saisie (Nom du PDF)
                    File _selectedPDF = _fileChooser.getSelectedFile();
                    String _namePDF = _selectedPDF.getName();
                    Display.boxPDF1.removeAllItems();
                    Display.boxPDF1.addItem(_namePDF);
                }
            }   
        });

        // Facture -> Répertoire par défaut ...
        Display.btRep1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Enregistrement du répertoire Facture par défaut
                Display.btValid.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (Display.txtRep1.getText() != null)
                        {
                            try 
                            {
                                List<String> _listLine = Files.readAllLines(Paths.get(filePath));

                                // Vérification de l'existence du fichier
                                if (!mainFile.exists())
                                {
                                    mainFile.createNewFile();
                                }
                                
                                // Vérifie si le champ de saisie est vide
                                if (!Display.txtRep1.getText().isEmpty())
                                {
                                    // Vérifie si un répértoire existe déjà
                                    if (!_listLine.contains("Directory Facture --> "))
                                    {
                                        _listLine.remove(0);
                                    }

                                    // Ajoute la ligne en début de liste 
                                    _listLine.add(0, "Directory Facture --> " + Display.txtRep1.getText());

                                    // Ajoute la ligne dans le fichier
                                    Files.write(Paths.get(filePath), 
                                    _listLine, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                                    JOptionPane.showMessageDialog(null, "Le répertoire facture a bien été enregistré",
                                    "Enregistrement", JOptionPane.INFORMATION_MESSAGE);

                                    // Fermeture de la fenêtre après validation
                                    Display.fenRep1.dispose();
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Veuillez compléter le champ de saisie avant de valider",
                                                                    "Champ vide", JOptionPane.INFORMATION_MESSAGE);

                                    // Fermeture de la fenêtre après réponse au message box
                                    Display.fenRep1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                }
                            }
                            catch (IOException ex)
                            {
                            ex.printStackTrace();
                            } 
                        }
                    }
                });
            }
        });

        // Utilisation de Sets pour éviter les doublons
        Set<String> _setRep1 = new HashSet<>();
        Set<String> _setPDF1 = new HashSet<>();

        // Facture -> Onglet 
        Display.boxRep1.addPopupMenuListener(new PopupMenuListener()
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                if (!mainFile.exists()) 
                {
                    try 
                    {
                        mainFile.createNewFile();
                    } 
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                    return;
                }

                try 
                {
                    BufferedReader reader = new BufferedReader(new FileReader(mainFile));

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
                    Display.boxRep1.removeAllItems();
                    Display.boxRep1.addItem("");
                    for (String _getRep1 : _arrayRep1) 
                    {
                        Display.boxRep1.addItem(_getRep1);
                    }
                    Display.boxPDF1.removeAllItems();
                    Display.boxPDF1.addItem("");
                    for (String _getPDF1 : _arrayPDF1) 
                    {
                        Display.boxPDF1.addItem(_getPDF1);
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
        Display.btOpenDecla.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String _getRep2 = (String) Display.boxRep2.getSelectedItem();
                String _getPDF2 = (String) Display.boxPDF2.getSelectedItem();
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
                    JOptionPane.showMessageDialog(Display.fen, "Le fichier PDF : " + _fileDecla.getAbsolutePath() + " n'a pas été trouvé",
                                                        "Erreur", JOptionPane.WARNING_MESSAGE);
                    Display.boxRep2.setSelectedItem("");
                    Display.boxPDF2.setSelectedItem("");
                }
            }
        });

        // Déclaration -> Parcourir 
        Display.btSearchDecla.addActionListener(new ActionListener()
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
                int _result = _fileChooser.showOpenDialog(Display.fen);
                if(_result == JFileChooser.APPROVE_OPTION)
                {
                    // Report du répertoire dans champ de saisie (Répertoire)
                    File _selectedRep = _fileChooser.getSelectedFile();
                    String _parentDirectory = _selectedRep.getParent();
                    Display.boxRep2.removeAllItems();
                    Display.boxRep2.addItem(_parentDirectory);
                    // Report du répertoire dans champ de saisie (Nom du PDF)
                    File _selectedPDF = _fileChooser.getSelectedFile();
                    String _namePDF = _selectedPDF.getName();
                    Display.boxPDF2.removeAllItems();
                    Display.boxPDF2.addItem(_namePDF);
                }
            }   
        });

        // Utilisation de Sets pour éviter les doublons
        Set<String> _setRep2 = new HashSet<>();
        Set<String> _setPDF2 = new HashSet<>();

        // Facture -> Onglet 
        Display.boxRep2.addPopupMenuListener(new PopupMenuListener()
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                if (!mainFile.exists()) 
                {
                    try 
                    {
                        mainFile.createNewFile();
                    } 
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                    return;
                }

                try 
                {
                    BufferedReader reader = new BufferedReader(new FileReader(mainFile));
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
                    Display.boxRep2.removeAllItems();
                    Display.boxRep2.addItem("");
                    for (String _getRep2 : _arrayRep2) 
                    {
                        Display.boxRep2.addItem(_getRep2);
                    }
                    Display.boxPDF2.removeAllItems();
                    Display.boxPDF2.addItem("");
                    for (String _getPDF2 : _arrayPDF2) 
                    {
                        Display.boxPDF2.addItem(_getPDF2);
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
        Display.btReset1.addActionListener(new ActionListener()   
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    /* A1 */ Display.boxYears.setSelectedItem("");
                    /* A2 */ Display.boxMonths.setSelectedItem("");
                    /* A3 */ Display.datePay.setDate(null); 
                    /* B1 */ Display.txtDays.setText("");  
                    /* B2 */ Display.txtTJM.setText("");
                    /* C1 */ Display.txtTTC.setText("");
                    /* C2 */ Display.txtHT.setText("");
                    /* C3 */ Display.txtTVA.setText("");
                    /* D1 */ Display.txtTaxeUrssaf.setText("");
                    /* D2 */ Display.txtTaxe.setText("");
                    /* E1 */ Display.boxRep1.removeAllItems();
                    /* F1 */ Display.boxPDF1.removeAllItems();
                    /* G1 */ Display.boxRep2.removeAllItems();
                    /* H1 */ Display.boxPDF2.removeAllItems(); 
            }
        });

        // Onglet Mensuel -> Calculer
        Display.btTVA.addActionListener(new ActionListener()
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
                String years = (String) Display.boxYears.getSelectedItem();
                String months = (String) Display.boxMonths.getSelectedItem();

                // Vérification cellule non-vide + Calcule : Année + Mois
                if ((years == "") || (months == "")) 
                {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer une date (année + mois)",
                                            "Demande d'informations", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Récupération des valeurs JTextField
                double _days1 = Double.parseDouble(Display.txtDays.getText());
                double _TJM1 = Double.parseDouble(Display.txtTJM.getText()); 
                _HT1 = _days1 * _TJM1;
                _TTC1 = _HT1 * 1.2;
                _TVA1 = _TTC1 - _HT1;

                //____ Calcule Facture (HT + TTC) ___\
                if (! Display.txtDays.getText().isEmpty() && ! Display.txtTJM.getText().isEmpty())
                {
                    String _HT1String = Double.toString(_HT1);
                    Display.txtHT.setText(_HT1String);                   
                    String _TTC1String = Double.toString(_TTC1);
                    Display.txtTTC.setText(_TTC1String);
                    String _TVA1String = Double.toString(_TVA1);
                    Display.txtTVA.setText(_TVA1String);
                }

                //____ Calcule URSSAF (Taxe) ___\
                // Année 2024 (ACRE)
                if
                (years == "2024")
                {
                    _TaxeUrssaf1 = _HT1 * ((2.2 + 11.6 + 0.2) / 100);
                }
                // Année 2025 (ACRE)
                else if 
                ((years == "2025") && 
                (months == "Janvier" ||
                months == "Février"||
                months == "Mars" ||
                months == "Avril")) 
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
                Display.txtTaxeUrssaf.setText(_decimal);

                //____ Calcul URSSAF (Différence) ___\
                _Taxe1 = _HT1 - _TaxeUrssaf1;
                String _Taxe1String = Double.toString(_Taxe1);
                Display.txtTaxe.setText(_Taxe1String);
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Veuillez entrez des nombres valides",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }  
        });

        // Onglet Mensuel -> Enrengistrer
        Display.btSave.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Date _getPay = Display.datePay.getDate();
                SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);

                /* A1 */  String _slctYears = (String)           "Année --> " + Display.boxYears.getSelectedItem();
                /* A2 */  String _slctMonths = (String)          "Mois --> " + Display.boxMonths.getSelectedItem();
                /* A3 */  String _slctPay = (String)             "Versement --> " + _dateFormat.format(_getPay);
                /* B1 */  String _slctDays = (String)            "Jours --> " + Display.txtDays.getText();
                /* B2 */  String _slctTJM = (String)             "TJM --> " + Display.txtTJM.getText();
                /* C1 */  String _slctTTC = (String)             "TTC --> " + Display.txtTTC.getText();
                /* C2 */  String _slctHT = (String)              "HT --> " + Display.txtHT.getText();
                /* C3 */  String _slctTVA = (String)             "TVA --> " + Display.txtTVA.getText();
                /* D1 */  String _slctTaxeUrssaf = (String)      "Urssaf --> " + Display.txtTaxeUrssaf.getText();
                /* D2 */  String _slctTaxe = (String)            "Restant --> " + Display.txtTaxe.getText(); 
                /* G1 */  String _slctFactureRep = (String)      Display.boxRep1.getSelectedItem();
                /* H1 */  String _slctFacturePDF = (String)      Display.boxPDF1.getSelectedItem();
                /* I1 */  String _slctDeclaraRep = (String)      Display.boxRep2.getSelectedItem();
                /* J1 */  String _slctDeclaraPDF = (String)      Display.boxPDF2.getSelectedItem();
                String _seperator = "<->";

                // Vérification cellule non-vide : Facture + Déclaration
                if ((Display.boxRep1.getSelectedItem()) == null || (Display.boxPDF1.getSelectedItem() == null) ||
                    (Display.boxRep2.getSelectedItem()) == null || (Display.boxPDF2.getSelectedItem() == null))
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
                        if (!mainFile.exists())
                        {
                            mainFile.createNewFile();
                        }

                        List<String> _listLine = Files.readAllLines(Paths.get(filePath));

                        // Si la ligne n'existe pas déjà, on l'ajoute
                        if (! line.contains(_slctFacturePDF))
                        {
                            // Ajoute la ligne dans le fichier
                            Files.write(Paths.get(filePath),
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
        Display.boxYearsTotal.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Vérification si année de sélection vide
                String _selectedYear = (String) Display.boxYearsTotal.getSelectedItem();
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
                                        
                if (_selectedYear.isEmpty()) 
                {
                    /* A1 */ Display.txtTotalHT.setText("");  
                    /* A2 */ Display.txtTotalTTC.setText("");
                    /* A3 */ Display.boxYearsTotal.setSelectedItem("");
                    /* B1 */ Display.txtTotalTaxeUrssaf.setText("");
                    /* B2 */ Display.txtTotalTaxe.setText("");          
                    return;
                }

                /************************************************************ 
                                        LECTURE .TXT
                *************************************************************/

                try (BufferedReader reader = new BufferedReader(new FileReader("Facture.txt"))) 
                {
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
                            Display.txtTotalTTC.setText(convTotalTTC);

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
                            Display.txtTotalTaxeUrssaf.setText(_resultUrssaf);
                            Display.txtTotalTaxe.setText(_resultTaxe);

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
                    }                        
                        
                    /************************************************************ 
                                             AFFICHGAGE
                    *************************************************************/

                    // Affichage du résultat sans décimales
                    Display.txtTotalHT.setText(String.format("%.0f", _totalHT));

                    // Si année non renseigné : RAZ
                    String _nothing = (String) Display.txtTotalHT.getText();
                    if (_nothing.equals("0")) 
                    {
                        /* A1 */ Display.txtTotalHT.setText("");  
                        /* A2 */ Display.txtTotalTTC.setText("");
                        /* A3 */ Display.boxYearsTotal.setSelectedItem("");
                        /* B1 */ Display.txtTotalTaxeUrssaf.setText("");
                        /* B2 */ Display.txtTotalTaxe.setText("");           
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
        Display.btReset2.addActionListener(new ActionListener()   
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    /* A1 */ Display.txtTotalHT.setText("");  
                    /* A2 */ Display.txtTotalTTC.setText("");
                    /* A3 */ Display.boxYearsTotal.setSelectedItem("");
                    /* B1 */ Display.txtTotalTaxeUrssaf.setText("");
                    /* B2 */ Display.txtTotalTaxe.setText("");
            }
        });   
    }
}