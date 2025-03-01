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
               
    private final String[] CATEGORIES = {"TTC", "TVA", "HT", "URSSAF", "Bénéfices"};

    private final int MAXRANGE = 10000;
    private DefaultCategoryDataset dataYears = new DefaultCategoryDataset();
    private DefaultCategoryDataset dataMonths = new DefaultCategoryDataset();
    private JFreeChart chartYears;
    private JFreeChart chartMonths;
    private ChartPanel chartPanelYears;
    private ChartPanel chartPanelMonths;

    /************* Déclarations Classes ****************/
    private Display display;
    private Treatment treatment; 
    
    /*********** Constructeur ***************/
    public Graphic (Display display)
    {
        this.display = display;
        chartYears = createChart(dataYears);
        chartMonths = createChart(dataMonths);
        initChartPanel();
        createGraphic(display.tabGraph, display.pan2, display.gbc);
    }

    private JFreeChart createChart (DefaultCategoryDataset dataset)
    {
        // Création du graphique à barres (Annuel)
        JFreeChart chart = ChartFactory.createBarChart(null,                 /* Titre du graphique */
                                                       null,                /* Axe des abscisses */ 
                                                       "Résultats",        /* Axe des ordonnées */  
                    dataset, PlotOrientation.HORIZONTAL, true,            /*Légende */ 
                                                           true,         /*Info tooltips */ 
                                                            false);     /* URL */

        // Personnalisation du graphique (Annuel)
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, MAXRANGE);
        return chart;
    }

    public ChartPanel[] initChartPanel()
    {
        chartPanelYears = new ChartPanel(chartYears);
        chartPanelMonths = new ChartPanel(chartMonths);
        chartPanelYears.setPreferredSize(new Dimension(340,400));
        chartPanelMonths.setPreferredSize(new Dimension(340,400)); 
        return new ChartPanel[] {chartPanelYears, chartPanelMonths};
    }

    public void createGraphic (JTabbedPane jTabbedPane, JPanel jPanel, GridBagConstraints gbc)
    {
        ChartPanel[] panels = initChartPanel();
        display.tabGraph.setTabPlacement(JTabbedPane.BOTTOM);
        display.tabGraph.removeAll();
        chartPanelYears = panels[0];
        chartPanelMonths = panels[1];
        display.tabGraph.addTab("Annuel", chartPanelYears);
        display.tabGraph.addTab("Mensuel", chartPanelMonths);
        display.gbc.gridx = 0;
        display.gbc.gridy = 0;
        display.gbc.gridwidth = 3;
        display.pan2.add(display.tabGraph, display.gbc);
    }

    // Initialisation des données graphiques
    public void clearGraph()
    {
        dataYears.clear();
        dataMonths.clear();
    }

    public void refreshGraph()
    {
        chartPanelYears.revalidate();
        chartPanelYears.repaint();
        chartPanelMonths.revalidate();
        chartPanelMonths.repaint();      
    }

    public void updateDatasets(Double[][][][][] data) 
    {
        for (int ii = 0; ii < GRAPHMONTHS.length; ii++) 
        {   
            for (int jj = 0; jj < CATEGORIES.length; jj++) 
            {   
                // Récupération de la valeur
                Double valeur = data[ii][ii][ii][ii][jj]; 
    
                if (valeur != null) 
                {
                    dataYears.addValue(valeur, CATEGORIES[jj], GRAPHMONTHS[ii]);
                    dataMonths.addValue(valeur, CATEGORIES[jj], GRAPHMONTHS[ii]);
                }
                else 
                {
                    // Vérifier si la clé existe déjà avant d'appeler getValue()
                    boolean keyExists = dataYears.getColumnIndex(GRAPHMONTHS[ii]) >= 0 &&
                                        dataYears.getRowIndex(CATEGORIES[jj]) >= 0;
    
                    // Si la clé n'existe pas, ajouter 0
                    if (!keyExists)  
                    {
                        dataYears.addValue(0.0, CATEGORIES[jj], GRAPHMONTHS[ii]);
                    }
                }
            }
        }
    }
}