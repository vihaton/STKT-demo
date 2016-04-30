package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by Sasu on 11.4.2016.
 */
public class PalauteScreen implements Screen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private Solmu solmu;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Pelaaja pelaaja;
    private Label arvio;
    private Skin skin;
    private Table rootTable;
    private Stage stage;

    public PalauteScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja, Skin masterSkin) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.pelaaja = pelaaja;
        this.skin = masterSkin;
        this.stage = new Stage(viewport);
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
        Gdx.app.log("PalS", "PalauteScreenin show() -metodia kutsuttiin");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        stage.draw();

        if (Gdx.input.isTouched()) {
            sp.resetPlayScreen();
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {

    }

}