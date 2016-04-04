package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.screens.PlayScreen;


/**
 * Created by jwinter on 29.3.2016.
 *
 * QuestionScreen luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class QuestionScreen implements Screen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private BitmapFont fontti;

    public QuestionScreen(SelviytyjanPurjeet sp) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
    }

/**
 * sendData lähettää saadun datan eteenpäin. sendData konfirmoi kysymykseen laitetun tiedon, ja
 * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
 */
public void sendData() {

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

        batch.begin();
        fontti.draw(batch, "Kysymyksiä", 100, 550);
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

    /**
     * Dispose metodi sulkee kysymyksen asettamalla selviytyjän purjeisiin kysymykseksi null. Oletettavasti
     * dispose lähettää ensin keräämänsä datan eteenpäin.
     */
    @Override
    public void dispose() {

    }

}