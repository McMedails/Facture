
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import javax.swing.*;
import java.awt.*;

public class BarChartPanel extends JPanel
{
    public BarChartPanel(CategoryDataset _dataset)
    {
        JFreeChart _chart = ChartFactory.createBarChart("", "", "", _dataset,
        PlotOrientation.HORIZONTAL, false, false, false);
    
        CategoryPlot _catPlot = _chart.getCategoryPlot();
        _catPlot.setDomainGridlinesVisible(true);
        _catPlot.setRangeGridlinesVisible(true);
        _catPlot.setDomainGridlinePaint(Color.BLACK);
        _catPlot.setRangeGridlinePaint(Color.BLACK);
         
         ChartPanel _chartPanel = new ChartPanel(_chart);
         _chartPanel.setPreferredSize(new Dimension(340,400));
         setLayout(new BorderLayout());
         add(_chartPanel, BorderLayout.CENTER);
    }

    private static CategoryDataset createDataset(String _chartTitle, double[] values)
    {
        DefaultCategoryDataset _dataset = new DefaultCategoryDataset();

        String _mounts[] = {"Janvier", "Février", "Mars", "Avril", 
                             "Mai", "Juin", "Juillet", "Août", "Septembre", 
                             "Octobre", "Novembre", "Décembre"};

        for (int ii = 0; ii < _mounts.length; ii++)
        {
            _dataset.addValue(values[ii], _chartTitle, _mounts[ii]);
        }                 
        return _dataset;
    }
}
