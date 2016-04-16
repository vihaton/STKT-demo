package fi.ymcafinland.demo.logiikka;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by xvixvi on 16.4.2016.
 * <p/>
 * Vaittamat vastaa väittämien luomisesta, kun sovellus käynnistetään, sekä väittämien ylläpidosta.
 */
public class Vaittamat {

    private HashMap<String, ArrayList<Vaittama>> karttaSolmujenVaittamista;

    public Vaittamat() {
        Scanner lukija = luoLukija("vaittamat.csv");

        if (lukija != null) {
            generoiVaittamat(lukija);
        }
    }

    private Scanner luoLukija(String tiedostonNimi) {
        Scanner lukija = null;

        try {
            FileHandle file = Gdx.files.internal(tiedostonNimi);
            lukija = new Scanner(file.read());
        } catch (Exception e) {
            int loglevel = Gdx.app.getLogLevel();
            Gdx.app.setLogLevel(Application.LOG_ERROR);
            Gdx.app.log("VAITTAMAT", "lukuongelmia \n" + e);
            Gdx.app.setLogLevel(loglevel);
        }

        return lukija;
    }

    private void generoiVaittamat(Scanner lukija) {
        Gdx.app.log("VAITTAMAT", "generoidaan väittämät");
        for (int i = 7; i < 25; i++) {
            ArrayList<Vaittama> vaittamat = new ArrayList<>();
        }
    }
}
