package logiikka;

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

    private final int id;
    private final Solmu mutsi;
    private String otsikko;
    private String sisalto;
    private Solmu vasenSisarus;
    private Solmu oikeaSisarus;
    private ArrayList<Solmu> lapset;
    private int x;
    private int y;

    /**
     * Luo solmun, jolla on tunnusluku ja vanhempi.
     *
     * @param id    yksilöllinen kaksinumeroinen luku, esim 04.
     * @param mutsi solmun vanhempi.
     */
    public Solmu(int id, Solmu mutsi) {
        this.id = id;
        this.mutsi = mutsi;
        lapset = new ArrayList<Solmu>();
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

    public void setOikeaSisarus(Solmu solmu) {
        oikeaSisarus = solmu;
    }

    public Solmu getVasenSisarus() {
        return vasenSisarus;
    }

    public Solmu getOikeaSisarus() {
        return oikeaSisarus;
    }

    public void setSijainti(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXKoordinaatti() {
        return x;
    }

    public int getYKoordinaatti() {
        return y;
    }

    public int getID() {
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

}