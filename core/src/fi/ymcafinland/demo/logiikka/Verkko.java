package fi.ymcafinland.demo.logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Selviytyjän purjeiden solmujen kokoelma. Vastaa solmujen luomisesta ja ylläpidosta.
 */
public class Verkko {

    private ArrayList<Solmu> solmut;
    private I18NBundle myBundle;
    private final float leveysPalikka;
    private final float korkeusPalikka;
    private final Vector2 keskipiste;
    private Solmu edellistaKosketustaLahinSolmu;
    private float lahimmanSolmunEtaisyys;
    private float[] solmujenJarjestysViimeisenKosketuksenEtaisyydenMukaan;
    Solmu keski;

    public Verkko(int taustakuvanLeveys, int taustakuvanKorkeus) {
        this.leveysPalikka = taustakuvanLeveys / 100;
        this.korkeusPalikka = taustakuvanKorkeus / 100;
        keskipiste = new Vector2(korkeusPalikka * 50, leveysPalikka * 50);
        solmut = new ArrayList<>();

        luoBundle();
        generoiSolmut();
        edellistaKosketustaLahinSolmu = solmut.get(0);
        solmujenJarjestysViimeisenKosketuksenEtaisyydenMukaan = new float[solmut.size()];
    }

    public void luoBundle() {
        String polkuTiedostolle = "solmujentekstit/solmut";
        FileHandle baseFileHandle = Gdx.files.internal(polkuTiedostolle);

        /*
        try-catch rakenne testaamista ja pöytäkonetta varten, voidaan poistaa(/kommentoida) kun demo valmis.
        Androidilla toimii moitteetta try-lohkossa oleva rivi, koska android poikkeuksetta on asetettu
        etsimään internal-memoryä assets-kansiosta (jossa solmut/solmut.properties sijaitsee).
         */
        try {
            myBundle = I18NBundle.createBundle(baseFileHandle);
        } catch (Exception e) {
            Gdx.app.log("ERROR", "kielibundlen teko ei onnistunut, käytetään jesarimenetelmää.");
            String absolutePath = getAbsolutePathToFile();
            baseFileHandle = Gdx.files.absolute(absolutePath);
            myBundle = I18NBundle.createBundle(baseFileHandle);
        }
    }

    //Jesarimetodi korjaamaan ajettavuus desktopille ja testeille androidin lisäksi.
    private String getAbsolutePathToFile() {
        String fileSeparator = File.separator;
        String ap = Paths.get("").toAbsolutePath().toString();
        String[] hakemistot = ap.split(fileSeparator);
        int i = hakemistot.length - 1;
        while (!hakemistot[i].equalsIgnoreCase("STKT-demo") && i > 0) {
            i--;
        }

        ap = "";
        for (int j = 0; j < i + 1; j++) {
            ap = ap.concat(hakemistot[j]) + fileSeparator;
        }
        ap = ap.concat("android" + fileSeparator + "assets" + fileSeparator + "solmujentekstit" + fileSeparator + "solmut");
        return ap;
    }

    private void generoiSolmut() {


//        solmut.addAll(luoToinenTaso());
        solmut.add(luoKeskiSolmu());
        solmut.addAll(luoEnsimmainenTaso(6, null));


        asetaOtsikotJaSisallot();
    }

    private Solmu luoKeskiSolmu() {
        Solmu s = new Solmu("0", null);
        s.setVasenSisarus(s);
        s.setOikeaSisarus(s);
        s.setSijainti((int) keskipiste.x, (int) keskipiste.y);
        keski = s;

        return s;
    }

    private ArrayList<Solmu> luoEnsimmainenTaso(int montako, Solmu keskisolmu) {
        ArrayList<Solmu> lista = new ArrayList<>();

        for (int i = 1; i < montako + 1; i++) {
            Solmu s = new Solmu("" + i, null);
            lista.add(s);
        }

        asetaTasonSolmutToistensaSisaruksiksi(lista);
        asetaTasonSolmujenSijainnit(lista, false, leveysPalikka * 10);
        asetaDialogienTekstit(lista);

        return lista;
    }

    private void asetaDialogienTekstit(ArrayList<Solmu> lista) {
        for (Solmu s : lista) {
            int mutsinID = Integer.parseInt(s.getID());
            int lapsenID = 7 + (mutsinID - 1) * 3;
            String dialoginSisalto = "";
            for (int i = lapsenID; i < lapsenID + 3; i++) {
                String otsikko = haeSolmunSisalto("solmun_sisalto_" + i);
                dialoginSisalto += "\n" + otsikko;
            }
            s.setDialoginSisalto(dialoginSisalto);
        }
    }

    private ArrayList<Solmu> luoToinenTaso() {
        ArrayList<Solmu> toinenTaso = new ArrayList<>();
        for (Solmu s : solmut) {
            if (s.getID().equals("0")) {
                continue;
            }
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
        asetaVanhempi(s);

        for (int i = 1; i < montako; i++) {
            vasen = tasonSolmut.get(i - 1);
            s = tasonSolmut.get(i);
            asetaVanhempi(s);
            asetaSisaruksiksi(s, vasen);
        }
    }

    private void asetaVanhempi(Solmu s) {
        s.setMutsi(keski);
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
            String otsikko = haeSolmunOtsikko("solmun_otsikko_" + s.getID());
            s.setOtsikko(otsikko);
            String sisalto = haeSolmunSisalto("solmun_sisalto_" + s.getID());
            s.setSisalto(sisalto);
        }
    }

