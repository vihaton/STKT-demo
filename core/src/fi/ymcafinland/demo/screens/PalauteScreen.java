package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
    private Table rootTable;

    public PalauteScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja, Skin masterSkin) {
        super(masterSkin, "PalS");
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.pelaaja = pelaaja;
        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);

        luoSisalto();
    }

    private void luoSisalto() {
        this.rootTable = new Table();
        rootTable.setFillParent(true);

        Label otsikko = new Label(pelaaja.getNimi(), skin, "otsikko");
        rootTable.add(otsikko).top().expandX().padTop(otsikko.getHeight());
        rootTable.row();

        this.arvio = new Label(pelaaja.toString(), skin, "arvio");
        arvio.setFontScale(2);
        rootTable.add(arvio).expand();

        stage.addActor(rootTable);
    }


    @Override
    public void show() {
        super.show();
        arvio.setText(pelaaja.valuesToString());
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
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