package fi.ymcafinland.demo.logiikka;

import java.util.HashSet;

/**
 * Pelaaja -luokalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin.
 *
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
    protected float[] selviytyisArvot;
    private int vastausmaara;
    private HashSet<Integer> vastatutVaittamat;

    private String nimi;

    /**
     * Konstruktori
     */
    public Pelaaja() {
        this.nimi = "Seini Selviytyjä";
        selviytyisArvot = new float[]{7f, 7f, 7f, 7f, 7f, 7f};
        vastausmaara = 0;
        vastatutVaittamat = new HashSet<>();
    }

    /**
     * @return prosenttiluvun (0-100)
     */
    public int getVastausprosentti() {
        return 50;
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
