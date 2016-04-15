package fi.ymcafinland.demo.logiikka;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sasu on 11.4.2016.
 */
public class Pelaaja {
    private String nimi;
    HashMap<String, Float> mappi;
    private double fyysinen;
    private double eettinen;
    private double luova;
    private double alyllinen;
    private double sosiaalinen;
    private double tunteellinen;

    /**
     * Pelaajalla on selviytymisarvosanat joihin vaikutetaan vastaamalla väittämiin
     */

    public Pelaaja(){
        this.nimi = "Seini Selviytyjä";
        this.alyllinen =0;
        this.eettinen =0;
        this.fyysinen = 0;
        this.luova = 0;
        this.sosiaalinen = 0;
        this.tunteellinen = 0;
//        mappi.put("Fyysinen", fyysinen);
//        mappi.put("Eettinen", eettinen);
//        mappi.put("Luova", luova);
//        mappi.put("Älyllinen", alyllinen);
//        mappi.put("Sosiaalinen", sosiaalinen);
//        mappi.put("Tunteellinen", tunteellinen);



    }

    public void lisaaEettista(float maara){
        this.eettinen += maara;
    }
    public void lisaaAlyllista(float maara){
        this.alyllinen += maara;
    }
    public void lisaaFyysista(float maara){
        this.fyysinen += maara;
    }
    public void lisaaLuovuutta(float maara){
        this.luova += maara;
    }
    public void lisaaSosiaalista(float maara){
        this.sosiaalinen += maara;
    }
    public void lisaaTunteellista(float maara){
        this.tunteellinen += maara;
    }
    public double getEettinen() {
        return eettinen;
    }
    public void setEettinen(float eettinen) {
        this.eettinen = eettinen;
    }

    public double getFyysinen() {
        return fyysinen;
    }
    public void setFyysinen(float fyysinen) {
        this.fyysinen = fyysinen;
    }

    public double getLuova() {
        return luova;
    }

    public void setLuova(float luova) {
        this.luova = luova;
    }

    public double getAlyllinen() {
        return alyllinen;
    }

    public void setAlyllinen(float alyllinen) {
        this.alyllinen = alyllinen;
    }

    public double getSosiaalinen() {
        return sosiaalinen;
    }

    public void setSosiaalinen(float sosiaalinen) {
        this.sosiaalinen = sosiaalinen;
    }

    public double getTunteellinen() {
        return tunteellinen;
    }

    public void setTunteellinen(float tunteellinen) {
        this.tunteellinen = tunteellinen;
    }
    //ToDo maxselviytyminen
    public String getMaxSelviytyminen(){
     return"";
    }

    @Override
    public String toString() {
        return  "Fyysinen: " + fyysinen + "\n" +
                "Eettinen: " + eettinen +  "\n" +
                "Luova: " + luova +  "\n" +
                "Älyllinen: " + alyllinen +  "\n" +
                "Sosiaalinen: " + sosiaalinen +  "\n" +
                "Tunteellinen: " + tunteellinen +  "\n";
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
