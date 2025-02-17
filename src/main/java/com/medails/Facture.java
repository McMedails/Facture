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
                new Display();
                new Treatment();
                new Graphic();
            }
        });
    }
}
