package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;
import fi.ymcafinland.demo.screens.PlayScreen;

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
    public Stage pallostage;
    private Table palloTable;

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
        pallostage = new Stage();
        palloTable = new Table();
    }

    /**
     * Piirtää kaikki solmut.
     *
     * @param batch              vastaa piirtämisestä.
     * @param angleToPointCamera kulma, jolla kamera on suunnattu keskipisteeseen
     */
    public void piirra(SpriteBatch batch, float angleToPointCamera, PlayScreen ps) {
        piirraPalloJaTekstiErikseen(batch, angleToPointCamera, ps);
    }

    private void piirraPalloJaTekstiErikseen(SpriteBatch batch, float angleToPointCamera,final PlayScreen ps) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        batch.begin();
        for (int i = 0; i < solmut.size(); i++) {
            final Solmu s = solmut.get(i);
            float x = s.getXKoordinaatti();
            float y = s.getYKoordinaatti();

            batch.draw(pallonKuva, x - pallonLeveys / 2, y - pallonKorkeus / 2, pallonLeveys / 2, pallonKorkeus / 2, pallonLeveys, pallonKorkeus, 1f, 1f, angleToPointCamera - 90, 0, 0, (int) pallonLeveys, (int) pallonKorkeus, false, false);

            otsikko.setText(s.getOtsikko());
            otsikko.setFontScale(0.7f);
            sisalto.setText(s.getSisalto());
            sisalto.setFontScale(2);

            tekstitaulukko.reset();
            palloTable.reset();

            Button button = new Button();
            button.setPosition(x, y);
            button.setBounds(x, y, pallonLeveys, pallonKorkeus);
            button.setFillParent(true);



            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("SolmunPiirtäjä", "Solmua " + s.getID() + " klikattu");
                    ps.setSolmu(s);

                }
            });

            palloTable.add(button);
            palloTable.setPosition(x, y);
            palloTable.setFillParent(true);
            palloTable.setBounds(x, y, pallonLeveys, pallonKorkeus);

            pallostage.addActor(palloTable);
            if (otsikko.getText().length != 0) { //jos otsikko ei ole tyhjä
                tekstitaulukko.add(otsikko);
                tekstitaulukko.row();
            }
            tekstitaulukko.add(sisalto);
            tekstitaulukko.setTransform(true);
            tekstitaulukko.setRotation(angleToPointCamera - 90);

            tekstitaulukko.draw(batch, 1f);
        }
        batch.end();
    }


}