    private String haeSolmunSisalto(String avain) {
        return myBundle.format(avain);
    }

    private String haeSolmunOtsikko(String avain) {
        return myBundle.format(avain);
    }

    /**
     * Metodi asettaa yhden tason solmut ympyrään taustakuvan keskipisteen ympärille. Asettaa solmut siten, että ympyrässä seuraava solmu vastapäivään on solmun oikea sisar.
     *
     * @param tasonSolmut ympyrään asetettavat solmut.
     * @param sade        muodostettavan ympyrän säde.
     */
    private void asetaTasonSolmujenSijainnit(ArrayList<Solmu> tasonSolmut, boolean toinenTaso, float sade) {
        final float keskiX = (int) keskipiste.x;
        final float keskiY = (int) keskipiste.y;
        int solmuja = tasonSolmut.size();

        final double kulma = Math.toRadians(360 / solmuja);
        double k = kulma;
        Solmu s = tasonSolmut.get(0);

        if (toinenTaso) {
            s = tasonSolmut.get(solmuja - 1);
        }

        for (int i = 0; i < solmuja; i++) {
            double x = (sade * Math.cos(k)) + keskiX;
            double y = (sade * Math.sin(k) + keskiY);

            Gdx.app.log("Verkko", "Solmun " + i + " sijainti: " + x + " " + y);
            s.setSijainti((float) x, (float) y);
            asetaSolmulleKulmaKeskipisteeseen(s);
            s = s.getOikeaSisarus();
            k += kulma;
        }
    }

    /**
     * Asettaa solmulle kulman kohti keskipistettä.
     *
     * @param solmu
     */
    private void asetaSolmulleKulmaKeskipisteeseen(Solmu solmu) {
        Vector2 lahto = new Vector2(solmu.getXKoordinaatti(), solmu.getYKoordinaatti());
        float angleToPoint = (float) Math.toDegrees(Math.atan2(SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2 - lahto.y, SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2 - lahto.x));

        solmu.setKulma(angleToPoint - 90f);
    }

    public ArrayList<Solmu> getSolmut() {
        return solmut;
    }

    /**
     * @param x peliavaruuden oikea x
     * @param y peliavaruuden oikea y
     * @return onko tarpeeksi lähellä jotain solmua
     */
    public boolean kosketusTarpeeksiLahelleJotainSolmua(float x, float y) {
        jarjestaSolmutEtaisyydenMukaan(x, y);

        if (lahimmanSolmunEtaisyys < 250) {
            return true;
        }

        return false;
    }

    /**
     * Järjestää solmut etäisyyden mukaan. Järjestelyperusteena etäisyys annettuihin koordinaatteihin.
     *
     * @param x peliavaruuden oikea x
     * @param y peliavaruuden oikea y
     */
    public void jarjestaSolmutEtaisyydenMukaan(float x, float y) {
        float[] etaisyydet = laskeEtaisyydet(x, y);

        for (int i = 0; i < solmut.size(); i++) {
            etsiSolmunPaikka(i, etaisyydet);
        }
    }

    /**
     * Päivittää solmujen paikat listaan solmujenJarjestys... sekä luokkamuuttujat lahimmanSolmunEtaisyys ja edellistaKosketustaLahinSolmu.
     *
     * @param solmunInd  tarkasteltavan solmun indeksi listassa solmut
     * @param etaisyydet taulukko solmujen etaisyyksista viimeiseen kosketukseen
     */
    private void etsiSolmunPaikka(int solmunInd, float[] etaisyydet) {
        float etaisyys = etaisyydet[solmunInd];
        int solmunPaikka = 0;
        for (int i = 0; i < etaisyydet.length; i++) {
            if (i == solmunInd) continue;

            if (etaisyys > etaisyydet[i]) {
                solmunPaikka++;
            }
        }

        if (solmunPaikka == 0) {
            lahimmanSolmunEtaisyys = etaisyys;
            edellistaKosketustaLahinSolmu = solmut.get(solmunInd);
        }

        solmujenJarjestysViimeisenKosketuksenEtaisyydenMukaan[solmunInd] = solmunPaikka;
    }

    /**
     * laskee solmujen etaisyydet annetuista koordinaateista
     *
     * @param x
     * @param y
     * @return taulukko etaisyyksista, indeksit samat kuin listassa solmut
     */
    private float[] laskeEtaisyydet(float x, float y) {
        float[] etaisyydet = new float[solmut.size()];

        for (int i = 0; i < solmut.size(); i++) {
            Solmu solmu = solmut.get(i);

            float etaisyys = (float) Math.hypot(solmu.getXKoordinaatti() - x, solmu.getYKoordinaatti() - y);

            etaisyydet[i] = etaisyys;
        }

        return etaisyydet;
    }

    public Solmu annaEdellistaKosketustaLahinSolmu() {
        return edellistaKosketustaLahinSolmu;
    }

    /**
     * Palauttaa kosketusta lähimmän solmun, joka ei ole keskipiste.
     *
     * @return
     */
    public Solmu annaKosketustaLahinSolmuEiKeskipistetta() {
        int etsittavaIndeksi = 0;

        if (solmujenJarjestysViimeisenKosketuksenEtaisyydenMukaan[0] == 0) {
            etsittavaIndeksi = 1;
        }

        Solmu s = null;
        for (int i = 0; i < solmut.size(); i++) {
            if (solmujenJarjestysViimeisenKosketuksenEtaisyydenMukaan[i] == etsittavaIndeksi) {
                s = solmut.get(i);
                break;
            }
        }
        return s;
    }

}
