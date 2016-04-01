package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunPiirtaja {

    ArrayList<Solmu> solmut;

    public SolmunPiirtaja(Verkko verkko) {
        solmut = verkko.getSolmut();
    }

    public void piirra(SpriteBatch batch) {
    }

}
