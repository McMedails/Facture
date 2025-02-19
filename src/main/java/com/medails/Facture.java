package com.medails;

import javax.swing.SwingUtilities;

public class Facture
{    
                         
    public static void main (String[]args)
    {
        // Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Display display = new Display();
                Graphic graphic = new Graphic(display);
                display.setGraphic(graphic);
                Treatment treatment = new Treatment(display, graphic);
            }
        });
    }
}
