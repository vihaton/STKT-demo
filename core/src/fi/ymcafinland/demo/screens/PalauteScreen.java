package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by Sasu on 11.4.2016.
 */
public class PalauteScreen extends PohjaScreen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private Pelaaja pelaaja;
    private Label arvio;

    public PalauteScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja, Skin masterSkin) {
        super(masterSkin, "PalS");
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.pelaaja = pelaaja;
        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        luoSisalto();
    }

    private void luoSisalto() {
        Label otsikko = new Label(pelaaja.getNimi(), skin, "otsikko");
        rootTable.add(otsikko).top().expandX().padTop(otsikko.getHeight());
        rootTable.row();

        this.arvio = new Label(pelaaja.toString(), skin, "arvio");
        arvio.setFontScale(2);
        rootTable.add(arvio).expand();
    }


    @Override
    public void show() {
        super.show();

        arvio.setText(pelaaja.valuesToString());
//        paivitaPalaute();
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    }

    private void paivitaPalaute() {
        String palaute = "";
        ArrayList<String> keinotJarjestyksessa = pelaaja.getSelviytymiskeinotJarjestyksessa();

        for (int i = 0; i < keinotJarjestyksessa.size(); i++) {
            palaute += keinotJarjestyksessa.get(i) + "\n";
        }
        arvio.setText(palaute);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();

        if (Gdx.input.isTouched()) {
            sp.setPlayScreen();
            dispose();
        }

    }
}