package fi.ymcafinland.demo.logiikka;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by xvixvi on 16.4.2016.
 * <p/>
 * Vaittamat vastaa väittämien luomisesta, kun sovellus käynnistetään, sekä väittämien ylläpidosta.
 */
public class Vaittamat {

    private HashMap<String, ArrayList<Vaittama>> karttaSolmujenVaittamista; //avaimena toisen tason solmun id (7-24)
    private ArrayList<String[]> rivit;

    public Vaittamat() {
        Scanner lukija = luoLukija("vaittamat.csv");

        rivit = new ArrayList<>();
        karttaSolmujenVaittamista = new HashMap<>();

        if (lukija != null) {
            lueRivit(lukija);
        }

        generoiVaittamat();
    }

    private Scanner luoLukija(String tiedostonNimi) {
        Scanner lukija = null;

        try {
            FileHandle file = Gdx.files.internal(tiedostonNimi);
            lukija = new Scanner(file.read());
        } catch (Exception e) {
            int loglevel = Gdx.app.getLogLevel();
            Gdx.app.setLogLevel(Application.LOG_ERROR);
            Gdx.app.log("VAITTAMAT", "lukuongelmia \n" + e);
            Gdx.app.setLogLevel(loglevel);
        }


        return lukija;
    }

    private void lueRivit(Scanner lukija) {
        Gdx.app.log("VAITTAMAT", "luetaan rivit");
        while (lukija.hasNextLine()) {
            rivit.add(pilkoRivi(lukija.nextLine()));
        }
    }

    //todo jos lause on suljettu hipsuihin " ", poistetaan ne
    private String[] pilkoRivi(String kokoRivi) {
        String[] rivi0 = kokoRivi.split(",");
        String[] pilkottuRivi = new String[18];
        int ind = 0;

        String edellinenPala = rivi0[0];
        for (int i = 1; i < rivi0.length; i++) {
            boolean pilkottuSolu = false;
            String pala = rivi0[i];

            if (edellinenPala.startsWith("\"")) {
                pala = edellinenPala + "," + pala;
                pilkottuSolu = true;
            } else if (pala.startsWith("\"")) {
                pilkottuSolu = true;
            }

            if (pala.endsWith("\"")) { //solu on valmis

                pilkottuRivi[ind] = pala.substring(1, pala.length()-1); //poistetaan " alusta ja lopusta
                ind++;
                pala = "";

            } else if (!pilkottuSolu) {
                pilkottuRivi[ind] = pala;
                ind++;
            }

            edellinenPala = pala;
        }

        //lisätään loppuun tyhjät väittämäsarakkeet, jos tarvetta
        for (int i = ind; i < 18; i++) {
            pilkottuRivi[i] = "";
            ind = i + 1;
        }

        //debuggaukseen
        if (ind != 18) {
            Gdx.app.log("VAITTAMAT", "pilkottuun riviin ei tullut 18  palasta (18*väittämäsarake)!\n"
                    + "palasia oli " + ind);
        }

        return pilkottuRivi;
    }

    private void generoiVaittamat() {
        Gdx.app.log("VAITTAMAT", "generoidaan väittämät");

        for (int i = 0; i < 18; i++) { //jokaisen solmun...
            String id = "" + (i + 7);
            ArrayList<Vaittama> solmunVaittamat = new ArrayList<>();

            for (int j = 2; j < rivit.size(); j++) { //...jokaiselle väittämäriville...
                String vaittamatxt = rivit.get(j)[i];
                if (vaittamatxt.equalsIgnoreCase("")) { //...kunnes väittämiä ei enää ole:
                    break;
                }

                Vaittama v = new Vaittama(vaittamatxt, id); //luodaan uusi väittämä...
                solmunVaittamat.add(v);                     //...lisätään se väittämälistaan, ja...
            }

            karttaSolmujenVaittamista.put(id, solmunVaittamat); //talletetaan väittämälista karttaan kyseisen solmun id.llä
        }
    }

    /**
     * palauttaa kartan, johon väittämät on talletettu avaimena toisen tason solmun id (7-24) ja
     * arvona kyseiseen solmuun liittyvät väittämät listassa.
     *
     * @return kartta, jossa on väittämälistoja
     */
    public HashMap<String, ArrayList<Vaittama>> getKarttaSolmujenVaittamista() {
        return karttaSolmujenVaittamista;
    }

    /**
     * Palauttaa toisen tason solmun id.tä vastaavan listan väittämistä.
     *
     * @param solmunID 7-24
     * @return lista väittämistä
     */
    public ArrayList<Vaittama> getYhdenSolmunVaittamat(String solmunID) {
        return karttaSolmujenVaittamista.get(solmunID);
    }
}
