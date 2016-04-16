package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.logiikka.Vaittamat;
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
    private Solmu solmu;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private BitmapFont fontti;
    private BitmapFont toinenFontti;
    private final Pelaaja pelaaja;
    private final Vaittamat vaittamat;
    private ArrayList<Vaittama> solmunVaittamat;
    private Random rnd;

    public QuestionScreen(SelviytyjanPurjeet sp, Pelaaja pelaaja, Vaittamat vaittamat) {
        Gdx.app.log("QS", "QS konstruktoria kutsuttiin");
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.toinenFontti = new BitmapFont();
        solmu = null;
        this.pelaaja = pelaaja;
        this.vaittamat = vaittamat;
        solmunVaittamat = vaittamat.getKarttaSolmujenVaittamista().get("7");
        rnd = new Random();

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
        Gdx.app.log("QS", "QS konstruktori on valmis");
    }

    /**
     * sendData lähettää saadun datan eteenpäin. sendData konfirmoi kysymykseen laitetun tiedon, ja
     * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
     */
    public void sendData() {

    }

    @Override
    public void show() {
        Gdx.app.log("QS", "QuestionScreenin show() -metodia kutsuttiin");
        lisaaSatunnaistaSelviytymista();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        glyphLayout.setText(fontti, "Kolmannen tason");
        float x = (sp.V_WIDTH - glyphLayout.width) / 2;
        float y = (sp.V_HEIGHT - 2 * glyphLayout.height);

        batch.begin();
        fontti.draw(batch, glyphLayout, x, y);
        y -= glyphLayout.height;
        glyphLayout.setText(fontti, "väittämät");
        fontti.draw(batch, glyphLayout, (sp.V_WIDTH - glyphLayout.width) / 2, y);
        y -= 2 * glyphLayout.height;
        x = sp.V_WIDTH / 10;

        for (int i = 0; i < solmunVaittamat.size(); i++) {
            glyphLayout.setText(toinenFontti, solmunVaittamat.get(i).getTeksti());
            toinenFontti.draw(batch, glyphLayout, x, y);
            y -= 1.5 * glyphLayout.height;
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            sp.resetPlayScreen();
            dispose();
        }

    }

    private void lisaaSatunnaistaSelviytymista() {
        Gdx.app.log("QS", "lisätään satunnaista selviytymistä");
        int kasvatettava = rnd.nextInt(6);
        pelaaja.lisaaSelviytymisarvoIndeksissa(kasvatettava, 1f);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    public void setSolmu(Solmu solmu) {
        this.solmu = solmu;
        solmunVaittamat = vaittamat.getYhdenSolmunVaittamat(solmu.getID());
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