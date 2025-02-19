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
    private DefaultCategoryDataset _dataYears = new DefaultCategoryDataset();
    private DefaultCategoryDataset _dataMonths = new DefaultCategoryDataset();
    private JFreeChart chartYears;
    private JFreeChart chartMonths;

    private Display display;

    public Graphic(Display display)
    {
        System.out.println("Ordre de traitement : 2");

        this.display = display;
        chartYears = createChart(_dataYears, "Annuel");
        chartMonths = createChart(_dataMonths, "Mensuel");
    }

    public void updateDatasets(Double[][][][] yearData, Double [][][][] monthsData)
    {
        // Réinitialisation des tableaux
        _dataYears.clear();
        _dataMonths.clear();

        if (yearData != null && monthsData != null)
        {
            // Remplir le dataset avec les données des mois et des totaux HT
            for (int ii = 0; ii < Treatment._graphMonths.length; ii++)
            { 
                /************************* ANNUEL **************************/

                // Ajouter les valeurs (totaux TTC) au dataset pour chaque mois 
                if (Treatment._graphTotauxYears[ii][ii][ii][0] != null)
                {
                    _dataYears.addValue(Treatment._graphTotauxYears[ii][ii][ii][0], "TTC", Treatment._graphMonths[ii]);
                }
                else
                {
                    Treatment._graphTotauxYears[ii][ii][ii][0] = 0.0;
                }

                // Ajouter les valeurs (totaux HT) au dataset pour chaque mois
                if (Treatment._graphTotauxYears[ii][ii][ii][1] != null)
                {
                    _dataYears.addValue(Treatment._graphTotauxYears[ii][ii][ii][1], "HT", Treatment._graphMonths[ii]);
                }
                else
                {
                    Treatment._graphTotauxYears[ii][ii][ii][1] = 0.0;
                }

                // Ajouter les valeurs (restant) au dataset pour chaque mois
                if (Treatment._graphTotauxYears[ii][ii][ii][2] != null)
                {
                    _dataYears.addValue(Treatment._graphTotauxYears[ii][ii][ii][2], "Restant", Treatment._graphMonths[ii]);
                }
                else
                {
                    Treatment._graphTotauxYears[ii][ii][ii][2] = 0.0;
                }

                // Ajouter les valeurs (Urssaf) au dataset pour chaque mois
                if (Treatment._graphTotauxYears[ii][ii][ii][3] != null)
                {
                    _dataYears.addValue(Treatment._graphTotauxYears[ii][ii][ii][3], "URSSAF", Treatment._graphMonths[ii]);
                }
                else
                {
                    Treatment._graphTotauxYears[ii][ii][ii][3] = 0.0;
                }

                /************************* MENSUEL **************************/ 

                // Ajouter les valeurs (totaux TTC) au dataset pour chaque mois 
                if (Treatment._graphTotauxMonths[ii][ii][ii][0] != null)
                {
                    _dataMonths.addValue(Treatment._graphTotauxMonths[ii][ii][ii][0], "TTC", Treatment._graphMonths[ii]);
                }

                // Ajouter les valeurs (totaux HT) au dataset pour chaque mois 
                if (Treatment._graphTotauxMonths[ii][ii][ii][1] != null)
                {
                    _dataMonths.addValue(Treatment._graphTotauxMonths[ii][ii][ii][1], "HT", Treatment._graphMonths[ii]); 
                }

                // Ajouter les valeurs (restant) au dataset pour chaque mois 
                if (Treatment._graphTotauxMonths[ii][ii][ii][2] != null)
                {
                    _dataMonths.addValue(Treatment._graphTotauxMonths[ii][ii][ii][2], "Restant", Treatment._graphMonths[ii]);
                }

                // Ajouter les valeurs (Urssaf) au dataset pour chaque mois 
                if (Treatment._graphTotauxMonths[ii][ii][ii][3] != null)
                {
                    _dataMonths.addValue(Treatment._graphTotauxMonths[ii][ii][ii][3], "URSSAF", Treatment._graphMonths[ii]);
                }
            }
        }
        // Mettre à jour les graphiques avec les nouveaux datasets
        chartYears.getCategoryPlot().setDataset(_dataYears);
        chartMonths.getCategoryPlot().setDataset(_dataMonths);
    }
        
    private JFreeChart createChart (DefaultCategoryDataset dataset, String title)
    {
        // Création du graphique à barres (Annuel)
            JFreeChart chart = ChartFactory.createBarChart(null,                 /* Titre du graphique */
                                                        null,                   /* Axe des abscisses */ 
                                                        "Résultats",           /* Axe des ordonnées */  
                        dataset, PlotOrientation.HORIZONTAL, true,            /*Légende */ 
                                                                true,        /*Info tooltips */ 
                                                                false);     /* URL */

        // Personnalisation du graphique (Annuel)
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 10000);
        return chart;
    }

    public ChartPanel[] initChartPanel()
    {
        ChartPanel chartPanelYears = new ChartPanel(chartYears);
        ChartPanel chartPanelMonths = new ChartPanel(chartMonths);
        chartPanelYears.setPreferredSize(new Dimension(340,400));
        chartPanelMonths.setPreferredSize(new Dimension(340,400)); 
        return new ChartPanel[] {chartPanelYears, chartPanelMonths};
    }

    public void initGraphic (JTabbedPane tabGraph, JPanel pan2, GridBagConstraints gbc)
    {
        ChartPanel[] panels = initChartPanel();
        tabGraph.removeAll();
        ChartPanel chartPanelYears = panels[0];
        ChartPanel chartPanelMonths = panels[1];
        tabGraph.addTab("Annuel", chartPanelYears);
        tabGraph.addTab("Mensuel", chartPanelMonths);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        pan2.add(tabGraph, gbc);
    }
}
