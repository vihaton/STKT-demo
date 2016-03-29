package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.screens.PlayScreen;


/**
 * Created by jwinter on 29.3.2016.
 *
 * Kysymys luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class Kysymys implements Screen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private FitViewport viewport;
    private OrthographicCamera camera;

    public Kysymys(final SelviytyjanPurjeet sp) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(400, 600, camera);
        camera.setToOrtho(false, 400, 600);
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
        sp.font.draw(batch, "Yer a wizard, harry", 10, 15);
        batch.end();

        if (Gdx.input.isTouched()) {
            sp.setScreen(new PlayScreen(this.sp, sp.verkko.getSolmut().get(0)));
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
        this.sp.setKysymys(null);
    }

}