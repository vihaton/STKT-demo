package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayDeque;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.piirtajat.SolmunPiirtaja;
import fi.ymcafinland.demo.scenes.HUD;
import fi.ymcafinland.demo.logiikka.Solmu;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen implements Screen {

    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected Solmu solmu;
    protected CameraTransition transition;
    protected float timeSinceTransitionZoom = 0;
    protected boolean trans = false;
    boolean zoomedOut = false;
    boolean zoomed = false;

    protected Vector3 polttopiste;
    protected Vector3 keskipiste;
    float angleToPoint1;
    float angleToPoint2;
    private SelviytyjanPurjeet sp;
    private Viewport viewPort;
    private HUD hud;
    private SolmunPiirtaja solmunPiirtaja;
    long stateTime;
    long timer;
    final float moveDuration = 1.0f;
    final float zoomDuration = 0.5f;
    private final int idleTime = 10000;

    private float deltaAVG;
    private ArrayDeque<Float> viimeisetDeltat;

    public PlayScreen(SelviytyjanPurjeet sp, Solmu aloitussolmu) {
        this.sp = sp;
        solmunPiirtaja = new SolmunPiirtaja(sp.getVerkko());
        this.solmu = aloitussolmu;

        polttopiste = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
        camera = new OrthographicCamera();

//        viewPort = new FillViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        viewPort = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        batch = new SpriteBatch();

        keskipiste = new Vector3(sp.TAUSTAN_LEVEYS / 2, sp.TAUSTAN_KORKEUS / 2, 0f);
        timer = System.currentTimeMillis();
        angleToPoint1 = getAngleToPoint(polttopiste, keskipiste);
        hud = new HUD(this, batch, aloitussolmu);
        stateTime = 0;
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();

        viimeisetDeltat = new ArrayDeque<>();
        deltaAVG = 0.02f;
    }

    @Override
    public void show() {

    }

    public void alkaaTapahtua() {
        trans = true;
        stateTime = 0;
        timer = System.currentTimeMillis();
        Gdx.app.log("playscreen", "UUSI SIIRTO" + stateTime + " " + trans);
    }

    @Override
    public void render(float delta) {
        float rgbJakaja = 255f;

//        //sininen
//        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //valkoinen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
        Gdx.input.setInputProcessor(hud.stage);
        if (delta > 0.1f || delta < 0.005f) {
            delta = deltaAVG;
        }

        deltaAVG = (deltaAVG * 19 + delta) / 20;

        Gdx.app.log("playscreen", "request render " + stateTime + " " + trans + " " + delta);

        actTransition(delta);

        camera.position.set(polttopiste);

        renderZoomz(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        solmunPiirtaja.piirra(batch, angleToPoint1);

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void actTransition(float delta) {
        if (trans) {
            if (stateTime < Math.max(moveDuration * 1000, zoomDuration * 1000) + idleTime) {
                transition.act(delta);
                Gdx.graphics.requestRendering();
                stateTime += System.currentTimeMillis() - timer;
                timer = System.currentTimeMillis();

            } else {
                trans = false;
                stateTime = 0;
            }
        }
    }

    private void renderZoomz(float delta) {

        if (!zoomedOut && zoomed) {

            if (stateTime < zoomDuration * 1000) {
                if (camera.zoom >= 1) {
                    camera.zoom -= delta * 3 * (1 / zoomDuration);
                }

            }
            if (stateTime >= zoomDuration * 1000) {
                zoomed = false;
            }
        }
        if (zoomedOut && zoomed) {

            if (stateTime < zoomDuration * 1000) {
                if (camera.zoom <= 4) {
                    camera.zoom += delta * 3 * (1 / zoomDuration);
                }

            }
            if (stateTime >= zoomDuration * 1000) {
                zoomed = false;
            }
        }
        if (zoomedOut) {
            angleToPoint2 = getAngleToPoint(keskipiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f));
            camera.rotate(-angleToPoint2 + 90 - 180);
        } else {
            angleToPoint1 = getAngleToPoint(polttopiste, keskipiste);
            camera.rotate(-angleToPoint1 + 90);
        }
        if (zoomedOut && timeSinceTransitionZoom >= zoomDuration * 1000) {
            camera.position.set(keskipiste);
        }
    }


    /**
     * Hakee kulman pisteiden välillä;
     * @param start aloituspiste
     * @param target lopetuspiste
     * @return palauttaa kulman
     */
    private float getAngleToPoint(Vector3 start, Vector3 target) {
        float angleToPoint = (float) Math.toDegrees(Math.atan2(target.y - start.y, target.x - start.x));

        return angleToPoint;
    }

    public void zoom(boolean in) {
        alkaaTapahtua();
        timeSinceTransitionZoom = 0;
        zoomed = true;
        if (in) {
            transition = new CameraTransition(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f), zoomDuration);
            zoomedOut = false;
        } else {
            transition = new CameraTransition(polttopiste, keskipiste, zoomDuration);
            zoomedOut = true;
        }
        hud.update(solmu);
    }

    public void resetInputProcessor() {
        this.hud.resetInputProcessor();
    }

    public void setZoom(float ratio) {
        if (camera.zoom + ratio < 5 || camera.zoom + ratio > 0) {
            camera.zoom += ratio;
        }
    }

    public float getZoom() {
        return camera.zoom;
    }

    //Purkkaviritelmä Selviytyjän purjeiden screeninvaihtometodia varten
    public SelviytyjanPurjeet getSp() {
        return this.sp;
    }

    /**
     * HUDista tulee kutsu riippuen mitä solmua painaa. Päivittää tiedot renderille.
     * Päivittää myös HUDin seuraavalle solmulle.
     * @param solmu käsiteltävä solmu
     */
    public void setSolmu(Solmu solmu) {
        if (!this.solmu.equals(solmu)) {
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            transition = new CameraTransition(polttopiste, goal, moveDuration);
            hud.update(solmu);
        }
    }
    public void resetStateTime(){
        stateTime = 0;
    }

    public boolean getTrans() {
        return this.trans;
    }

    public void setTrans(boolean t) {
        this.trans = t;
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
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
