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

    private final String id;
    private final Solmu mutsi;
    private String otsikko;
    private String sisalto;
    private Solmu vasenSisarus;
    private Solmu oikeaSisarus;
    private ArrayList<Solmu> lapset;
    private float x;
    private float y;

    /**
     * Luo solmun, jolla on tunnusluku ja vanhempi.
     *
     * @param id    yksilöllinen luku, 1-24.
     * @param mutsi solmun vanhempi.
     */
    public Solmu(String id, Solmu mutsi) {
        this.id = id;
        this.mutsi = mutsi;
        lapset = new ArrayList();
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

    public Solmu getMutsi(){ return mutsi; }

    public void setSijainti(int x, int y) {
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


}
