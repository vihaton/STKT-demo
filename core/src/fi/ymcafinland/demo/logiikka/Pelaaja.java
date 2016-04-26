package fi.ymcafinland.demo.logiikka;

import java.util.ArrayList;
import java.util.HashMap;

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
    int vastausmaara;

    private String nimi;

    /**
     * Konstruktori
     */
    public Pelaaja() {
        this.nimi = "Seini Selviytyjä";
        selviytyisArvot = new float[]{1f, 1f, 1f, 1f, 1f, 1f};
        vastausmaara = 0;
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
    public int getVastausmaara(){
        return vastausmaara;
    }
    public void lisaaVastaus(){
        vastausmaara++;
    }
    public void lisaaSelviytymisarvoIndeksissa(int i, float maara) {
        selviytyisArvot[i] += maara;
    }

    public void lisaaFyysista(float maara) {
        lisaaSelviytymisarvoIndeksissa(FYYSINEN, maara);
    }

    public void lisaaAlyllista(float maara) {
        lisaaSelviytymisarvoIndeksissa(ALYLLINEN, maara);
    }

    public void lisaaEettista(float maara) {
        lisaaSelviytymisarvoIndeksissa(EETTINEN, maara);
    }

    public void lisaaTunteellista(float maara) {
        lisaaSelviytymisarvoIndeksissa(TUNTEELLINEN, maara);
    }

    public void lisaaSosiaalista(float maara) {
        lisaaSelviytymisarvoIndeksissa(SOSIAALINEN, maara);
    }

    public void lisaaLuovuutta(float maara) {
        lisaaSelviytymisarvoIndeksissa(LUOVA, maara);
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

    public void setFyysinen(float fyysinen) {
        selviytyisArvot[FYYSINEN] = fyysinen;
    }

    public void setAlyllinen(float alyllinen) {
        selviytyisArvot[ALYLLINEN] = alyllinen;
    }

    public void setEettinen(float eettinen) {
        selviytyisArvot[EETTINEN] = eettinen;
    }

    public void setTunteellinen(float tunteellinen) {
        selviytyisArvot[TUNTEELLINEN] = tunteellinen;
    }

    public void setSosiaalinen(float sosiaalinen) {
        selviytyisArvot[SOSIAALINEN] = sosiaalinen;
    }

    public void setLuova(float luova) {
        selviytyisArvot[LUOVA] = luova;
    }

    //ToDo maxselviytyminen
    public String getMaxSelviytyminen() {
        return "";
    }

    @Override
    public String toString() {
        return "Fyysinen: " + String.format("%.1f", selviytyisArvot[FYYSINEN]) + "\n" +
                "Älyllinen: " + String.format("%.1f", selviytyisArvot[ALYLLINEN]) + "\n" +
                "Eettinen: " + String.format("%.1f", selviytyisArvot[EETTINEN]) + "\n" +
                "Tunteellinen: " + String.format("%.1f", selviytyisArvot[TUNTEELLINEN]) + "\n" +
                "Sosiaalinen: " + String.format("%.1f", selviytyisArvot[SOSIAALINEN]) + "\n" +
                "Luova: " + String.format("%.1f", selviytyisArvot[LUOVA]) + "\n";
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
