package fi.ymcafinland.demo.logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import java.util.ArrayList;

/**
 * Luokka kuvaa selviytyjän purjeissa olevia "palloja", joissa on tietoa selviytymiskeinoista.
 * Esimerkiksi kuusi eri selviytymistyyppiä (Fyysinen, Älyllinen...) saavat omat solmunsa.
 * Jokaisella solmulla on pohjustamisen jälkeen sijainti, linkit muihin naapurisolmuihin (mutsi, sisarukset, lapset)
 * ja kieliversion mukaiset tekstit.
 */
public class Solmu {

    private final String id;
    private  Solmu mutsi;
    private String otsikko;
    private String sisalto;
    private Solmu vasenSisarus;
    private Solmu oikeaSisarus;
    private ArrayList<Solmu> lapset;
    private float x;
    private float y;
    private float kulma;
    private String dialoginSisalto;

    /**
     * Luo solmun, jolla on tunnusluku ja vanhempi.
     *
     * @param id    yksilöllinen luku, 1-24.
     * @param mutsi solmun vanhempi.
     */
    public Solmu(String id, Solmu mutsi) {
        this.id = id;
        this.mutsi = mutsi;
        lapset = new ArrayList<>();
        vasenSisarus = null;
        oikeaSisarus = null;
        sisalto = "Omia selviytymiskeinoja pystyy aina vahvistamaan!";
        otsikko = "ID: " + id;
        x = 0;
        y = 0;
    }

    public void setLapset(ArrayList<Solmu> lapsoset) {
        this.lapset = lapsoset;
    }

    public ArrayList<Solmu> getLapset() {
        return lapset;
    }

    public void setVasenSisarus(Solmu solmu) {
        vasenSisarus = solmu;
    }

    public String getMinikuvanNimi() {
        return "mini_" + id;
    }

    public String getTaustakuvanNimi() {
        return "tausta" + id;
    }

    public void setOikeaSisarus(Solmu solmu) {
        oikeaSisarus = solmu;
    }

    public Solmu getVasenSisarus() {
        return vasenSisarus;
    }

    public Solmu getOikeaSisarus() {
        return oikeaSisarus;
    }

    public Solmu getMutsi() {
        return mutsi;
    }
    public void setMutsi(Solmu solmu){mutsi = solmu;}

    public void setSijainti(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getXKoordinaatti() {
        return x;
    }

    public float getYKoordinaatti() {
        return y;
    }

    public String getID() {
        return id;
    }

    public void setOtsikko(String name) {
        otsikko = name;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    public String getSisalto() {
        return sisalto;
    }


    public void setKulma(float kulma) {
        this.kulma = kulma;
    }

    public float getKulma() {
        return this.kulma;
    }

    public String getDialoginSisalto() {
        return dialoginSisalto;
    }

    public void setDialoginSisalto(String dialoginSisalto) {
        this.dialoginSisalto = dialoginSisalto;
    }

}
