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
                Treatment1 treatment1 = new Treatment1(display, readFile); 
                Treatment2 treatment2 = new Treatment2(display, graphic, readFile); 
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
              ||                                                          ||
              || Jours travaillées      TJM                               ||
              ||     [_(B1)_]         [_(B2)_]        [Calculer](B3)      ||
              ||                                                          ||
              ||                                                          ||
              ||       TTC               HT              TVA              ||
              ||     [_(C1)_]         [_(C2)_]         [_(C3)_]           ||
              ||                                                          ||
              ||                                                          ||
              || URSSAF                                                   ||
              ||                                                          ||
              ||   Montant taxe       Bénéfices                           || 
              ||     [_(D1)_]         [_(D2)_]                            ||               
              ||                                                          ||
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
              ||       Annuel|Mensuel                                     ||
              ||                                                          ||
              ||      TTC     TVA     HT      URSSAF      Bénéfices       ||  
              ||     ■(A1)    ■(A2)  ■(A3)     ■(A4)        ■(A5)         ||
              ||                                                          ||
              ||   Facture                                  [_(B1)_]      ||
              ||                                                          ||
              ||   Total TTC        Total HT                  TVA         ||
              ||    [_(C1)_]        [_(C2)_]                [_(C3)_]      ||
              ||                                                          ||
              ||   URSSAF                                                 ||
              ||                                                          ||
              ||   Total taxe    Total bénéfices                          ||
              ||    [_(D1)_]        [_(D2)_]                 [RAZ](D3)    ||
              ||                                                          ||
              ||__________________________________________________________||    
              

 */