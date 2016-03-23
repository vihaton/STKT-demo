package logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import java.util.ArrayDeque;
import java.util.ArrayList;

import sun.rmi.runtime.Log;

/**
 * Selviytyjän purjeiden solmujen kokoelma. Vastaa solmujen luomisesta ja ylläpidosta.
 */
public class Verkko {

    private ArrayList<Solmu> solmut;

    public Verkko() {
        solmut = new ArrayList<>();
        generoiSolmut();
    }

    private void generoiSolmut() {

        /*
        TODO metodi, joka luo selviytyjän purjeiden solmut.
        Solmusta 01 lähtien: luodaan solmu ja sen lapset,
        asetetaan lapsisolmut mutsin lapsiksi ja lapset toistensa sisaruksiksi, sen jälkeen jatketaan rekursiivisesti
        lapsiin, kunnes solmuja ei enää ole. Tiedot sukulaisuuksista, tekstit ym tiedot luetaan toisesta tiedostosta.
         */

        ArrayDeque<Solmu> jono = luoEnsimmainenTaso(6);


    }

    private ArrayDeque<Solmu> luoEnsimmainenTaso(int montako) {
        ArrayDeque<Solmu> jono = new ArrayDeque<>();

        for (int i = 1; i < montako + 1; i++) {
            Solmu s = new Solmu(i, null);
            solmut.add(s);
        }

        Solmu vasen = solmut.get(montako);
        Solmu s = solmut.get(0);

        asetaSisaruksiksi(s, vasen);

        for (int i = 1; i < montako; i++) {
            vasen = solmut.get(i-1);
            s = solmut.get(i);
            asetaSisaruksiksi(s, vasen);
        }

        jono.addAll(solmut);
        return jono;
    }

    private void asetaSisaruksiksi(Solmu s, Solmu vasen) {
        s.setVasenSisarus(vasen);
        vasen.setOikeaSisarus(s);
    }

}
