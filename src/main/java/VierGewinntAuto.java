

import com.sun.jdi.IntegerValue;

import java.io.*;// diese Zeile muss ganz am Anfang des files stehen
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Integer.min;
import static java.lang.Math.max;
//import static sun.swing.*;



public class VierGewinntAuto { // Anfang class VierGewinnt

    /**
     * ******************************************************** In der Prozedur
     * (Methode ohne Rueckgabewert -viod) <b>hilfe()</b> ist nur eine einfache
     * System.out.println() Anweisung, die den Spieler Hilfestellungen zum Spiel
     * anzeigt.
     *
     ******************************************************** */

    /**
     * Scannt Spielfeld nach spielbaren Feldern und gibt ihnen eine Bewertung
     *
     * @param spielfeld
     */
    public static void scannen(char[][] spielfeld) {
        Set<String> spielmoeglichkeiten = scannen2(spielfeld);

        for (String s : spielmoeglichkeiten) {
            // max Distanz von jeder Spielmöglichkeit bestimmen und im String merken
            s = maxDistanz2(s);
            // Bewertung des Spielzuges anhand Gruppenbildung bestimmen und im String merken
            s = maxAllerRichtungenUndFaktor(s, spielfeld);
            bewertung_aus_spielmoeglichkeit_ausgeben(s);
            // Bewertung ausgeben
            // System.out.println("" + s.charAt(0) + s.charAt(1) + " hat die Bewertung: " + s.charAt(s.length() - 1));

        }

        System.out.println(spielmoeglichkeiten);

    }

    public static Set<String> scannen2(char[][] spielfeld) {
        Set<String> spielmoeglichkeiten = new HashSet<>();
        // spalte von links nach rechts
        // Zeile von unten nach oben
        for (int spalte = 0; spalte <= 6; spalte++) {
            for (int zeile = 5; zeile >= 0; zeile--) {
                {
                    // erstes gefundenes leeres Spielfeld von unten nach oben == Spielmöglichkeit
                    // insgesamt 7
                    if (spielfeld[zeile][spalte] == ' ') {
                        String xy = "" + zeile + spalte;
                        spielmoeglichkeiten.add(xy);
                        break;
                    }
                }
            }
        }
        return spielmoeglichkeiten;
    }



