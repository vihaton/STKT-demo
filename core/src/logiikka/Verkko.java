package logiikka;

/*
 * Created by xvixvi on 20.3.2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxBuild;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

/**
 * Selviytyjän purjeiden solmujen kokoelma. Vastaa solmujen luomisesta ja ylläpidosta.
 */
public class Verkko {

    private ArrayList<Solmu> solmut;
    private I18NBundle myBundle;

    public Verkko() {
        solmut = new ArrayList<>();
        String polkuTiedostolle = "solmut/solmut";
        FileHandle baseFileHandle = Gdx.files.internal(polkuTiedostolle);
        try {
            myBundle = I18NBundle.createBundle(baseFileHandle);
        } catch (Exception e) {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("android/assets/" + polkuTiedostolle));
        }
//        //englanninkielisen version testaamiseen
//        Locale locale = new Locale("en");
//        myBundle = I18NBundle.createBundle(baseFileHandle, locale);
        generoiSolmut();
    }

    private void generoiSolmut() {

        solmut.addAll(luoEnsimmainenTaso(6));

        ArrayList<Solmu> toinenTaso = new ArrayList<>();
        for (Solmu s : solmut) {
            toinenTaso.addAll(luoLapset(s));
        }
        asetaTasonSolmutToistensaSisaruksiksi(toinenTaso);
        solmut.addAll(toinenTaso);

        asetaOtsikotJaSisallot();

        asetaSijainnit();
    }

    private ArrayList<Solmu> luoEnsimmainenTaso(int montako) {
        ArrayList<Solmu> lista = new ArrayList<>();

        for (int i = 1; i < montako + 1; i++) {
            Solmu s = new Solmu("" + i, null);
            solmut.add(s);
        }

        asetaTasonSolmutToistensaSisaruksiksi(solmut);

        lista.addAll(solmut);
        return lista;
    }

    private void asetaTasonSolmutToistensaSisaruksiksi(ArrayList<Solmu> tasonSolmut) {
        int montako = tasonSolmut.size();

        //paritetaan ensimmäinen ja viimeinen solmu
        Solmu vasen = tasonSolmut.get(montako - 1);
        Solmu s = tasonSolmut.get(0);
        asetaSisaruksiksi(s, vasen);

        for (int i = 1; i < montako; i++) {
            vasen = tasonSolmut.get(i - 1);
            s = tasonSolmut.get(i);
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

    private void asetaOtsikotJaSisallot() {
        for (Solmu s : solmut) {
            String otsikko = myBundle.format("solmun_otsikko_" + s.getID());
            s.setOtsikko(otsikko);
            String sisalto = myBundle.format("solmun_sisalto_" + s.getID());
            s.setSisalto(sisalto);
        }
    }

    private void asetaSijainnit() {
        //TODO aseta solmuille sijainnit
    }

    public ArrayList<Solmu> getSolmut() {
        return solmut;
    }

}
