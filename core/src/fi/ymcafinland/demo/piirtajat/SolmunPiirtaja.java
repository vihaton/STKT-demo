package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunPiirtaja {

    private ArrayList<Solmu> solmut;
    private Texture pallonKuva;
    private final int sade;

    public SolmunPiirtaja(Verkko verkko) {
        solmut = verkko.getSolmut();
        pallonKuva = new Texture("emptynode.png");
        sade = pallonKuva.getWidth() / 2;
    }

    public void piirra(SpriteBatch batch) {
        for (int i = 0; i < solmut.size(); i++) {
            Solmu s = solmut.get(i);
            batch.draw(pallonKuva, s.getXKoordinaatti() - sade, s.getYKoordinaatti() - sade);
        }
    }

}
