package com.medails;

import javax.swing.SwingUtilities;

    /*********************************************************** 
                          DEMARAGE PROGRAMME
    ***********************************************************/

public class AppFacture
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
                ReadFile readFile = new ReadFile();
                Treatment treatment = new Treatment(display, graphic, readFile); 
            }
        });
    }
}

/*             ____________________________________________________________       
              || Enregistrement|Graphique|                                ||                                   
              ||                                                          ||
              || Facture                                                  ||
              ||                                                          ||
              ||      Année             Mois         Date de paiement     ||
              ||     [_(A1)_]>        [_(A2)_]>   [_______(A3)________]>  ||                     
              ||                                                          ||
              || Jours travaillées      TJM                               ||
              ||     [_(B1)_]         [_(B2)_]        [Calculer](B3)      ||
              ||                                                          ||
              ||       TTC               HT              TVA              ||
              ||     [_(C1)_]         [_(C2)_]         [_(C3)_]           ||
              ||                                                          ||
              || URSSAF                                                   ||
              ||                                                          ||
              ||   Montant taxe        Restant                            || 
              ||     [_(D1)_]         [_(D2)_]                            ||               
              ||                                                          ||
              || Liens                                                    ||
              ||                                                          ||
              ||        Facture            [Ouvrir](E1)   [Parcourir](E2) ||
              || [...] [____________________(F1)_____________________]>   ||
              ||       [____________________(G1)_____________________]>   ||
              ||                                                          ||
              ||        Déclaration        [Ouvrir](H1)   [Parcourir](H2) ||
              || [...] [____________________(I1)____________________]>    ||
              ||       [____________________(J1)____________________]>    ||
              ||                                                          ||
              ||             [Enrengistrer](K1)       [RAZ](K2)           ||
              ||__________________________________________________________||

               ____________________________________________________________  
              || Enregistrement|Graphique|                                || 
              ||       Annuel|Mensuel                                     ||
              ||   ___________________________________________________    ||
              ||  |                                                   |   ||
              ||  |                                                   |   || 
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   ||
              ||  |                                                   |   || 
              ||  |                                                   |   || 
              ||  |                                                   |   ||
              ||  |___________________________________________________|   ||
              ||                                                          ||
              ||   Facture                                                ||
              ||                                                          ||
              ||   Total TTC        Total HT                 Année        ||
              ||    [_(A1)_]        [_(A2)_]                [_(A3)_]>     ||
              ||                                                          ||
              ||   URSSAF                                                 ||
              ||                                                          ||
              ||   Total taxe   Total différence                          ||
              ||    [_(B1)_]        [_(B2)_]                 [RAZ](B3)    ||
              ||                                                          ||
              ||__________________________________________________________||    
              

 */