import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import javax.swing.*;
import java.awt.*;

public class BarChartPanel extends JPanel {

    public static String[] _months = {"Janvier", "Février", "Mars", "Avril", 
                                      "Mai", "Juin", "Juillet", "Août", "Septembre", 
                                      "Octobre", "Novembre", "Décembre"};

    public static Double[] _totauxHT = new Double[12];

    /************************************************************ 
                        TRAITEMENT DES DONNEES
    *************************************************************/
        
    // Constructeur
    public BarChartPanel() 
    {
        // Afficher les mois et leur total HT
        for (int ii = 0; ii < _months.length; ii++) 
        {
            System.out.println(_months[ii] + ": " + (_totauxHT[ii] != null ? _totauxHT[ii] : "Aucune donnée"));
        }
    }

    // Méthode pour traiter les données du fichier
    public void processFactureData() 
    {
        String _currentMonth = null;
        Double _currentHT = null;

        // Chercher le mois
        if (Facture.line.contains("Mois --> ")) 
        {   
            _currentMonth = Facture.line.substring(Facture.line.indexOf("Mois --> ") + 9).trim();
        }

        // Chercher le total HT
        if (Facture.line.contains("HT --> ") && _currentMonth != null) 
        {
            String _valueHT = Facture.line.substring(Facture.line.indexOf("HT --> ") + 7).trim();
            try 
            {
                _currentHT = Double.parseDouble(_valueHT.replace(",", "."));
            } 
            catch (NumberFormatException e) 
            {
                _currentHT = null;
            }

            if (_currentMonth != null && _currentHT != null) 
            {
                // Trouver le mois correspondant et stocker la valeur HT
                for (int ii = 0; ii < _months.length; ii++) 
                {
                    if (_months[ii].equalsIgnoreCase(_currentMonth)) 
                    {
                        _totauxHT[ii] = _currentHT;
                        break;  // Une fois le mois trouvé, sortir de la boucle
                    }
                }
            }

            // Réinitialiser pour le prochain mois
            _currentMonth = null;
            _currentHT = null;
        }
    }

    /************************************************************ 
                             GRAPHIQUE
    *************************************************************/

    public static ChartPanel _chartPanel;

    public BarChartPanel(CategoryDataset _categoryDataset)
    {
        // Création du graphique à barres
        JFreeChart _chart = ChartFactory.createBarChart("HT par mois",   /* Titre du graphique */
                                                        "Mois",          /* Axe des abscisses */ 
                                                        "Montant",       /* Axe des ordonnées */  
                   _categoryDataset, PlotOrientation.HORIZONTAL, true,   /*Légende */ 
                                                                 true,   /*Info tooltips */ 
                                                                 false); /* URL */

        // Personnalisation du graphique
        CategoryPlot _catPlot = _chart.getCategoryPlot();
        _catPlot.setDomainGridlinesVisible(true);
        _catPlot.setRangeGridlinesVisible(true);
        _catPlot.setDomainGridlinePaint(Color.BLACK);
        _catPlot.setRangeGridlinePaint(Color.BLACK);
         
        // Ajouter le graphique au panneau
        _chartPanel = new ChartPanel(_chart);
        _chartPanel.setPreferredSize(new Dimension(340,400));
        setLayout(new BorderLayout());
        add(_chartPanel, BorderLayout.CENTER);
    }

    public ChartPanel getChartPanel()
    {
        return _chartPanel;
    }

    private static CategoryDataset createDataset(double [] values)
    {
        // Créer un dataset vide
        DefaultCategoryDataset _defaultDataset = new DefaultCategoryDataset();

        // Remplir le dataset avec les données des mois et des totaux HT
        for (int ii = 0; ii < _months.length; ii++)
        {
            // Ajouter les valeurs (totaux HT) au dataset pour chaque mois
            if (_totauxHT[ii] != null)
            {
                _defaultDataset.addValue(values[ii], "Total HT", _months[ii]);
            }
        }                 
        return _defaultDataset;
    }
}
