package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunPiirtaja {

    private ArrayList<Solmu> solmut;
    private Texture pallonKuva;
    private TextureRegion palloregion;
    private TextureAtlas atlas;
    private BitmapFont fontti;
    private BitmapFont toinenFontti;
    private static GlyphLayout glyphLayout = new GlyphLayout();



    public SolmunPiirtaja(Verkko verkko) {
        solmut = verkko.getSolmut();
        pallonKuva = new Texture("emptynode.png");

        atlas = new TextureAtlas(Gdx.files.internal("taustat/taustat.pack"));

        fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false); //must be set true to be flipped
        toinenFontti = new BitmapFont();
    }

    /**
     * Piirtää kaikki solmut.
     *
     * @param batch vastaa piirtämisestä.
     * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void piirra(SpriteBatch batch, float angleToPointCamera) {
//        piirraPallonKuva(batch, angleToPointCamera);
        piirraPalloJaTekstiErikseen(batch, angleToPointCamera);
    }

    private void piirraPallonKuva(SpriteBatch batch, float angleToPointCamera) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        batch.begin();
        for (int i = 0; i < solmut.size(); i++) {
            Solmu s = solmut.get(i);
            palloregion = atlas.findRegion(s.getTaustakuvanNimi());
            float leveys = palloregion.getRegionWidth();
            float korkeus = palloregion.getRegionHeight();

//            //Vaihtoehto1, pallot pysyvät samassa asennossa. Kulma = s.getKulma()
//            batch.draw(palloregion, s.getXKoordinaatti() - leveys / 2, s.getYKoordinaatti() - korkeus / 2, leveys / 2, korkeus / 2, leveys, korkeus, 1f, 1f, s.getKulma());

            //Vaihtoehto2, pallojen tekstit aina alaspäin. Kulma = angleToPointCamera
            batch.draw(palloregion, s.getXKoordinaatti() - leveys / 2, s.getYKoordinaatti() - korkeus / 2, leveys / 2, korkeus / 2, leveys, korkeus, 1f, 1f, angleToPointCamera - 90);
        }
        batch.end();
    }

    //Todo tekstit irti balloista
    private void piirraPalloJaTekstiErikseen(SpriteBatch batch, float angleToPointCamera) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        float leveys = pallonKuva.getWidth();
        float korkeus = pallonKuva.getHeight();

        batch.begin();
        for (int i = 0; i < solmut.size(); i++) {
            Solmu s = solmut.get(i);
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            batch.draw(pallonKuva, x - leveys / 2, y - korkeus / 2, leveys / 2, korkeus / 2, leveys, korkeus, 1f, 1f, angleToPointCamera - 90, 0, 0, (int)leveys, (int)korkeus, false, false);

//            fontti.draw(batch, s.getOtsikko(), x, y);
            Label myLabel = new Label("My Text", new Skin());
            myLabel.setPosition(x, y);
            myLabel.setRotation(-angleToPointCamera - 90);
            myLabel.draw(batch, 1);
        }
        batch.end();
    }


}
