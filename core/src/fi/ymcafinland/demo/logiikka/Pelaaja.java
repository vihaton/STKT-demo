package fi.ymcafinland.demo.logiikka;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sasu on 11.4.2016.
 */
public class Pelaaja {
    private String nimi;
    float[] selviytyisArvot;

    /**
     * Pelaajalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin.
     */

    public Pelaaja() {
        this.nimi = "Seini Selviytyjä";
        selviytyisArvot = new float[]{1f, 1f, 1f, 1f, 1f, 1f};
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
    public void lisaaSelviytymisarvoIndeksissa(int i, float maara) {
        selviytyisArvot[i] += maara;
    }

    public void lisaaFyysista(float maara) {
        lisaaSelviytymisarvoIndeksissa(0, maara);
    }

    public void lisaaAlyllista(float maara) {
        lisaaSelviytymisarvoIndeksissa(1, maara);
    }

    public void lisaaEettista(float maara) {
        lisaaSelviytymisarvoIndeksissa(2, maara);
    }

    public void lisaaTunteellista(float maara) {
        lisaaSelviytymisarvoIndeksissa(3, maara);
    }

    public void lisaaSosiaalista(float maara) {
        lisaaSelviytymisarvoIndeksissa(4, maara);
    }

    public void lisaaLuovuutta(float maara) {
        lisaaSelviytymisarvoIndeksissa(5, maara);
    }

    public double getFyysinen() {
        return selviytyisArvot[0];
    }

    public double getAlyllinen() {
        return selviytyisArvot[1];
    }

    public double getEettinen() {
        return selviytyisArvot[2];
    }

    public double getTunteellinen() {
        return selviytyisArvot[3];
    }

    public double getSosiaalinen() {
        return selviytyisArvot[4];
    }

    public double getLuova() {
        return selviytyisArvot[5];
    }

    public void setFyysinen(float fyysinen) {
        selviytyisArvot[0] = fyysinen;
    }

    public void setAlyllinen(float alyllinen) {
        selviytyisArvot[1] = alyllinen;
    }

    public void setEettinen(float eettinen) {
        selviytyisArvot[2] = eettinen;
    }

    public void setTunteellinen(float tunteellinen) {
        selviytyisArvot[3] = tunteellinen;
    }

    public void setSosiaalinen(float sosiaalinen) {
        selviytyisArvot[4] = sosiaalinen;
    }

    public void setLuova(float luova) {
        selviytyisArvot[5] = luova;
    }

    //ToDo maxselviytyminen
    public String getMaxSelviytyminen() {
        return "";
    }

    @Override
    public String toString() {
        return "Fyysinen: " + selviytyisArvot[0] + "\n" +
                "Älyllinen: " + selviytyisArvot[1] + "\n" +
                "Eettinen: " + selviytyisArvot[2] + "\n" +
                "Tunteellinen: " + selviytyisArvot[3] + "\n" +
                "Sosiaalinen: " + selviytyisArvot[4] + "\n" +
                "Luova: " + selviytyisArvot[5] + "\n";
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
