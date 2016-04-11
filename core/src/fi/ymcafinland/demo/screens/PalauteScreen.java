package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
    private BitmapFont fontti;

    public PalauteScreen(SelviytyjanPurjeet sp) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        glyphLayout.setText(fontti, "PalautettaJÄBILLE: ");
        float x = (sp.V_WIDTH - glyphLayout.width) / 2;
        float y = (sp.V_HEIGHT / 2 + glyphLayout.height);

        batch.begin();
        fontti.draw(batch, glyphLayout, x, y);
        glyphLayout.setText(fontti, "OLET KULTAA");
        y -= glyphLayout.height * 1.3f;
        fontti.draw(batch, glyphLayout, (sp.V_WIDTH - glyphLayout.width) / 2, y);
        batch.end();

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