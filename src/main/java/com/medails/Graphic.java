package com.medails;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
        
/************************************************************ 
                        GRAPHIQUE
*************************************************************/

public class Graphic
{
    /************************* Variables d'instance **************************/

    public final String[] GRAPHMONTHS = {"janvier", "février", "mars", "avril", 
                                         "mai", "juin", "juillet", "août", "septembre", 
                                         "octobre", "novembre", "décembre"};

    public final String[] GRAPHYEARS = {"2024", "2025", "2026", "2027", "2028"};                                     

    private final String[] CATEGORIES = {"TTC", "TVA", "HT", "URSSAF", "Bénéfices"};

    private final int MAXRANGE_GRAPHIC = 10000;
    private final int WIDTH_GRAPHIC = 340;
    private final int HEIGHT_GRAPHIC = 400;
    private DefaultCategoryDataset dataYears = new DefaultCategoryDataset();
    private DefaultCategoryDataset dataMonths = new DefaultCategoryDataset();
    private DefaultCategoryDataset dataDeduction = new DefaultCategoryDataset();
    private JFreeChart chartYears;
    private JFreeChart chartMonths;
    private JFreeChart chartDeduction;
    private ChartPanel chartPanelYears;
    private ChartPanel chartPanelMonths;
    private ChartPanel chartPanelDeduction;

    /************* Déclarations Classes ****************/
    private Display display;
    private Treatment1 treatment1; 
    
    /*********** Constructeur ***************/
    public Graphic (Display display)
    {
        this.display = display;
        chartYears = createChart(dataYears, MAXRANGE_GRAPHIC);
        chartMonths = createChart(dataMonths, MAXRANGE_GRAPHIC);
        chartDeduction = createChart(dataDeduction, MAXRANGE_GRAPHIC);
        createGraphic(chartYears, WIDTH_GRAPHIC, HEIGHT_GRAPHIC, display.tabGraph, "Annuel", display.pan2, 0, 2, 5);
        createGraphic(chartMonths, WIDTH_GRAPHIC, HEIGHT_GRAPHIC, display.tabGraph, "Mensuel", display.pan2, 0, 2, 5);
        createGraphic(chartDeduction, WIDTH_GRAPHIC, HEIGHT_GRAPHIC, display.tabDeduction, "Déduction", display.pan3, 0, 2, 5);
    }

    private JFreeChart createChart (DefaultCategoryDataset dataset, Integer maxRange)
    {
        // Création du graphique à barres (Annuel)
        JFreeChart chart = ChartFactory.createBarChart(null,     /* Titre du graphique */
                                                       null,     /* Axe des abscisses */ 
                                                       null,     /* Axe des ordonnées */  
                  dataset, PlotOrientation.HORIZONTAL, true,     /*Légende */ 
                                                       true,     /*Info tooltips */ 
                                                     false);     /* URL */

        // Personnalisation du graphique 
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);

        // Définir la plage de l'axe des ordonnées
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, maxRange);
        return chart;
    }

    public ChartPanel createGraphic (JFreeChart freeChart, int width, int height, JTabbedPane jTabbedPane, 
                                     String txtTab, JPanel jPanel, int gridX, int gridY, int gridWidth)
    {
        ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new Dimension(width, height));
        jTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        jTabbedPane.addTab(txtTab, chartPanel);
        display.gbc.gridx = gridX;
        display.gbc.gridy = gridY;
        display.gbc.gridwidth = gridWidth;
        jPanel.add(jTabbedPane, display.gbc);
        return chartPanel;
    }

    // Initialisation des données graphiques
    public void clearGraph()
    {
        dataYears.clear();
        dataMonths.clear();
    }

    public void updateDatasets(Double[][][][][] data, String[] graph) 
    {
        for (int ii = 0; ii < graph.length; ii++) 
        {   
            for (int jj = 0; jj < CATEGORIES.length; jj++) 
            {   
                // Récupération de la valeur
                Double valeur = data[ii][ii][ii][ii][jj]; 
    
                if (valeur != null) 
                {
                    dataYears.addValue(valeur, CATEGORIES[jj], graph[ii]);
                    dataMonths.addValue(valeur, CATEGORIES[jj], graph[ii]);
                }
                else 
                {
                    // Vérifier si la clé existe déjà avant d'appeler getValue()
                    boolean keyExists = dataYears.getColumnIndex(graph[ii]) >= 0 &&
                                        dataYears.getRowIndex(CATEGORIES[jj]) >= 0;
    
                    // Si la clé n'existe pas, ajouter 0
                    if (!keyExists)  
                    {
                        dataYears.addValue(0.0, CATEGORIES[jj], graph[ii]);
                    }
                }
            }
        }
    }
}