    /**
     * Berechnet maximale Distanz aller Richtungen einer Spielmöglichkeit
     *
     * @param spielmoeglichkeit
     * @return gibt spielmoeglichkeit mit maximaler Distanz aller Richtungen zurück
     */
    public static String maxDistanz2(String spielmoeglichkeit) {
        int r = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0))) + 1;
        int s = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1))) + 1;

        // ternärer Ausdruck für Formel, wenn Formel > 3 dann Distanz = 3 setzen für die Richtung, da 4er Gruppe max
        int oben = r - 1 > 3 ? 3 : (r - 1);
        int rechts = 7 - s > 3 ? 3 : 7 - s;
        int unten = 5 - (r - 1) > 3 ? 3 : 5 - (r - 1);
        int links = s - 1 > 3 ? 3 : s - 1;
        int links_unten = min(links,unten);
        int rechts_oben = min(rechts, oben);
        int links_oben = min(links, oben);
        int rechts_unten = min(rechts, unten);

        //(Zeile(0), Spalte(1), Vertikal(2,3), Horizontal(4,5), AbDg(6,7), AufDg(8,9))
        spielmoeglichkeit += "" + oben + unten + links + rechts + links_oben + rechts_unten + links_unten + rechts_oben;

        return spielmoeglichkeit;

    }

    /**
     * Berechnet vertikale Nachbarn für bis zu 4 Frames und bestimmt daraus das Max
     * als Bewertung für eine Spielmöglichkeit eines Spielfeldes
     *
     * @param spielmoeglichkeit
     * @param spielfeld
     * @return Spielmoeglichkeit mit Bewertung wird zurückgegeben
     */
    public static int maxSpStVertikal(String spielmoeglichkeit, char[][] spielfeld) {
        int[] sqeq = SqEqVertikal(spielmoeglichkeit);
        int sq = sqeq[0];
        int eq = sqeq[1];

//        for (int spalte = 0; spalte <= 6; spalte++){
//            for (int zeile = 5; zeile >= 0; zeile--)
//                {
//                    if (spielfeld[zeile][spalte] == 'X') {
//                        System.out.println("" + zeile+spalte+spielfeld[zeile][spalte]);
//                    }
//                }
//            }


        // max[] beinhaltet die Anzahl der Nachbarn aller 4er-Frames (max 4 Frames)
        int max[] = new int[4];
        // zähler für Gruppenanzahl bzw Nachbarn
        int counter = 0;
        // gesamte Suchdistanz in Richtung und Subrichtung -3+1 um richtige anzahl an Frames zu bestimmen
        int z = (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(3))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(2))) - 3) + 1;
        //  System.out.println(z);
        //  System.out.println(spielmoeglichkeit);
        //  System.out.println(sq);
        // Frames durchgehen
        for (int i = 0; i < z; i++) {
            // vom Startingsquare 3 Felder runter nach Nachbarn suchen
            for (int j = sq; j <= sq + 3; j++) {
                //      System.out.println("j = " + j);
                //     System.out.println(spielmoeglichkeit.charAt(1));
                // Nachbar des eigenen Spielsteins gefunden -> counter erhöhen
                if ((spielfeld[j][Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1)))]) == 'X') {
                    counter++;
                    // Nachbar des gegnerischen Spielsteins gefunden -> counter auf 0 und abbrechen, da keine 4er Gruppe mehr möglich
                } else if ((spielfeld[j][Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1)))]) == 'O') {
                    counter = 0;
                    max[i] = counter;
                    break;
                }

            }
            // neues Frame eintreten mit Inkrementierung von sq
            sq++;
            //    System.out.println("Hier muss vier mal");
            // Anzahl der Nachbarn des Frames in max merken
            max[i] = counter;
            // Nachbarzähler reset für nächsten Frame
            counter = 0;

        }
        //   System.out.println(Arrays.toString(max));
        // Maximum von allen Frames bestimmen
        int maxD = max[0];
        for (int k = 0; k < max.length; k++) {
            if (maxD<max[k]){
                maxD = max[k];} }


        /* TODO: Bewertung anhand aller Richtungen -> kann man auch einfach alle in max reinstecken und Maximum
          TODO: von bestimmen oder bei gesplitteten Methoden alle Maximen sammeln und Maximum davon bestimmen */

        return maxD;
    }


    public static int maxSpStHorizontal(String spielmoeglichkeit, char[][] spielfeld) {
        int[] sqeq = SqEqHorizontal(spielmoeglichkeit);
        int sq = sqeq[0];
        int eq = sqeq[1];


        // max[] beinhaltet die Anzahl der Nachbarn aller 4er-Frames (max 4 Frames)
        int max[] = new int[4];
        // zähler für Gruppenanzahl bzw Nachbarn
        int counter = 0;
        // gesamte Suchdistanz in Richtung und Subrichtung -3+1 um richtige anzahl an Frames zu bestimmen
        int z = (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(4))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(5))) - 3) + 1;
        // Frames durchgehen
        for (int i = 0; i < z; i++) {
            // vom Startingsquare 3 Felder runter nach Nachbarn suchen
            for (int j = sq; j <= sq + 3; j++) {
                //           System.out.println("j = " + j);
                //          System.out.println(spielmoeglichkeit.charAt(1));
                // Nachbar des eigenen Spielsteins gefunden -> counter erhöhen
                if ((spielfeld[Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0)))][j]) == 'X') {
                    counter++;
                    // Nachbar des gegnerischen Spielsteins gefunden -> counter auf 0 und abbrechen, da keine 4er Gruppe mehr möglich
                } else if ((spielfeld[Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0)))][j]) == 'O') {
                    counter = 0;
                    max[i] = counter;
                    break;
                }

            }
            // neues Frame eintreten mit Inkrementierung von sq
            sq++;
            //      System.out.println("Hier muss vier mal");
            // Anzahl der Nachbarn des Frames in max merken
            max[i] = counter;
            // Nachbarzähler reset für nächsten Frame
            counter = 0;

        }
        //    System.out.println(Arrays.toString(max));
        // Maximum von allen Frames bestimmen
        int maxD = max[0];
        for (int k = 0; k < max.length; k++) {
            if (maxD<max[k]){
                maxD = max[k];} }


        /* TODO: Bewertung anhand aller Richtungen -> kann man auch einfach alle in max reinstecken und Maximum
          TODO: von bestimmen oder bei gesplitteten Methoden alle Maximen sammeln und Maximum davon bestimmen */

        return maxD;
    }

    public static int maxSpStDiagonalAb(String spielmoeglichkeit, char[][] spielfeld) {
        int[] sqeq = SqEqDiagonalAb(spielmoeglichkeit);
        int sq1 = sqeq[0];
        int sq2 = sqeq[1];

        // max[] beinhaltet die Anzahl der Nachbarn aller 4er-Frames (max 4 Frames)
        int max[] = new int[4];
        // zähler für Gruppenanzahl bzw Nachbarn
        int counter = 0;
        // gesamte Suchdistanz in Richtung und Subrichtung -3+1 um richtige anzahl an Frames zu bestimmen
        int z = (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(6))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(7))) - 3) + 1;
        //   System.out.println(z);
        // Frames durchgehen
        for (int e = 0; e < z; e++) {

            int i = sq1;
            int j = sq2;
            while (i <= sq1+3 && j <= sq2+3) {
                // Nachbar des eigenen Spielsteins gefunden -> counter erhöhen
                if ((spielfeld[i][j]) == 'X') {
                    counter++;
                    // Nachbar des gegnerischen Spielsteins gefunden -> counter auf 0 und abbrechen, da keine 4er Gruppe mehr möglich
                } else if ((spielfeld[i][j]) == 'O') {
                    counter = 0;
                    max[e] = counter;
                    break;
                }
                i++;
                j++;
            }

            // neues Frame eintreten mit Inkrementierung von sq
            sq1++;
            sq2++;
            //   System.out.println("Hier muss vier mal");
            // Anzahl der Nachbarn des Frames in max merken
            max[e] = counter;
            // Nachbarzähler reset für nächsten Frame
            counter = 0;
        }

        //  System.out.println(Arrays.toString(max));
        // Maximum von allen Frames bestimmen
        int maxD = max[0];
        for (int k = 0; k < max.length; k++) {
            if (maxD<max[k]){
                maxD = max[k];} }
        /* TODO: Bewertung anhand aller Richtungen -> kann man auch einfach alle in max reinstecken und Maximum
          TODO: von bestimmen oder bei gesplitteten Methoden alle Maximen sammeln und Maximum davon bestimmen */

        return maxD;
    }

    public static int maxSpStDiagonalAuf(String spielmoeglichkeit, char[][] spielfeld) {
        int[] sqeq = SqEqDiagonalAuf(spielmoeglichkeit);
        int sq1 = sqeq[0];
        int sq2 = sqeq[1];

        // max[] beinhaltet die Anzahl der Nachbarn aller 4er-Frames (max 4 Frames)
        int max[] = new int[4];
        // zähler für Gruppenanzahl bzw Nachbarn
        int counter = 0;
        // gesamte Suchdistanz in Richtung und Subrichtung -3+1 um richtige anzahl an Frames zu bestimmen
        int z = (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(8))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(9))) - 3) + 1;
        // System.out.println(z);
        // Frames durchgehen
        for (int e = 0; e < z; e++) {

            int i = sq1;
            int j = sq2;
            while (i <= sq1+3 && j <= sq2+3) {
                // Nachbar des eigenen Spielsteins gefunden -> counter erhöhen
                if ((spielfeld[i][j]) == 'X') {
                    counter++;
                    // Nachbar des gegnerischen Spielsteins gefunden -> counter auf 0 und abbrechen, da keine 4er Gruppe mehr möglich
                } else if ((spielfeld[i][j]) == 'O') {
                    counter = 0;
                    max[e] = counter;
                    break;
                }
                i--;
                j++;
            }

            // neues Frame eintreten mit Inkrementierung von sq
            sq1--;
            sq2++;
            //  System.out.println("Hier muss vier mal");
            // Anzahl der Nachbarn des Frames in max merken
            max[e] = counter;
            // Nachbarzähler reset für nächsten Frame
            counter = 0;
        }

        // System.out.println(Arrays.toString(max));
        // Maximum von allen Frames bestimmen
        int maxD = max[0];
        for (int k = 0; k < max.length; k++) {
            if (maxD<max[k]){
                maxD = max[k];} }
        /* TODO: Bewertung anhand aller Richtungen -> kann man auch einfach alle in max reinstecken und Maximum
          TODO: von bestimmen oder bei gesplitteten Methoden alle Maximen sammeln und Maximum davon bestimmen */

        return maxD;
    }

    public static int[] SqEqVertikal(String spielmoeglichkeit) {
        int[] SqEq = new int[2];
        // Zeile - unten für Startsquare
        SqEq[0] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0))) - Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(2)));
        // Zeile + oben für Endsquare (angrenzender Square)
        SqEq[1] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(3)));
        return SqEq;
    }

    public static int[] SqEqHorizontal(String spielmoeglichkeit) {
        int[] SqEq = new int[2];
        SqEq[0] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1))) - Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(4)));
        SqEq[1] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(5)));
        return SqEq;
    }

    public static int[] SqEqDiagonalAb(String spielmoeglichkeit) {
        int[] SqEq = new int[2];
        SqEq[0] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0))) - Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(6)));
        SqEq[1] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1))) - Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(6)));
        return SqEq;
    }

    public static int[] SqEqDiagonalAuf(String spielmoeglichkeit) {
        int[] SqEq = new int[2];
        SqEq[0] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0))) + Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(8)));
        SqEq[1] = Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1))) - Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(8)));
        return SqEq;
    }



    public static String maxAllerRichtungenUndFaktor(String spielmoeglichkeit, char[][] spielfeld) {

        int maximum = max(max(maxSpStVertikal(spielmoeglichkeit,spielfeld),maxSpStHorizontal(spielmoeglichkeit,spielfeld))
                ,max(maxSpStDiagonalAuf(spielmoeglichkeit,spielfeld), maxSpStDiagonalAb(spielmoeglichkeit,spielfeld)));

        switch (maximum+1){
            case 1:
                maximum = 100;
                break;
            case 2:
                maximum = 250;
                break;
            case 3:
                maximum = 500;
                break;
            case 4:
                maximum = 1000;
                break;
        }


// Faktor 3 - 1,0 ; 2,4 - 0,95 ; 1,5 - 0,90 ; 0,6 - 0,85
        switch (spielmoeglichkeit.charAt(1)){
            case '2','4':
                maximum *= 0.95;
                break;
            case '1','5':
                maximum *= 0.9;
                break;
            case '0','6':
                maximum *= 0.85;
                break;
        }

        spielmoeglichkeit += ":" + maximum;
        return spielmoeglichkeit;
    }

    public static String[] bewertung_aus_spielmoeglichkeit_ausgeben(String spielmoeglichkeit) {
        String[] st = spielmoeglichkeit.split(":");
        System.out.println("Feld[" + (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(0)))+1) + ","+ (Integer_parseInt(String.valueOf(spielmoeglichkeit.charAt(1)))+1) +
                "] hat die Bewertung: " + st[1]);
        return st;
    }


    public static void hilfe() {

        System.out
                .println("HILFE"
                        + "\n"
                        + "Vier Gewinnt - Spielbeschreibung und Spielregeln"
                        + "\n"
                        + "Spielbeschreibung und Reglen:"
                        + "\n"
                        + "Das Spielbrett besteht aus sieben Spalten (senkrecht) und sechs Reihen (waagerecht). "
                        + "\n"
                        + "Jeder Spieler besitzt 21 gleichfarbige Spielsteine (hier: Spieler 1 kenntlich durch X und Spieler 2 durch O)."
                        + "\n"
                        + " Beide Spieler lassen abwechselnd  Spielsteine  in eine Spalte fallen (durch Eingabe der gewuenschten Spalte, Bsp. 3Ã¬+ Enter), "
                        + "\n"
                        + "dieser besetzt den untersten freien Platz der Spalte. Gewinner ist der Spieler, der es als erster schafft, "
                        + "\n"
                        + "vier seiner Spielsteine waagerecht, senkrecht oder diagonal in eine Linie zu bringen. Das Spiel endet unentschieden, "
                        + "\n"
                        + "wenn das Spielbrett komplett gefuellt ist, ohne dass ein Spieler eine Viererlinie gebildet hat.");

    }

    public static void ueberschrift(int n) { // Anfang ueberschrift()

        // Schleife, die Zahlen von 1 - n als einzeilig, mit Tabulator getrennt,
        // ausgibt
        for (int i = 1; i <= n; i++) {

            System.out.print(i + "   ");

        }
        System.out.println(); // Leerzeile einfuegen

    } // Ende ueberschrift()

    /**
     * ******************************************************** Innerhalb dieser
     * Methode <b>spielbrettleer</b> wird das leere Spielbrett erzeugt, mit
     * Hilfe eines 2 dim. Arrays (spielfeld[][]) und samt "Geruest" dargestellt.
     * Dies wird innerhalb 2 ineinander geschachtelte for-Schleife "entworfen".
     *
     * @param
     *            name gibt den Spielernamen an, z.B. Spieler 1 char
     *            spielsteinchen zeigt den passenden Spielstein zum Spieler an
     *            (X oder O) int i gibt die moeglichen Spielzuege (i < 43)
     * @return gibt mËglichen Spielzuege zurueck
     ******************************************************** */

    public static int spielablauf(char spielfeld[][], String name,
                                  char spielsteinchen, int i) {

        String spalte = spielereingabe(); // Aufruf der Funktion
        // spielereingabe()
        int spaltennummer = gueltigeEingabe(spalte); // Aufruf der Funktion
        // gueltigeEingabe
        // String spalte_2 = spielereingabe(); // Aufruf der Funktion
        // spielereingabe()
        volleSpalte(spaltennummer, spielfeld); // Aufruf der Funktion
        // volleSpalte()
        spielfeld = spielzug(spielfeld, spaltennummer, spielsteinchen); // Aufruf
        // der
        // Funktion
        // spielzug()
        i = gewonnen(spielfeld, spielsteinchen, name, i);
        spielbrettVoll(spielfeld); // Aufruf der Prozedur spielbrettVoll()

        return i;

    }
    public static int spielablauf2(char spielfeld[][], String name,
                                   char spielsteinchen, int i) {
        String spalte = null;
        int max =0;
        if (i == 0) {
            Random random = new Random();
            spalte = String.valueOf(random.nextInt((6)+1)+1);
        }
        else {
            int counter = 0;
            for (String s : scannen2(spielfeld)) {
                // max Distanz von jeder Spielmöglichkeit bestimmen und im String merken
                s = maxDistanz2(s);
                // Bewertung des Spielzuges anhand Gruppenbildung bestimmen und im String merken
                s = maxAllerRichtungenUndFaktor(s, spielfeld);
                bewertung_aus_spielmoeglichkeit_ausgeben(s);
                // Bewertung ausgeben
                // System.out.println("" + s.charAt(0) + s.charAt(1) + " hat die Bewertung: " + s.charAt(s.length() - 1));
                max = max(max,Integer_parseInt(s.split(":")[1]));
                if ((s.split(":")[1].equals(String.valueOf(max)))&& counter == 0) {
                    spalte = String.valueOf(Integer.valueOf(String.valueOf(s.charAt(1)))+1);
                    counter++;
                } else if ((s.split(":")[1].equals(String.valueOf(max)))) {
                    Random random = new Random();
                    int[] zeroORone= new int[2];
                    zeroORone[0] = Integer.parseInt(spalte);
                    zeroORone[1] = Integer.valueOf(String.valueOf(s.charAt(1)))+1;

                    spalte = String.valueOf(zeroORone[random.nextInt(2)]);
                }

            }


        }
        // spielereingabe()
        //int spaltennummer = gueltigeEingabe(spalte); // Aufruf der Funktion
        // gueltigeEingabe
        // String spalte_2 = spielereingabe(); // Aufruf der Funktion
        // spielereingabe()
        //volleSpalte(spaltennummer, spielfeld); // Aufruf der Funktion
        // volleSpalte()
        if (spalte != null) {
            spielfeld = spielzug(spielfeld, Integer.parseInt(spalte)-1, spielsteinchen);} // Aufruf
        // der
        // Funktion
        // spielzug()
        i = gewonnen(spielfeld, spielsteinchen, name, i);
        spielbrettVoll(spielfeld); // Aufruf der Prozedur spielbrettVoll()

        return i;

    }

    public static char[][] spielbrettleer(char spielfeld[][]) { // Anfang
        // spielbrettleer()

        ueberschrift(7);

        for (char y = 0; y < spielfeld.length; y++) // y = Zeilen
        {
            for (char x = 0; x < spielfeld[y].length; x++) // x = Spalten
            {
                spielfeld[y][x] = (' ');
                System.out.print(spielfeld[y][x] + " | "); // zwischen den
                // Spalten
                // erscheinen | als
                // optische
                // Trennung, so zu
                // sagen als Geruest
            }
            System.out.println(""); // macht einen Zeilenumbruch nach jeder
            // Zeile, damit das gewuenschte Format 6x7
            // erscheint

        }
        return spielfeld;

    } // ende spielbrettleer()

    /**
     * ******************************************************** Die Hilfsmethode
     * <b>spielereingabe()</b> liest eine Zeile von der Konsole ein
     *
     * @return gibt eingelesene Zeile als String zurueck
     ******************************************************** */

    public static String spielereingabe() { // anfang Funktion spielereingabe()

        String eingabe = ""; // die eingegebenen Zeichen werden als String
        // eingelesen und als Integer zurueck gegeben
        try {
            BufferedReader Tast = new BufferedReader(new InputStreamReader(
                    System.in));
            eingabe = Tast.readLine();
        } catch (Exception e) {

        }
        return eingabe;

    } // ende der Funktion spielereingabe()

    /**
     * ******************************************************** Die Hilfsmethode
     * <b>Integer_parseInt()</b> wandelt einen String in eine ganze Zahl um
     *
     * @param
     *            , der in ein int umgewandelt werden soll
     * @return Zahlenwert des Strings s, wenn gueltig, sonst -1111
     *
     *         Wenn der String keine ganze Zahl ist, z.B. Ã12aÃ¬ oder Ã12,2Ã¬
     *         oder Ã12.2Ã¬ wird der Zahlenwert -1111 zurueckgegeben
     ******************************************************** */

    public static int Integer_parseInt(String eingabe) { // Anfang
        // Integer_parseInt()

        // int asInteger(String eingabe)

        int i;
        try {
            i = Integer.parseInt(eingabe);
        } catch (Exception e) {
            i = -1111;
        }
        return i;
    } // ende Integer_parseInt()

    /**
     * ******************************************************** In der Methode
     * <b>spielzug()</b> wird die vom Spieler eingegebene Spaltennummer
     * ausgewertet, und das entsprechende Spielsteinchen (X oder O) in die
     * naechst moegliche, unterster, freie Stelle in der eingegebenen Spalte
     * gesetzt.
     *
     * @param
     * @return gibt das bearbeitete Spielfeld zurueck
     ******************************************************** */

    public static char[][] spielzug(char spielfeld[][], int spaltennummer,
                                    char spielsteinchen) { // Anfang spielzug()

        for (int y = 5; y >= 0; y--)
        // Schleife geht die Zeilen von unten nach oben durch, und Ueberprueft
        // so ob das Feld leer ist (=' ')
        // um in die gewÂ¸nschte Spalte das Spielsteinchen zu packen, an die
        // naechst moegliche, unterste, freie Stelle
        {
            if (spielfeld[y][spaltennummer] == ' ') // Ueberprueft die naechst
            // moegliche, freie,
            // unterste Stelle der
            // eingegebenen Spalte und
            // Ueberprueft ob
            // diese frei ist
            {
                spielfeld[y][spaltennummer] = spielsteinchen; // wenn die Zeile
                // frei ist,
                // wird das
                // Steinchen
                // gesetzt
                y = 0; // durch diese Abbruchbedingung wird die Schleife
                // abgebrochen, wenn das freie Feld ermittelt wurden ist
                // um das Steinchen zu setzen.
            }

        }
        return spielfeld; // Spielfeld wird bearbeitet zurueck gegeben
    } // ende der Funktion spielzug()

    /**
     * ******************************************************** In der Methode
     * <b>spielfeldBefuellen()</b> werden die zuvor gezeigten 0, durch die
     * gesetzten Spielsteinchen X und O ersetzt und angezeigt.
     *
     * @param
     * @return gibt bearbeitetes bzw. befuelltes Spielfeld zurÂ¸ck bennung
     *         aendern!
     ******************************************************** */

    public static char[][] spielfeldBefuellen(char spielfeld[][]) { // Anfang
        // spielfeldBefuellen()
        ueberschrift(7);
        for (char y = 0; y < spielfeld.length; y++) // y = Zeilen
        {
            for (char x = 0; x < spielfeld[y].length; x++) // x = spalten
            {
                System.out.print(spielfeld[y][x] + " | "); // Geruest
            }
            System.out.println(" "); // Zeilenumbruch
        }
        return spielfeld; // gibt befuelltes Spielfeld zurueck

    } // ende spielfeldBefuellen()

    /**
     * ******************************************************** In der Methode
     * <b>gueltigeEingabe()</b> wird geprueft, ob die Eingabe des Spielers
     * gueltig war, sprich ob die eingegebene Zahl zwischen 1 und 7 lag,
     * entsprechend der Spaltennummer. Falls eine ungueltige
     *
     * @param @ return
     ******************************************************** */

    public static int gueltigeEingabe(String spalte) { // Anfang
        // gueltigeEingabe()

        // Aufruf der Methode Integer_parseInt()
        int spaltennummer = Integer_parseInt(spalte);

        spaltennummer = spaltennummer - 1; // von der eingegebenen Zahl wird 1
        // subtrahiert, da ein Array bei 0
        // anfaengt
        // damit die gewuenschte Spalte auch
        // "angewaehlt" wird

        while (0 > spaltennummer || spaltennummer >= 7) // hier werden die
        // Tasten definiert, die
        // "zugelassen sind",
        // sprich die Zahlen
        // zwischen 1 und 7
        // (Spaltenanzahl)
        // damit bei ungueltigen Eingabe das Spiel nicht direkt abgebrochen wird
        {
            System.out
                    .println("Die Eingabe war ungueltig! Waehlen Sie bitte eine andere Spalte zwischen 1 und 7 aus!");
            // bei falscher Eingabe, wird der Spieler aufgefordert // eine neue
            // Spalte zwischen 1 und 7 zu wâ°hlen
            spalte = spielereingabe();
            // Aufruf der Methode spielereingabe()
            spaltennummer = Integer_parseInt(spalte);
            spaltennummer = spaltennummer - 1;// Die Eingabe (spaltennummer)
            // wird in einen Integer
            // umgewandelt
        }

        return spaltennummer; // gibt die gueltige Eingabe zurueck
    } // ende gueltigeEingabe()

    /**
     * ******************************************************** In der Methode
     * <b>gewonnen_vertikal()</b> wird einer der vier Moeglichkeiten das Spiel
     * zu gewinnen abgeprueft. In der folgenden Methode wird geprueft ob es
     * jemanden gelungen ist eine vertikale Viererlinie zu bauen. Zunaechst wird
     * jede Zeile, die aus 6 "Felder" besteht, abgetastet ob in der gewuenschten
     * Zeile, wo das neue Spielsteinchen hinein gesetzt werden soll, schon 3 der
     * gleichen Spielsteine vorhanden sind. Wenn dies zu trifft, was in der
     * if-Anweisung ueberprueft wird, hat der Spieler gewonnen und wird diesem
     * mitgeteilt. Danach kehrt das Spiel automatisch zum Begrueßungsbildschirm
     * zurueck.
     *
     * @return
     * @param spielfeld
     *            , spielsteinchen, String name, int i (43 - max. Spielzuege)
     *            ********************************************************
     */

    public static int gewonnen_vertikal(char spielfeld[][],
                                        char spielsteinchen, String name, int i) {

        for (int z = 5; z >= 3; z--) // jede Zeile wird geprueft, Index wird
        // immer um 1 erniedrigt (d.h. von oben
        // nach unten wird abgetastet)
        {
            for (int s = 0; s < spielfeld[z].length; s++) // Spalten werden
            // geprueft,
            {
                if (spielfeld[z][s] == spielsteinchen
                        && spielfeld[z - 1][s] == spielsteinchen
                        && spielfeld[z - 2][s] == spielsteinchen
                        && spielfeld[z - 3][s] == spielsteinchen) {
                    System.out.println(name + " hat gewonnen!");
                    i = 44;
                }
            }
        }
        return i;

    }

    /**
     * ******************************************************** In der Methode
     * <b>gewonnen_waagerecht()</b> wird einer der vier Moeglichkeiten das
     * Spiel zu gewinnen abgeprueft. In der folgenden Methode wird geprueft ob es
     * jemanden gelungen ist eine waagerechte Viererlinie zu bauen. Zunaechst
     * werden die Felder der Zeile der laenge nach (spielfeld.length) von 0
     * abgetastet (immer um 1). Bei den Spalten wird dann geguckt von ob nach
     * dem eingeworfenen Spielstein noch 3 nebenan sitzen, um abzuchecken, ob es
     * dem Spieler gelungen ist, eine Viererlinie waagerecht zu bauen. Dies wird
     * wieder innerhalb der if-Anweisung Ueberprueft.
     *
     * @return
     * @param spielfeld
     *            , spielsteinchen, String name, int i (43 - max. Spielzuege)
     *            ********************************************************
     */

    public static int gewonnen_waagerecht(char spielfeld[][],
                                          char spielsteinchen, String name, int i) {

        for (int z = 0; z < spielfeld.length; z++) // geht durch die kompletten
        // Zeilen durch
        {
            for (int s = 0; s <= 3; s++) // geht Spalten durch, guckt ob schon 3
            // vorhandene Spielsteine der
            // gleichen Art in der drin sind
            {
                if (spielfeld[z][s] == spielsteinchen
                        && spielfeld[z][s + 1] == spielsteinchen
                        && spielfeld[z][s + 2] == spielsteinchen
                        && spielfeld[z][s + 3] == spielsteinchen) // Ueberpruefung
                {
                    System.out.println(name + " hat gewonnen!");
                    i = 44; // Abbruchbedingung
                }
            }
        }
        return i;
    }

    /**
     * ******************************************************** In der Methode
     * <b>gewonnen_diagonal_1()</b> wird einer der vier Moeglichkeiten das Spiel
     * zu gewinnen abgeprueft. In der folgenden Methode wird geprueft ob es
     * jemanden gelungen ist eine diagonale Viererlinie zu bilden, und zwar von
     * rechts unten nach links oben. Dies wird mithilfe einer Art
     * Steigungsdreieck abgeprueft, in dem zu erst 2 "Schritte" in den Zeilen
     * und 3 "Schritte" in den Spalten abgegangen werden, und diese
     * zusammengesetzt werden und zudem wird geschaut ob dort ein Spielstein
     * liegt.
     *
     * @return
     * @param spielfeld
     *            , spielsteinchen, String name, int i
     *            ********************************************************
     */

    public static int gewonnen_diagonal_1(char spielfeld[][],
                                          char spielsteinchen, String name, int i) {

        for (int z = 0; z <= 2; z++) // durch die Zeilen gehen
        {
            for (int s = 0; s <= 3; s++) // durch die Spalten gehen
            {
                if (spielfeld[z][s] == spielsteinchen
                        && spielfeld[z + 1][s + 1] == spielsteinchen
                        && spielfeld[z + 2][s + 2] == spielsteinchen
                        && spielfeld[z + 3][s + 3] == spielsteinchen) // Werte
                // werden
                // zusammengesetzt
                // und
                // geguckt
                // ob
                // dort
                // ein
                // Spielstein
                // liegt
                {
                    System.out.println(name + " hat gewonnen!"); // wenn dies
                    // zutrifft,
                    // hat der
                    // Spieler
                    // gewonnen
                    // und das
                    // SPiel
                    // wird
                    // abgebrochen,
                    // man kehrt
                    // zum
                    // Begrueungsbildschirm
                    // zurueck
                    i = 44; // Abbruchbedingun
                }
            }
        }
        return i;
    }


    public static int gewonnen_diagonal_2(char spielfeld[][],
                                          char spielsteinchen, String name, int i) {

        for (int z = 0; z <= 2; z++) // zeilen durch gehen
        {
            for (int s = 6; s >= 3; s--) // Spalten durchgehen hier; um einen
            // Index verringern
            {
                if (spielfeld[z][s] == spielsteinchen
                        && spielfeld[z + 1][s - 1] == spielsteinchen
                        && spielfeld[z + 2][s - 2] == spielsteinchen
                        && spielfeld[z + 3][s - 3] == spielsteinchen) {
                    System.out.println(name + " hat gewonnen!"); // Spieler hat
                    // gewonnen,
                    // Spiel
                    // wird
                    // abgebrochen,
                    // man kehrt
                    // zum
                    // BegrÂ¸ï¬ungsbildschirm
                    // zurÂ¸ck
                    i = 44;
                }
            }
        }
        return i;
    }

    public static int gewonnen(char spielfeld[][], char spielsteinchen,
                               String name, int i) {

        i = gewonnen_vertikal(spielfeld, spielsteinchen, name, i); // Aufruf der
        // Funktion
        // gewonnen_vertikal()
        i = gewonnen_waagerecht(spielfeld, spielsteinchen, name, i); // Aufruf
        // der
        // Funktion
        // gewonnen_waagerecht()
        i = gewonnen_diagonal_1(spielfeld, spielsteinchen, name, i); // Aufruf
        // der
        // Funktion
        // gewonnen_diagonal_1()
        i = gewonnen_diagonal_2(spielfeld, spielsteinchen, name, i); // Aufruf
        // der
        // Funktion
        // gewonnen_diagonal_2()

        System.out.println(i);
        return i;
    }


    public static int volleSpalte(int spaltennummer, char spielfeld[][]) {
        boolean spaltevoll = spielfeld[0][spaltennummer] != ' '; // hier wird

        if (spaltevoll) // trifft dies zu, erfolgt die untenstehende Ausgabe
        {
            System.out
                    .println("Die von dir gewaehlte Spalte ist voll. Waehle eine andere Spalte zwischen 1 und 7 aus!");
            //String spalte = spielereingabe(); // hier werden die neu
            // eingegebenen "Zeichen"
            // entgegengenommen
            //spaltennummer = gueltigeEingabe(spalte); // hier wird geprueft ob
            // die Eingabe korrekt
            // ist
            return spaltennummer; // die neue Spaltennummer wird zurueckgegeben
            // und erneut ueberprueft
        }
        return spaltennummer; // Spaltennummer wird zurueck gegeben
    }


    public static void spielbrettVoll(char spielfeld[][]) { // anfang
        // spielbrettVoll()

        // for(int i = 0; i < spielfeld[0].length; spielfeld[0][i++])

        boolean vollesBrett = spielfeld[0][0] != ' ' && spielfeld[0][1] != ' '
                && spielfeld[0][2] != ' ' && spielfeld[0][3] != ' '
                && spielfeld[0][4] != ' ' && spielfeld[0][4] != ' '
                && spielfeld[0][5] != ' ' && spielfeld[0][6] != ' ';
        if (vollesBrett) {
            System.out
                    .println("Das Spielbrett ist voll! Das Spiel endet unentschieden!");
        }

    }


    public static void main(String args[]) { // anfang main()

        char spielfeld[][];
        spielfeld = new char[6][7];
        int j = 0;
        int[] sp1 = new int[3];

        while (j < 1000) // while-Schleife, die abgebrochen wird, sobald die Taste
        // 2 gedrueckt wird - so kann man das Spiel verlassen
        { // Beginn while-Schleife
            // Spielzuege - Spielablauf
            spielfeld = spielbrettleer(spielfeld);
            for (int i = 0; 43 > i; i++) // Schleife, zaehlt hoch bis 43,
            // weil nur 42 moegliche
            // Spielzuege erlaubt sind

            { // Beginn for-Schleife
                if (i % 2 == 0) // ungerader Zug - Spieler 1 ist dran
                {

                    i = spielablauf2(spielfeld, "Spieler 1", 'X', i);
                    if (i == 44) {
                        sp1[0]++;
                    }
                    if (i == 42) {
                        sp1[2]++;
                    }

                } else if (i % 2 != 0) // gerader Zug, Spieler 2 ist dran
                {
                    i = spielablauf2(spielfeld, "Spieler 2", 'O', i);
                    if (i == 44) {
                        sp1[1]++;
                    }
                    if (i == 42) {
                        sp1[2]++;
                    }

                }

                spielfeld = spielfeldBefuellen(spielfeld);
                //scannen(spielfeld);
            } // ende for-Schleife
            j++;
        } // ende Spiel starten
        System.out.println(Arrays.toString(sp1));

    } // ende main()

} // ende Class

