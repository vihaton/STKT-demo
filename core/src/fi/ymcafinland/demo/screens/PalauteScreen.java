package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private final BitmapFont fontti;
    private Pelaaja pelaaja;
    private Label arvio;
    private Skin skin;
    private Table table;
    private Stage stage;

    public PalauteScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.pelaaja = pelaaja;
        table = new Table();
        stage = new Stage(viewport);
        table.center();
        table.setFillParent(true);
        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);

        Label.LabelStyle arvioStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        skin = new Skin();
        skin.add("arvio", arvioStyle);
        this.arvio = new Label(pelaaja.toString(), skin, "arvio");

        arvio.setFontScale(2);
        stage.addActor(table);
        table.add(arvio);
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

        glyphLayout.setText(fontti, pelaaja.getNimi());
        float x = (sp.V_WIDTH - glyphLayout.width) / 3f;
        float y = (sp.V_HEIGHT / 1.2f + glyphLayout.height);

        batch.begin();
        fontti.draw(batch, glyphLayout, x, y);
        batch.end();

        arvio.setText(pelaaja.toString());

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