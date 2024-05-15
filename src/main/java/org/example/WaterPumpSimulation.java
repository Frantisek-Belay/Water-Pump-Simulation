package org.example;    /* Tento řádek definuje jmenný prostor (package), ve kterém je umístěna tato třída.*/

import java.util.Scanner;   /* Importuje třídu `Scanner` z balíku `java.util`. `Scanner` je používán k získávání vstupu od uživatele.*/
import java.time.Duration;  /* Importuje třídu `Duration` z balíku `java.time`. `Duration` se používá k práci s časovými úseky.*/

public class WaterPumpSimulation {  /* Zde začíná definice třídy `WaterPumpSimulation`, která obsahuje metodu `main` jako vstupní bod programu.*/
    public static void main(String[] args) {    /* Definice metody `main`, což je vstupní bod programu. Tato metoda je volána při spuštění programu.*/
        Scanner input = new Scanner(System.in); /* Vytvoření instance `Scanner` pro načítání vstupu od uživatele ze systémového vstupu (`System.in`).*/

        System.out.println();   /* Toto jsou řádky, které vypisují uvítací zprávu na konzoli. */
        System.out.println("*********************************************");
        System.out.println("* SIMULACE ČERPÁNÍ VODY ČERPADLEM ZE STUDNY *");
        System.out.println("*      Autor projektu: František Belay      *");
        System.out.println("*         Kontakt: frabel@volny.cz          *");
        System.out.println("*********************************************");

        /* Zadání parametrů studny - Zde začíná část kódu, která zadává parametry studny od uživatele pomocí cyklů `do-while`. */
        double studnaPrumer;
        double minVyskaHladiny;
        double maxVyskaHladiny;
        double minPritok;
        double maxPritok;

        do {
            System.out.print("Zadej průměr studny (200-3000 mm): ");
            studnaPrumer = input.nextDouble();
        } while (studnaPrumer < 200 || studnaPrumer > 3000);

        do {
            System.out.print("Zadej minimální výšku hladiny vody (1000-5000 mm): ");
            minVyskaHladiny = input.nextDouble();
        } while (minVyskaHladiny < 1000 || minVyskaHladiny > 5000);

        do {
            System.out.print("Zadej maximální výšku hladiny vody (1000-5000 mm): ");
            maxVyskaHladiny = input.nextDouble();
        } while (maxVyskaHladiny < 1000 || maxVyskaHladiny > 5000);

        double vyskaSloupceVody = maxVyskaHladiny - minVyskaHladiny;
        System.out.println("Výška sloupce vody k čerpání je: " + vyskaSloupceVody + " mm");

        do {
            System.out.print("Zadej rychlost přítoku vody do studny (1000-100000 ml/min): ");
            minPritok = input.nextDouble();
        } while (minPritok < 1000 || minPritok > 100000);

        /* do {    Zde je zakomentovaný blok pro zadání maximálního přítoku vody, což v programu aktuálně není použito.
            System.out.print("Zadej maximální přítok vody (1000-10000 ml/min): ");
            maxPritok = input.nextDouble();
        } while (maxPritok < 1000 || maxPritok > 10000); */

        /* Zadání parametrů čerpadla */
        double rychlostCerpani;
        do {    /* Zadání rychlosti čerpání vody čerpadlem od uživatele s opětovnou kontrolou platnosti vstupu. */
            System.out.print("Zadej rychlost čerpání vody čerpadlem (500-100000 ml/min): ");
            rychlostCerpani = input.nextDouble();
        } while (rychlostCerpani < 500 || rychlostCerpani > 100000);

        /* SKUTEČNÁ RYCHLOST ČERPÁNÍ VODY =  rychlostCerpani -  minPritok: */
        double skutecnaRychlostCerpani = rychlostCerpani - minPritok;
        System.out.println("Skutečná rychlost čerpání vody (ml/min): " + skutecnaRychlostCerpani);

        /* Výpočet objemu vody - Výpočet objemu vody v závislosti na zadaných parametrech studny a výšce hladiny. */
        double objemMinHladiny = Math.PI * Math.pow(studnaPrumer / 2, 2) * minVyskaHladiny / 1000000; /* v litrech */
        double objemMaxHladiny = Math.PI * Math.pow(studnaPrumer / 2, 2) * maxVyskaHladiny / 1000000; /* v litrech */
        double objemKcerpani = objemMaxHladiny - objemMinHladiny; /* Vypočtený objem vody k čerpání */
    /*  double minutovyPoklesHladiny = (vyskaSloupceVody / objemKcerpani) * (skutecnaRychlostCerpani/1000); /* Původní funkční */

        /* TADY je první verze pro opravu správného odečítání poklesu pro všechny sekundy - 10.5.2024 */
        double minutovyPoklesHladiny = (vyskaSloupceVody / objemKcerpani) * (skutecnaRychlostCerpani / 1000);

        /* Výpočet poklesu hladiny za 10 sekund */
        double poklesZa10Sekund = minutovyPoklesHladiny / 6;
        double poklesZa1Sekundu = minutovyPoklesHladiny / 60;

        double poklesZaDesetSekund = minutovyPoklesHladiny / 6;

        /* Kontrola, zda čerpadlo může udržet krok s přítokem vody, a pokudne, lze upravit vstupní parametry nebo program ukončit. */
        if (rychlostCerpani <= minPritok) {
            System.out.println("Čerpadlo bude čerpat vodu nepřetržitě a nikdy nedojde k jeho vypnutí.");
            System.out.println("Upravte vstupní parametry přítoku vody do studny a rychlost čerpání vody čerpadlem.");
            System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení. */
        }

        long pocetSekund = (long) (vyskaSloupceVody / minutovyPoklesHladiny * 60); /* Převedeme minutový pokles na celkový počet sekund */
        Duration casCerpani = Duration.ofSeconds(pocetSekund); /* Tyto dva řádky vypočítávají správně celkový čas včetně vteřin */

        long casVterin = casCerpani.getSeconds();

        /* Zobrazí informace o čerpání vody */
        System.out.println();
        System.out.println("Objem vody ve studni o průměru " + studnaPrumer + " mm při nejnižší hladině vody " + minVyskaHladiny + " mm je: " + String.format("%.3f", objemMinHladiny) + " litrů");
        System.out.println("Objem vody ve studni o průměru " + studnaPrumer + " mm při nejvyšší hladině vody " + maxVyskaHladiny + " mm je: " + String.format("%.3f", objemMaxHladiny) + " litrů");
        System.out.println("Objem vody k čerpání: " + String.format("%.3f", objemKcerpani) + " litrů");
        System.out.println("Hladina vody klesne za 1 minutu o: " + String.format("%.3f", minutovyPoklesHladiny) + " mm"); /* pokles hladiny vody zaokrouhlený na 3 des. místa */
        System.out.println("Celková doba čerpání vody čerpadlem bude: " + casCerpani.toHours() + " hodin, " +
                (casCerpani.toMinutes() % 60) + " minut, " + (casCerpani.toSeconds() % 60) + " sekund");

        System.out.println();

        if (casCerpani.toMinutes() > 60) {  /* Výpočet celkové doby čerpání vody na základě objemu vody a rychlosti čerpání.*/
            System.out.println("Doba čerpání vody podle zadaných parametrů bude delší, než 1 hodina.");
            System.out.println("Přejete si i přesto zahájit čerpání vody ze studny? (ano/ne): ");
            String volba = input.next();    /* Pokud doba čerpání je delší než 1 hodina, program se zeptá uživatele, zda chce pokračovat.*/
            if (!volba.equalsIgnoreCase("ano")) {
                System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
            }
        }

        /* Menu volby */
        while (true) { /* Toto je hlavní menu volby, kde uživatel může vybrat mezi úpravou parametrů, spuštěním čerpadla a ukončením programu. Následuje čekání na volbu uživatele a zpracování této volby.*/
            System.out.println("*********************************************");
            System.out.println("*            Zvolte další krok:             *");
            System.out.println("*********************************************");
            System.out.println("1 - Upravit parametry studny a čerpadla");
            System.out.println("2 - Spustit čerpadlo a zahájit čerpání vody ze studny");
            System.out.println("3 - KONEC programu");
            System.out.print("Zvolte možnost (1/2/3): ");
            System.out.println();
            int volbaMenu = input.nextInt();

            if (volbaMenu == 1) {
                /* Upravit parametry studny a čerpadla */
                System.out.println("*********************************************");
                System.out.println("*    Změna parametrů studny a čerpadla:     *");
                System.out.println("*********************************************");
               /* Opět zadání parametrů studny */
                do {
                    System.out.print("Zadej průměr studny (200-3000 mm): ");
                    studnaPrumer = input.nextDouble();
                } while (studnaPrumer < 200 || studnaPrumer > 3000);

                do {
                    System.out.print("Zadej minimální výšku hladiny vody (1000-5000 mm): ");
                    minVyskaHladiny = input.nextDouble();
                } while (minVyskaHladiny < 1000 || minVyskaHladiny > 5000);

                do {
                    System.out.print("Zadej maximální výšku hladiny vody (1000-5000 mm): ");
                    maxVyskaHladiny = input.nextDouble();
                } while (maxVyskaHladiny < 1000 || maxVyskaHladiny > 5000);

                vyskaSloupceVody = maxVyskaHladiny - minVyskaHladiny;
                System.out.println("Výška sloupce vody k čerpání je: " + vyskaSloupceVody + " mm"); /* Zrušil jsem typ - ten je již deklarován výše */

                do {
                    System.out.print("Zadej rychlost přítoku vody do studny (1000-100000 ml/min): ");
                    minPritok = input.nextDouble();
                } while (minPritok < 1000 || minPritok > 100000);

                /* Opět zadání parametrů čerpadla */
                do {
                    System.out.print("Zadej rychlost čerpání vody čerpadlem (500-100000 ml/min): ");
                    rychlostCerpani = input.nextDouble();
                } while (rychlostCerpani < 500 || rychlostCerpani > 100000);

                /* SKUTEČNÁ RYCHLOST ČERPÁNÍ VODY =  rychlostCerpani -  minPritok: */
                skutecnaRychlostCerpani = rychlostCerpani - minPritok;
                System.out.println("Skutečná rychlost čerpání vody (ml/min): " + skutecnaRychlostCerpani);

                /* Výpočet objemu vody s novými parametry */
                objemMinHladiny = Math.PI * Math.pow(studnaPrumer / 2, 2) * minVyskaHladiny / 1000000; /* v litrech */
                objemMaxHladiny = Math.PI * Math.pow(studnaPrumer / 2, 2) * maxVyskaHladiny / 1000000; /* v litrech */

                /* Kontrola, zda čerpadlo může udržet krok s přítokem vody, a pokud ne, lze upravit vstupní parametry nebo program ukončit.*/
                if (rychlostCerpani <= minPritok) {
                    System.out.println("Čerpadlo bude čerpat vodu nepřetržitě a nikdy nedojde k jeho vypnutí.");
                    System.out.println("Upravte vstupní parametry přítoku vody do studny a rychlost čerpání vody čerpadlem.");
                    System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
                }

                /* Výpočet nové doby čerpání */
                pocetSekund = (long) (vyskaSloupceVody / minutovyPoklesHladiny * 60); /* Převedeme minutový pokles na celkový počet sekund */
                casCerpani = Duration.ofSeconds(pocetSekund);

               casVterin = casCerpani.getSeconds();

                System.out.println();
                System.out.println("Objem vody ve studni o průměru " + studnaPrumer + " mm při nejnižší hladině vody " + minVyskaHladiny + " mm je: " + String.format("%.3f", objemMinHladiny) + " litrů");
                System.out.println("Objem vody ve studni o průměru " + studnaPrumer + " mm při nejvyšší hladině vody " + maxVyskaHladiny + " mm je: " + String.format("%.3f", objemMaxHladiny) + " litrů");
                System.out.println("Objem vody k čerpání: " + String.format("%.3f", objemKcerpani) + " litrů");
                System.out.println("Celková doba čerpání vody čerpadlem bude: " + casCerpani.toHours() + " hodin, " + casCerpani.toMinutes() % 60 + " minut, " + casCerpani.toSeconds() % 60 + " sekund");
                System.out.println();

                if (casCerpani.toMinutes() > 60) {
                    System.out.println("Doba čerpání vody podle zadaných parametrů bude delší, než 1 hodina.");
                    System.out.println("Přejete si i přesto zahájit čerpání vody ze studny? (ano/ne): ");
                    String volba = input.next();
                    if (!volba.equalsIgnoreCase("ano")) {
                        System.exit(0); /** Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
                    }
                }   /** Blok pro spuštění čerpadla a simulaci čerpání vody ze studny. Program v reálném čase aktualizuje informace o zbývajícím čase,
                 aktuální výšce hladiny a dalších důležitých informacích.*/

            } else if (volbaMenu == 2) {    /* Spustit čerpadlo a zahájit čerpání vody ze studny */
                System.out.println();
                System.out.println("*********************************************");
                System.out.println("*         Spouštím čerpadlo...              *");
                System.out.println("*********************************************");
                System.out.println();

                pocetSekund = (long) (vyskaSloupceVody / minutovyPoklesHladiny * 60); /* Převedeme minutový pokles na celkový počet sekund */
                casCerpani = Duration.ofSeconds(pocetSekund);

                casVterin = casCerpani.getSeconds();

                /* Výpočet vyčerpaného objemu a poklesu hladiny
            /*  double vycerpanyObjem = (skutecnaRychlostCerpani / 6); /* vyčerpaný objem za 10 sekund */
            /*  double poklesHladiny = vycerpanyObjem / (Math.PI * Math.pow((studnaPrumer / 2), 2)); /* pokles hladiny za 10 sekund */
                double aktualniHladina = maxVyskaHladiny - ( minutovyPoklesHladiny / 6 );

                Duration casOdZapnuti = Duration.ofSeconds(0); /* POSLEDNÍ ZMĚNA - PŮVODNĚ MINUTY */

                while (casOdZapnuti.compareTo(casCerpani) < 0) {
            /*      System.out.println("Celková doba čerpání vody: " + casCerpani.toHours() + ":" + (casVterin % 3600) / 60 + ":" + (casVterin % 60)); */
                    System.out.println("Celková doba čerpání vody: " + casCerpani.toHours() + " hodin, " + casCerpani.toMinutes() % 60 + " minut, " + casCerpani.toSeconds() % 60 + " sekund");

            /*      System.out.print("Čas od zapnutí čerpadla: " + String.format("%02d", casOdZapnuti.toHours()) + ":" + String.format("%02d", casOdZapnuti.toMinutes() % 60) + ":" + String.format("%02d", casOdZapnuti.toSecondsPart())); */
                    System.out.println("Čas od zapnutí čerpadla: " + String.format("%02d", casOdZapnuti.toHours()) + ":" + String.format("%02d", casOdZapnuti.toMinutes() % 60) + ":" + String.format("%02d", casOdZapnuti.toSecondsPart()));

                    System.out.println("Do ukončení čerpání zbývá: " + String.format("%02d", casCerpani.minus(casOdZapnuti).toHours()) + ":" + String.format("%02d", casCerpani.minus(casOdZapnuti).toMinutes() % 60) + ":" + String.format("%02d", casCerpani.minus(casOdZapnuti).getSeconds() % 60));
                    System.out.println();

                    try {
                        Thread.sleep(10000); /* 10 sekund mezi aktualizacemi stavu čerpání vody */
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

/*                    System.out.println("Aktuální výška hladiny vody je: " + String.format("%.3f", aktualniHladina) + " mm"); /* Výška hladiny vody zaokrouhlená na 3 des. místa */
                    System.out.println("Aktuální výška hladiny vody je: " + String.format("%.3f", aktualniHladina) + " mm"); /* Úprava pro správný odečet poklesu výšky hladiny vody */

                    /* Výpočet zbývajícího času ve šedesátkové soustavě /* ZASTAVENO: 10.5. 12:24 */
/*                    Duration casZbyva = casCerpani.minus(casOdZapnuti).minus(Duration.ofSeconds(10)); */
/*                    double zbyleHodiny = casZbyva.toHours(); */
/*                   double zbyleMinuty = (casZbyva.toMinutes() - zbyleHodiny * 60); */
/*                   double zbyleVteriny = (casZbyva.getSeconds() - zbyleHodiny * 3600 - zbyleMinuty * 60); */
/*                  System.out.println("Do ukončení čerpání vody zbývá: " + String.format("%02d", zbyleHodiny) + ":" + String.format("%02d", zbyleMinuty) + ":" + String.format("%02d", zbyleVteriny)); */
/*                    System.out.println("Do ukončení čerpání vody zbývá: " + String.format("%02.0f", zbyleHodiny) + ":" + String.format("%02.0f", zbyleMinuty) + ":" + String.format("%02.0f", zbyleVteriny)); */


                    /* Aktualizace času a hladiny */
                    casOdZapnuti = casOdZapnuti.plus(Duration.ofSeconds(10));
/*                    aktualniHladina -= ( minutovyPoklesHladiny / 6 ); /* ZASTAVENO: 10.5. 12:24 */
                    /* Pokles hladiny vody za 10 sekund  =  DOPLNĚNO: 10.5. 12:24 */
                    poklesZa10Sekund = minutovyPoklesHladiny / 6;
                    /* Pokles hladiny vody za 1 sekundu  =  DOPLNĚNO: 10.5. 12:24 */
                    poklesZa1Sekundu = poklesZa10Sekund / 10;
/*                    System.out.println("Hodnota aktuaální hladiny po odečtu poklesu za 10s: " + aktualniHladina); */

                    /* Pokud zbývá méně než 10 sekund do konce čerpání, proměnná se aktivuje a */
                    if (casCerpani.getSeconds() < 10) {
                        /* Vypočítáme zbývající čas do konce čerpání */
                        double zbyleSekundy = casCerpani.getSeconds();
                        /* Odečteme zbývající pokles hladiny */
                        double posledniPokles = poklesZa1Sekundu * zbyleSekundy;
                        /* Aktualizujeme výšku sloupce vody */
                        vyskaSloupceVody -= posledniPokles;
                    } else {
                        /* Pokud zbývá 10 sekund nebo více, odečteme standardní pokles za 10 sekund */
                        vyskaSloupceVody -= poklesZa10Sekund;
                    }


                    /* Aktualizace zbývajícího času ve šedesátkové soustavě  =  DOPLNĚNO: 10.5. 12:24 */
                    Duration casZbyva = casCerpani.minus(casOdZapnuti).minus(Duration.ofSeconds(10));
                    double zbyleHodiny = casZbyva.toHours();
                    double zbyleMinuty = casZbyva.toMinutes() % 60;
                    double zbyleVteriny = casZbyva.getSeconds() % 60;
/*                    System.out.println("Do ukončení čerpání vody zbývá: " + String.format("%02.0f", zbyleHodiny) + ":" + String.format("%02.0f", zbyleMinuty) + ":" + String.format("%02.0f", zbyleVteriny)); */

                    /* Aktualizace výšky hladiny vody  =  DOPLNĚNO: 10.5. 12:24 */
                    aktualniHladina -= (minutovyPoklesHladiny / 6);
/*                    System.out.println("Hodnota aktuální hladiny po odečtu poklesu za 10s: " + aktualniHladina); */


                    if (aktualniHladina < minVyskaHladiny) {
/*                      double aktualniHladina = minVyskaHladiny; /* Chraň minimální výšku (původně za rovnítkem: (int) ) */

                        try {
                            Thread.sleep(10000); /* 10 sekund */
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Hladina vody ve studni dosáhla své spodní hranice 1000mm.");
                System.out.println("Čerpání vody bylo proto přerušeno.");
                System.out.println("Vyčkejte prosím, než dojde k opětovnému zvýšení hladiny vody.");

                double casDoSpusteni = objemKcerpani / (minPritok / 1000); /* Celkový čas napouštění studny v minutách */
                pocetSekund = (long) (casDoSpusteni * 60); /* Celkový čas napouštění studny v sekundách */
                long hodinDoSpusteni = pocetSekund / 3600;
                long minutDoSpusteni = (pocetSekund % 3600) / 60;
                long sekundDoSpusteni = pocetSekund % 60;
                System.out.println("Do možnosti opětovného zahájení čerpání vody zbývá: " + hodinDoSpusteni + " hodin, " + minutDoSpusteni + " minut, " + sekundDoSpusteni + " sekund.");
                System.out.println("Po uplynutí této doby můžete opět zahájit čerpání vody.");


            /*  System.out.println("Čerpadlo dokončilo čerpání vody."); */
                System.out.println();

                /* Po dokončení simulace program nabízí možnost pokračovat nebo ukončit program, v závislosti na výběru uživatele.*/
                System.out.print("Simulace dokončena, přejete si pokračovat v napouštění studny? (ano/ne): ");
                String volba = input.next();
                if (!volba.equalsIgnoreCase("ano")) {
                    System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
                }
            } else if (volbaMenu == 3) {    /* Pokud uživatel vybere možnost 3, program se ukončí. V opačném případě se vypíše zpráva o neplatné volbě.*/
                System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
            } else {
                System.out.println("Neplatná volba. Zvolte 1, 2 nebo 3.");
            }
            System.exit(0); /* Tato instrukce ukončí program s kódem 0, což znamená úspěšné ukončení.*/
        }
    }
}
