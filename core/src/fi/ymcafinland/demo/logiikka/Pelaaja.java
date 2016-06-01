package fi.ymcafinland.demo.logiikka;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Pelaaja -luokalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin.
 * <p/>
 * indeksit:
 * 0 - Fyysinen
 * 1 - Älyllinen
 * 2 - Eettinen
 * 3 - Tunteellinen
 * 4 - Sosiaalinen
 * 5 - Luova
 */
public class Pelaaja {

    public static final int FYYSINEN = 0;
    public static final int ALYLLINEN = 1;
    public static final int EETTINEN = 2;
    public static final int TUNTEELLINEN = 3;
    public static final int SOSIAALINEN = 4;
    public static final int LUOVA = 5;

    private int vaittamienMaara = 1;
    private int vastausmaara;
    private HashSet<Integer> vastatutVaittamat;
    private String[] selviytymiskeinot;
    private float[] selviytymisarvot;
    private Integer[] keinojenIndeksitJarjestyksessa;

    private String nimi;

    /**
     * Konstruktori
     */
    public Pelaaja() {
        this.nimi = "Seini Selviytyjä";
        float alkuarvo = 15f;
        selviytymisarvot = new float[]{alkuarvo, alkuarvo, alkuarvo, alkuarvo, alkuarvo, alkuarvo};
        vastausmaara = 0;
        vastatutVaittamat = new HashSet<>();
//        lueSelviytymiskeinot();
        kirjoitaSelviytymiskeinot();
    }

    /**
     * Väliaikainen ratkaisu,kunnes @lueSelviytymiskeinot on korjattu.
     */
    private void kirjoitaSelviytymiskeinot() {
        selviytymiskeinot = new String[6];
        keinojenIndeksitJarjestyksessa = new Integer[6];

        selviytymiskeinot[0] = "Fyysinen selviytyjä";
        selviytymiskeinot[1] = "Älyllinen selviytyjä";
        selviytymiskeinot[2] = "Eettinen selviytyjä";
        selviytymiskeinot[3] = "Tunteellinen selviytyjä";
        selviytymiskeinot[4] = "Sosiaalinen selviytyjä";
        selviytymiskeinot[5] = "Luova selviytyjä";

        for (int i = 0; i < 6; i++) {
            keinojenIndeksitJarjestyksessa[i] = i;
        }
    }

    //todo fixfix
    private void lueSelviytymiskeinot() {
        selviytymiskeinot = new String[6];
        keinojenIndeksitJarjestyksessa = new Integer[6];
        FileHandle fh = new FileHandle("solmujentekstit/solmut");
        I18NBundle myBundle = I18NBundle.createBundle(fh);

        for (int i = 1; i < 7; i++) {
            String selviytyja = myBundle.format("solmun_otsikko_" + i);
            selviytymiskeinot[i - 1] = selviytyja;
            keinojenIndeksitJarjestyksessa[i - 1] = i - 1;
        }
    }

    /**
     * @return prosenttiluvun (0-100)
     */
    public int getVastausprosentti() {
        return (vastausmaara * 100 + 50) / vaittamienMaara;
    }

    public void lisaaVastaus(SliderVaittama v) {
        lisaaVastauksia(1);
        vastatutVaittamat.add(v.hashCode());
    }

    public void lisaaVastauksia(int maara) {
        vastausmaara += maara;
    }

    public void lisaaSelviytymisarvoIndeksissa(int indeksissa, float maara) {
        selviytymisarvot[indeksissa] += maara;
    }

    public void setSelviytymisarvoaIndeksissa(int i, float arvo) {
        selviytymisarvot[i] = arvo;
    }

    public int getMaxSelviytymisenIndeksi() {
        int maxID = 0;
        float maxArvo = selviytymisarvot[maxID];
        for (int i = 1; i < selviytymisarvot.length; i++) {
            if (maxArvo < selviytymisarvot[i]) {
                maxID = i;
            }
        }
        return maxID;
    }

    /**
     * Järjestää pelaajan selviytymiskeinot paremmuusjärjestykseen.
     */
    private void jarjestaSelviytymisarvot() {
        //todo varmistu toiminnan oikeellisuudesta
        float[] kopio = selviytymisarvot.clone();

        for (int i = 0; i < 6; i++) {
            int kopionSuurin = etsiSuurimmanIndeksi(kopio);
            kopio[kopionSuurin] = Float.MIN_VALUE;
            keinojenIndeksitJarjestyksessa[i] = kopionSuurin;
        }
    }

    private int etsiSuurimmanIndeksi(float [] lista) {
        float suurin = 0;
        int suurimmanIndeksi = 0;

        for (int i = 0; i < lista.length; i++) {
            if (lista[i] == Float.MIN_VALUE) {
                continue;
            }
            if (suurin < lista[i]) {
                suurin = lista[i];
                suurimmanIndeksi = i;
            }
        }
        return suurimmanIndeksi;
    }

    public int getIndeksiJarjestetystaListasta(int i){

        return keinojenIndeksitJarjestyksessa[i];
    }

    /**
     * @return järjestetty lista selviytymiskeinoista
     */
    public ArrayList<String> getSelviytymiskeinotJarjestyksessa() {
        jarjestaSelviytymisarvot();

        ArrayList<String> jarjestettyLista = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            jarjestettyLista.add(selviytymiskeinot[keinojenIndeksitJarjestyksessa[i]]);
        }

        return jarjestettyLista;
    }

    public String[] getSelviytymiskeinot() {
        return selviytymiskeinot;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String valuesToString() {
        return "Fyysinen: " + String.format("%.1f", selviytymisarvot[FYYSINEN]) + "\n" +
                "Älyllinen: " + String.format("%.1f", selviytymisarvot[ALYLLINEN]) + "\n" +
                "Eettinen: " + String.format("%.1f", selviytymisarvot[EETTINEN]) + "\n" +
                "Tunteellinen: " + String.format("%.1f", selviytymisarvot[TUNTEELLINEN]) + "\n" +
                "Sosiaalinen: " + String.format("%.1f", selviytymisarvot[SOSIAALINEN]) + "\n" +
                "Luova: " + String.format("%.1f", selviytymisarvot[LUOVA]) + "\n";
    }

    public boolean onkoVastannut(SliderVaittama v) {
        return vastatutVaittamat.contains(v.hashCode());
    }

    public float getSelviytymisprosentit(int selviytymisarvonIndeksi) {
        float kaikkiarvot = 0;
        float lisattava = 0;
        if(selviytymisarvot[keinojenIndeksitJarjestyksessa[5]] < 0) {
            lisattava = -selviytymisarvot[keinojenIndeksitJarjestyksessa[5]];
        }
        float[] palautettavatArvot = selviytymisarvot;
        for (int i = 0; i < 6; i++) {
            palautettavatArvot[i] += lisattava;
            kaikkiarvot += palautettavatArvot[i];
        }
        return palautettavatArvot[selviytymisarvonIndeksi]/kaikkiarvot * 100;
    }

    public void setVaittamienMaara(int vaittamienMaara) {
        this.vaittamienMaara = vaittamienMaara;
    }

}
