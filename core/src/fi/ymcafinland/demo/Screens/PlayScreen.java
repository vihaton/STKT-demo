package fi.ymcafinland.demo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.SelviytyjanPurjeet;
import fi.ymcafinland.demo.scenes.HUD;
import logiikka.Solmu;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen implements Screen {

    public final static int V_WIDTH = 180;
    public final static int V_HEIGHT = 300;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Solmu solmu;
    protected CameraTransition transition;
    protected float timeSinceTransition = 0;
    protected boolean trans = false;

    private SelviytyjanPurjeet sp;
    private Sprite map;
    private Viewport viewPort;
    private HUD hud;

    public PlayScreen(SelviytyjanPurjeet sp) {
        this.sp = sp;

        //TURHAA SHITTIII TESTAUSTA VARTEN
        Solmu s1 = new Solmu("1",null);
        Solmu s2 = new Solmu("2",s1);
        Solmu s3 = new Solmu("3",s1);
        Solmu s4 = new Solmu("4",s1);
        Solmu s5 = new Solmu("5",s2);
        Solmu s6 = new Solmu("6",s2);
        Solmu s7 = new Solmu("7",s2);


        s1.setOtsikko("Kappasolmu");
        s2.setSijainti(100, 100);
        s2.setOtsikko("Disaster");
        s2.setVasenSisarus(s3);
        s2.setOikeaSisarus(s4);
        ArrayList<Solmu> testiS = new ArrayList<Solmu>();
        testiS.add(s5);
        testiS.add(s6);
        testiS.add(s7);
        s2.setLapset(testiS);
        //TÄHÄN ASTI

        camera = new OrthographicCamera();
        viewPort = new FitViewport(V_WIDTH,V_HEIGHT,camera);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        batch = new SpriteBatch();

        //Tästä poistettu muuttuja 'img' koska sitä käytettiin vaan yhessä rivissä, pistetään takas jos on tarvis
        map = new Sprite(new Texture("pallokuva.png"));
        map.setOrigin(0, 0);
        map.setPosition((-map.getWidth() / 2 + 150), -map.getHeight() / 2 + 100);


        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);
        hud = new HUD(this, batch, s2);
        this.solmu = s1;
        setSolmu(s2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        float rgbJakaja = 255f;
        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

        //ToDo Sulava siirtyminen.

        if (trans) {
            transition.act(delta);
            timeSinceTransition+=delta;
        }

        if (timeSinceTransition > 1.0f){
            //WHATS WRONG WITH YOU
            camera.position.set(solmu.getXKoordinaatti(),solmu.getYKoordinaatti(), 0f);
            //WHATS WRONG WITH YOU ^
            trans = false;
        }

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        map.draw(batch);
        batch.end();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }
    public void zoom(boolean in) {
        //ToDo Koko kuvan kokoinen zoom-out. (camera.zoom -= 100000000000000000) tai kunnes kuvan rajat tulee vastaan? Miten tehdä sulava?
        if (in) {
            camera.zoom -= 1;
        } else {
            camera.zoom += 1;
        }
    }

    public void setSolmu(Solmu solmu){
        if(!this.solmu.equals(solmu)) {

            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);

            this.solmu = solmu;
            trans = true;
            transition = new CameraTransition(camera.position, goal, 1f);
            timeSinceTransition = 0;


        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width,height);
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
