package fi.ymcafinland.demo.logiikka;

import java.util.HashSet;

/**
 * Pelaaja -luokalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin.
 */
public class Pelaaja {

    public final int FYYSINEN = 0;
    public final int ALYLLINEN = 1;
    public final int EETTINEN = 2;
    public final int TUNTEELLINEN = 3;
    public final int SOSIAALINEN = 4;
    public final int LUOVA = 5;

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
     * indeksit:
     * 0 - Fyysinen
     * 1 - Älyllinen
     * 2 - Eettinen
     * 3 - Tunteellinen
     * 4 - Sosiaalinen
     * 5 - Luova
     */
    //ToDo vastausmaara prosentteina kaikista vastauksista. (jos 200 väittämää ja olet vastannut sataan niin palauttaa 50)
    public int getVastausmaara() {
        return vastausmaara;
    }

    public void lisaaVastaus(Vaittama v) {
        vastausmaara++;
        vastatutVaittamat.add(v.hashCode());
    }

    public void lisaaSelviytymisarvoIndeksissa(int i, float maara) {
        selviytyisArvot[i] += maara;
    }

    public void setSelviytymisarvoaIndeksissa(int i, float arvo) {
        selviytyisArvot[i] = arvo;
    }

    public float getFyysinen() {
        return selviytyisArvot[FYYSINEN];
    }

    public float getAlyllinen() {
        return selviytyisArvot[ALYLLINEN];
    }

    public float getEettinen() {
        return selviytyisArvot[EETTINEN];
    }

    public float getTunteellinen() {
        return selviytyisArvot[TUNTEELLINEN];
    }

    public float getSosiaalinen() {
        return selviytyisArvot[SOSIAALINEN];
    }

    public float getLuova() {
        return selviytyisArvot[LUOVA];
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
}
