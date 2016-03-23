package logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Selviytyjän purjeiden solmujen kokoelma. Vastaa solmujen luomisesta ja ylläpidosta.
 */
public class Verkko {

    private ArrayList<Solmu> solmut;
    private I18NBundle myBundle;

    public Verkko() {
        solmut = new ArrayList<>();
        boolean exists = Gdx.files.isLocalStorageAvailable();

        FileHandle baseFileHandle = Gdx.files.internal("solmut/solmut");
        myBundle = I18NBundle.createBundle(baseFileHandle);

//        //englanninkielisen version testaamiseen
//        Locale locale = new Locale("en");
//        myBundle = I18NBundle.createBundle(baseFileHandle, locale);
        generoiSolmut();
    }

    private void generoiSolmut() {

        /*
        TODO metodi, joka luo selviytyjän purjeiden solmut.
        Solmusta 1 lähtien: luodaan solmu ja sen lapset,
        asetetaan lapsisolmut mutsin lapsiksi ja lapset toistensa sisaruksiksi, sen jälkeen jatketaan rekursiivisesti
        lapsiin, kunnes solmuja ei enää ole. Tiedot sukulaisuuksista, tekstit ym tiedot luetaan toisesta tiedostosta.
         */

        ArrayDeque<Solmu> jono = luoEnsimmainenTaso(6);
        ArrayList<Solmu> toinenTaso = new ArrayList<>();
//        for (Solmu s:jono) {
//            toinenTaso.addAll(luoLapset(s));
//        }
//        asetaTasonSolmutToistensaSisaruksiksi(toinenTaso);

        jono.addAll(toinenTaso);
        while (!jono.isEmpty()) {
            Solmu s = jono.pollFirst();
            String otsikko = myBundle.format("solmun_otsikko_" + s.getID());
            s.setOtsikko(otsikko);
            String sisalto = myBundle.format("solmun_sisalto_" + s.getID());
            s.setSisalto(sisalto);
        }
    }

    private ArrayDeque<Solmu> luoEnsimmainenTaso(int montako) {
        ArrayDeque<Solmu> jono = new ArrayDeque<>();

        for (int i = 1; i < montako + 1; i++) {
            Solmu s = new Solmu("" + i, null);
            solmut.add(s);
        }

        asetaTasonSolmutToistensaSisaruksiksi(solmut);

        jono.addAll(solmut);
        return jono;
    }

    private void asetaTasonSolmutToistensaSisaruksiksi(ArrayList<Solmu> tasonSolmut) {
        int montako = tasonSolmut.size();

        //paritetaan ensimmäinen ja viimeinen solmu
        Solmu vasen = solmut.get(montako - 1);
        Solmu s = solmut.get(0);
        asetaSisaruksiksi(s, vasen);

        for (int i = 1; i < montako; i++) {
            vasen = solmut.get(i - 1);
            s = solmut.get(i);
            asetaSisaruksiksi(s, vasen);
        }
    }

    private void asetaSisaruksiksi(Solmu s, Solmu vasen) {
        s.setVasenSisarus(vasen);
        vasen.setOikeaSisarus(s);
    }

    private ArrayList<Solmu> luoLapset(Solmu s) {
        int mutsinID = Integer.parseInt(s.getID());
        int lapsenID = 7 + (mutsinID - 1) * 3;

        ArrayList<Solmu> lapset = new ArrayList<>();
        for (int i = lapsenID; i < lapsenID + 3; i++) {
            Solmu lapsi = new Solmu("" + i, s);
            lapset.add(lapsi);
        }
        return lapset;
    }

    public ArrayList<Solmu> getSolmut() {
        return solmut;
    }

}
