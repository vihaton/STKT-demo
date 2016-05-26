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
    private float[] selviytyisArvot;
    private int[] keinojenIndeksitJarjestyksessa;

    private String nimi;

    /**
     * Konstruktori
     */
    public Pelaaja() {
        this.nimi = "Seini Selviytyjä";
        selviytyisArvot = new float[]{7f, 7f, 7f, 7f, 7f, 7f};
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
        keinojenIndeksitJarjestyksessa = new int[6];

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
        keinojenIndeksitJarjestyksessa = new int[6];
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

    public void lisaaVastaus(Vaittama v) {
        lisaaVastauksia(1);
        vastatutVaittamat.add(v.hashCode());
    }

    public void lisaaVastauksia(int maara) {
        vastausmaara += maara;
    }

    public void lisaaSelviytymisarvoIndeksissa(int indeksissa, float maara) {
        selviytyisArvot[indeksissa] += maara;
    }

    public void setSelviytymisarvoaIndeksissa(int i, float arvo) {
        selviytyisArvot[i] = arvo;
    }

    public int getMaxSelviytymisenIndeksi() {
        int maxID = 0;
        float maxArvo = selviytyisArvot[maxID];
        for (int i = 1; i < selviytyisArvot.length; i++) {
            if (maxArvo < selviytyisArvot[i]) {
                maxID = i;
            }
        }
        return maxID;
    }

    /**
     * Järjestää pelaajan selviytymiskeinot paremmuusjärjestykseen.
     */
    private void jarjestaSelviytymisarvot() {
        //todo fix bug
        for (int i = 0; i < 6; i++) {       //jokaista selviytymiskeinoa...
            int isompia = 0;
            float selviytymisarvo = selviytyisArvot[i];
            for (int j = 0; j < 6; j++) {   //...verrataan kaikkiin muihin...
                if (i == j) continue;       //...paitsi itseensä...
                if (selviytymisarvo <= selviytyisArvot[j])
                    isompia++;              // ...ja käydään läpi kuinka monta isompaa arvoa on
            }
            keinojenIndeksitJarjestyksessa[isompia] = i; //lopuksi lisätään tarkasteltu selviytymiskeino omalle paikalleen
        }
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
        return "Fyysinen: " + String.format("%.1f", selviytyisArvot[FYYSINEN]) + "\n" +
                "Älyllinen: " + String.format("%.1f", selviytyisArvot[ALYLLINEN]) + "\n" +
                "Eettinen: " + String.format("%.1f", selviytyisArvot[EETTINEN]) + "\n" +
                "Tunteellinen: " + String.format("%.1f", selviytyisArvot[TUNTEELLINEN]) + "\n" +
                "Sosiaalinen: " + String.format("%.1f", selviytyisArvot[SOSIAALINEN]) + "\n" +
                "Luova: " + String.format("%.1f", selviytyisArvot[LUOVA]) + "\n";
    }

    public boolean onkoVastannut(Vaittama v) {
        return vastatutVaittamat.contains(v.hashCode());
    }

    public float getSelviytymisarvo(int selviytymisaronIndeksi) {
        return selviytyisArvot[selviytymisaronIndeksi];
    }

    public void setVaittamienMaara(int vaittamienMaara) {
        this.vaittamienMaara = vaittamienMaara;
    }

}
