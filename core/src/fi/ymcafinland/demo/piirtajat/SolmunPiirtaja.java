package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private ArrayList<Table> solmuTaulukot;

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

        solmuTaulukot = new ArrayList<>();
        generoiSolmutaulukot(verkko);
    }

    private void generoiSolmutaulukot(Verkko verkko) {
        for (Solmu s : verkko.getSolmut()) {
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            Label otsikko = new Label(s.getOtsikko(), skin, "otsikko");
            otsikko.setFontScale(0.7f);
            Label sisalto = new Label(s.getSisalto(), skin, "sisalto");
            sisalto.setFontScale(2);

            Table tekstit = new Table();
            tekstit.setPosition(x, y);

            if (otsikko.getText().length != 0) { //jos otsikko ei ole tyhjä
                tekstit.add(otsikko);
                tekstit.row();
            }
            tekstit.add(sisalto);

            solmuTaulukot.add(tekstit);
        }
    }

    /**
     * Piirtää kaikki solmut.
     *
     * @param batch              vastaa piirtämisestä.
     * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void piirra(SpriteBatch batch, float angleToPointCamera) {
        piirraPalloJaTekstiErikseen(batch, angleToPointCamera);
    }

    private void piirraPalloJaTekstiErikseen(SpriteBatch batch, float angleToPointCamera) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        batch.begin();
        for (Table t:solmuTaulukot) {
            batch.draw(pallonKuva, t.getX() - pallonLeveys / 2, t.getY() - pallonKorkeus / 2, pallonLeveys / 2, pallonKorkeus / 2, pallonLeveys, pallonKorkeus, 1f, 1f, angleToPointCamera - 90, 0, 0, (int) pallonLeveys, (int) pallonKorkeus, false, false);
            t.setTransform(true);
            t.setRotation(angleToPointCamera - 90);
            t.draw(batch, 1f);
        }
        batch.end();
    }


}
