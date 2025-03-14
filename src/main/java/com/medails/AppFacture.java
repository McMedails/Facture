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
                ReadWrite readwriteMain = new ReadWrite(ReadWrite.MAINFILENAME);
                ReadWrite readwriteOther = new ReadWrite(ReadWrite.OTHERFILENAME);
                Treatment1 treatment1 = new Treatment1(display, readwriteMain);  
                Treatment2 treatment2 = new Treatment2(display, graphic, readwriteMain, treatment1); 
                Treatment3 treatment3 = new Treatment3(display, graphic, readwriteMain, readwriteOther, treatment1, treatment2); 
            }
        });
    }
}

/*             ____________________________________________________________       
              || Enregistrement|Graphique|Déduction                       ||                                   
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
              || Enregistrement|Graphique|Déduction                       ||
              ||                                                          ||
              ||   Annuel/Mensuel     <>-----------------------------     ||
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
              
               ____________________________________________________________  
              || Enregistrement|Graphique|Déduction                       || 
              ||                                                          ||
              ||     Décénie          <>-----------------------------     || 
              ||     Déductible       <>-----------------------------     || 
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
              ||       Décénie|Déductible                                 ||
              ||                                                          ||
              ||      Date d'achat             Déduction     Année        ||
              ||   [_______(A1)________]>        ■(A2)      [_(A3)_]>     ||  
              ||                                                          ||
              ||                                                          ||
              ||      TTC              HT                     TVA         ||
              ||    [_(B1)_]        [_(B2)_]                [_(B3)_]      ||
              ||                                                          ||
              ||    Déduction                                             ||
              ||                                                          ||
              ||                           [Ouvrir](C1)   [Parcourir](C2) ||
              || [...] [____________________(D1)_____________________]>   ||
              ||       [____________________(E1)_____________________]>   ||
              ||                                                          ||
              ||             [Enrengistrer](F1)       [RAZ](F2)           ||
              ||__________________________________________________________||
 */