package fi.ymcafinland.demo.logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Selviytyjän purjeiden solmujen kokoelma. Vastaa solmujen luomisesta ja ylläpidosta.
 */
public class Verkko {

    private ArrayList<Solmu> solmut;
    private I18NBundle myBundle;
    private final int leveysPalikka;
    private final int korkeusPalikka;

    public Verkko(int taustakuvanLeveys, int taustakuvanKorkeus) {
        this.leveysPalikka = taustakuvanLeveys / 100;
        this.korkeusPalikka = taustakuvanKorkeus / 100;
        solmut = new ArrayList<>();
        String polkuTiedostolle = "solmuja/solmut";
        FileHandle baseFileHandle = Gdx.files.internal(polkuTiedostolle);

        /*
        try-catch rakenne testaamista ja pöytäkonetta varten, voidaan poistaa(/kommentoida) kun demo valmis.
        Androidilla toimii moitteetta try-lohkossa oleva rivi, koska android poikkeuksetta on asetettu
        etsimään internal-memoryä assets-kansiosta (jossa solmut/solmut.properties sijaitsee.
         */
        try {
            myBundle = I18NBundle.createBundle(baseFileHandle);
        } catch (Exception e) {
            String absolutePath = getAbsolutePathToFile();
            baseFileHandle = Gdx.files.absolute(absolutePath);
            myBundle = I18NBundle.createBundle(baseFileHandle);
        }

//        //englanninkielisen version testaamiseen
//        Locale locale = new Locale("en");
//        myBundle = I18NBundle.createBundle(baseFileHandle, locale);
        generoiSolmut();
    }

    //Jesarimetodi korjaamaan ajettavuus desktopille ja testeille androidin lisäksi.
    private String getAbsolutePathToFile() {
        String ap = Paths.get("").toAbsolutePath().toString();
        String[] hakemistot = ap.split("/");
        int i = hakemistot.length - 1;
        while (!hakemistot[i].equalsIgnoreCase("STKT-demo")) {
            i--;
        }

        String fileSeparator = File.separator;
        ap = "";
        for (int j = 0; j < i + 1; j++) {
            ap = ap.concat(hakemistot[j]) + fileSeparator;
        }
        ap = ap.concat("android" + fileSeparator + "assets" + fileSeparator + "solmuja" + fileSeparator + "solmut");
        return ap;
    }

    private void generoiSolmut() {

        solmut.addAll(luoEnsimmainenTaso(6));
        solmut.addAll(luoToinenTaso());

        asetaOtsikotJaSisallot();
    }

    private ArrayList<Solmu> luoEnsimmainenTaso(int montako) {
        ArrayList<Solmu> lista = new ArrayList<>();

        for (int i = 1; i < montako + 1; i++) {
            Solmu s = new Solmu("" + i, null);
            lista.add(s);
        }

        asetaTasonSolmutToistensaSisaruksiksi(lista);
        asetaTasonSolmujenSijainnit(lista, false, leveysPalikka * 10);

        return lista;
    }

    private ArrayList<Solmu> luoToinenTaso() {
        ArrayList<Solmu> toinenTaso = new ArrayList<>();
        for (Solmu s : solmut) {
            toinenTaso.addAll(luoLapset(s));
        }

        asetaTasonSolmutToistensaSisaruksiksi(toinenTaso);
        asetaTasonSolmujenSijainnit(toinenTaso, true, leveysPalikka * 20);
        return toinenTaso;
    }

    private void asetaTasonSolmutToistensaSisaruksiksi(ArrayList<Solmu> tasonSolmut) {
        int montako = tasonSolmut.size();

        //paritetaan ensimmäinen ja viimeinen solmu
        Solmu vasen = tasonSolmut.get(montako - 1);
        Solmu s = tasonSolmut.get(0);
        asetaSisaruksiksi(s, vasen);

        for (int i = 1; i < montako; i++) {
            vasen = tasonSolmut.get(i - 1);
            s = tasonSolmut.get(i);
            asetaSisaruksiksi(s, vasen);
        }
    }

    private void asetaSisaruksiksi(Solmu s, Solmu vasen) {
        s.setVasenSisarus(vasen);
        vasen.setOikeaSisarus(s);
    }

    private ArrayList<Solmu> luoLapset(Solmu s) {
        int mutsinID = Integer.parseInt(s.getID());
        int lapsenID = 7 + (mutsinID - 1) * 3;
        ArrayList<Solmu> lapset = new ArrayList<>();
        for (int i = lapsenID; i < lapsenID + 3; i++) {
            Solmu lapsi = new Solmu("" + i, s);
            lapset.add(lapsi);
        }
        s.setLapset(lapset);
        return lapset;
    }

    private void asetaOtsikotJaSisallot() {
        for (Solmu s : solmut) {
            String otsikko = myBundle.format("solmun_otsikko_" + s.getID());
            s.setOtsikko(otsikko);
            String sisalto = myBundle.format("solmun_sisalto_" + s.getID());
            s.setSisalto(sisalto);
        }
    }

    /**
     * Metodi asettaa yhden tason solmut ympyrään taustakuvan keskipisteen ympärille. Asettaa solmut siten, että ympyrässä seuraava solmu vastapäivään on solmun oikea sisar.
     *
     * @param tasonSolmut ympyrään asetettavat solmut.
     * @param sade        muodostettavan ympyrän säde.
     */
    private void asetaTasonSolmujenSijainnit(ArrayList<Solmu> tasonSolmut, boolean toinenTaso, int sade) {
        final int keskiX = leveysPalikka * 50;
        final int keskiY = korkeusPalikka * 50;
        int solmuja = tasonSolmut.size();

        final double kulma = Math.toRadians(360 / solmuja);
        double k = kulma;
        Solmu s = tasonSolmut.get(0);

        if (toinenTaso) {
            s = tasonSolmut.get(solmuja - 1);
        }

        for (int i = 0; i < solmuja; i++) {
            int x = (int) (sade * Math.cos(k)) + keskiX;
            int y = (int) (sade * Math.sin(k) + keskiY);

            s.setSijainti(x, y);
            s = s.getOikeaSisarus();
            k += kulma;
        }
    }

    public ArrayList<Solmu> getSolmut() {
        return solmut;
    }
}
