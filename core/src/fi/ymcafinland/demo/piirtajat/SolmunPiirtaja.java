package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunPiirtaja {

    private ArrayList<Solmu> solmut;
    private Texture pallonKuva;
    private BitmapFont fontti;
    private Table tekstitaulukko;
    private Skin skin;
    private Label.LabelStyle labelStyle;
    private Label otsikko;
    private Label sisalto;
    private float pallonLeveys;
    private float pallonKorkeus;

    public SolmunPiirtaja(Verkko verkko) {
        solmut = verkko.getSolmut();
        pallonKuva = new Texture("emptynode.png");
        pallonLeveys = pallonKuva.getWidth();
        pallonKorkeus = pallonKuva.getHeight();

        fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false); //must be set true to be flipped
        tekstitaulukko = new Table();
        skin = new Skin();
        labelStyle = new Label.LabelStyle(fontti, fontti.getColor());
        Label.LabelStyle sisaltotyyli = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        skin.add("otsikko", labelStyle);
        skin.add("sisalto", sisaltotyyli);
        otsikko = new Label("ymca", skin, "otsikko");
        sisalto = new Label("ymca", skin, "sisalto");
    }

    /**
     * Piirtää kaikki solmut.
     *
     * @param batch vastaa piirtämisestä.
     * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void piirra(SpriteBatch batch, float angleToPointCamera) {
        piirraPalloJaTekstiErikseen(batch, angleToPointCamera);
    }

    private void piirraPalloJaTekstiErikseen(SpriteBatch batch, float angleToPointCamera) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        batch.begin();
        for (int i = 0; i < solmut.size(); i++) {
            Solmu s = solmut.get(i);
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            batch.draw(pallonKuva, x - pallonLeveys / 2, y - pallonKorkeus / 2, pallonLeveys / 2, pallonKorkeus / 2, pallonLeveys, pallonKorkeus, 1f, 1f, angleToPointCamera - 90, 0, 0, (int) pallonLeveys, (int) pallonKorkeus, false, false);

            //todo tekstit pallojen sisälle (mahdollisimman isolla)
            otsikko.setText(s.getOtsikko());
            sisalto.setText(s.getSisalto());

            tekstitaulukko.reset();

            tekstitaulukko.setPosition(x, y);
            tekstitaulukko.add(otsikko);
            tekstitaulukko.row();
            tekstitaulukko.add(sisalto);
            tekstitaulukko.setTransform(true);
            tekstitaulukko.setRotation(angleToPointCamera - 90);
            tekstitaulukko.draw(batch, 1f);
        }
        batch.end();
    }


}
