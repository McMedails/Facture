package com.medails;

import java.awt.Color;

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
    private ChartPanel chartPanelYears;
    private ChartPanel chartPanelMonths;

    public ChartPanel getchartPanelYears()
    {
        return this.chartPanelYears;
    }

    public ChartPanel getchartPanelMonths()
    {
        return this.chartPanelMonths;
    }

    public Graphic()
    {
        DefaultCategoryDataset _dataYears = new DefaultCategoryDataset();
        DefaultCategoryDataset _dataMonths = new DefaultCategoryDataset();

        // Réinitialisation des tableaux
        _dataYears.clear();
        _dataMonths.clear();

        if (Treatment._startGraphic)
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

            // Création du graphique à barres (Annuel)
            JFreeChart chartYears = ChartFactory.createBarChart(null,                    /* Titre du graphique */
                                                                null,                   /* Axe des abscisses */ 
                                                                "Résultats",           /* Axe des ordonnées */  
                                _dataYears, PlotOrientation.HORIZONTAL, true,         /*Légende */ 
                                                                        true,        /*Info tooltips */ 
                                                                        false);     /* URL */
        
            // Création du graphique à barres (Mensuel)  
            JFreeChart chartMonths = ChartFactory.createBarChart(null,                   /* Titre du graphique */
                                                                 null,                  /* Axe des abscisses */ 
                                                                 "Résultats",          /* Axe des ordonnées */  
                                _dataMonths, PlotOrientation.HORIZONTAL, true,        /*Légende */ 
                                                                         true,       /*Info tooltips */ 
                                                                         false);    /* URL */
        
            // Personnalisation du graphique (Annuel)
            CategoryPlot catPlotYears = chartYears.getCategoryPlot();
            catPlotYears.setDomainGridlinesVisible(true);
            catPlotYears.setRangeGridlinesVisible(true);
            catPlotYears.setDomainGridlinePaint(Color.BLACK);
            catPlotYears.setRangeGridlinePaint(Color.BLACK);
            NumberAxis rangeAxisYears = (NumberAxis) catPlotYears.getRangeAxis();
            rangeAxisYears.setRange(0, 10000);

            // Personnalisation du graphique (Mensuel)
            CategoryPlot catPlotMonths = chartMonths.getCategoryPlot();
            catPlotMonths.setDomainGridlinesVisible(true);
            catPlotMonths.setRangeGridlinesVisible(true);
            catPlotMonths.setDomainGridlinePaint(Color.BLACK);
            catPlotMonths.setRangeGridlinePaint(Color.BLACK);
            NumberAxis rangeAxisMonths = (NumberAxis) catPlotMonths.getRangeAxis();
            rangeAxisMonths.setRange(0, 10000);

            this.chartPanelYears = new ChartPanel(chartYears);
            this.chartPanelMonths = new ChartPanel(chartMonths);
        }
    }
}