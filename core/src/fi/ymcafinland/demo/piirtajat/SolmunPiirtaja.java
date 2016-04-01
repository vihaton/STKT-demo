package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.*;

/**
 * Created by xvixvi on 1.4.2016.
 */
public class SolmunPiirtaja {

    private ArrayList<Solmu> solmut;
    private Texture pallonKuva;
    private final int sade;
    private SpriteBatch spriteFont;
    private Matrix4 mx4Font;
    private BitmapFont fontti;

    public SolmunPiirtaja(Verkko verkko) {
        solmut = verkko.getSolmut();
        pallonKuva = new Texture("emptynode.png");
        sade = pallonKuva.getWidth() / 2;

        spriteFont = new SpriteBatch();
        mx4Font = new Matrix4();
        fontti = new BitmapFont(Gdx.files.internal("fontti/fontti.fnt"), Gdx.files.internal("fontti/fontti.png"), true); //must be set true to be flipped
    }

    public void piirra(SpriteBatch batch, Camera camera) {
        for (int i = 0; i < solmut.size(); i++) {
            Solmu s = solmut.get(i);

            batch.begin();
            batch.draw(pallonKuva, s.getXKoordinaatti() - sade, s.getYKoordinaatti() - sade);
            batch.end();


            mx4Font.setToRotation(new Vector3(200, 200, 0), 180);
            spriteFont.setTransformMatrix(mx4Font);
            spriteFont.begin();
            fontti.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            fontti.draw(spriteFont, "kommunisti ruma huora", 100, 110);
            spriteFont.end();

        }
    }

}
