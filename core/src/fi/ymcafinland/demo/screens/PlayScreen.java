package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.piirtajat.EdistymismittarinPiirtaja;
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
    protected fi.ymcafinland.demo.screens.Apuluokat.CameraTransition transition;
    protected float timeSinceTransitionZoom = 0;
    protected boolean trans = false;
    protected boolean zoomedOut = false;
    protected boolean zoomed = false;
    protected Vector3 polttopiste;
    protected Vector3 keskipiste;
    protected float angleToPoint1;
    protected float angleToPoint2;
    protected long stateTime;
    protected long timer;
    protected float moveDuration = 1.0f;
    protected float zoomDuration = 0.5f;

    private SelviytyjanPurjeet sp;
    private Stage stage;
    private Verkko verkko;
    private Viewport viewPort;
    private HUD hud;
    private SolmunPiirtaja solmunPiirtaja;
    private EdistymismittarinPiirtaja edistymismittarinPiirtaja;
    private float deltaAVG;
    ProgressBar progressBar;
    ProgressBar.ProgressBarStyle progressBarStyle;
    Skin skin;
    Pelaaja pelaaja;
    Texture progressBackground;
    Texture progressKnob;
    Table progressTable;

    private final int idleTime = 5000;

    /**
     * Playscreen luokan konstruktori
     *
     * @param sp           SelviytyjänPurjeet -instanssi
     * @param aloitussolmu ensimmäisenä ruutuun ilmestyvä solmu
     */
    public PlayScreen(SelviytyjanPurjeet sp, Solmu aloitussolmu, Pelaaja pelaaja, Skin masterSkin) {
        this.sp = sp;
        this.verkko = sp.getVerkko();
        this.skin = masterSkin;
        this.solmu = aloitussolmu;
        this.polttopiste = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
        this.pelaaja = pelaaja;

        this.camera = new OrthographicCamera();
        this.viewPort = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
//        viewPort = new FillViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);

        this.stage = new Stage(viewPort);
        this.solmunPiirtaja = new SolmunPiirtaja(stage, sp.getVerkko(), masterSkin);
        this.edistymismittarinPiirtaja = new EdistymismittarinPiirtaja(stage, masterSkin, pelaaja);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();

        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, aloitussolmu);

        this.keskipiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.timer = System.currentTimeMillis();
        this.angleToPoint1 = getAngleToPoint(polttopiste, keskipiste);
        this.stateTime = 0;

        //Ilman tätä riviä zoomin kutsuminen ennen liikkumista aiheuttaa NullPointerExeptionin
        this.transition = new fi.ymcafinland.demo.screens.Apuluokat.CameraTransition(polttopiste, polttopiste, 0);

        //Asetetaan jatkuva renderin pois päältä, renderöidään kerran.
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();

        this.deltaAVG = 0.02f;
    }

    @Override
    public void show() {
        Gdx.app.log("PS", "Playscreenin show() -metodia kutsuttiin");

//        float rgbJakaja = 255f;
//        //sininen
//        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //valkoinen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
    }

    public void alkaaTapahtua() {
        trans = true;
        stateTime = 0;
        timer = System.currentTimeMillis();

        //debug
//        Gdx.app.log("PS", "UUSI SIIRTO" + stateTime + " " + trans);
    }

    @Override
    public void render(float delta) {
        //debug
//        Gdx.app.log("PS", "request render " + stateTime + " " + trans + " " + delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.setDebugAll(true);

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        delta = fixDelta(delta);

        actTransition(delta);

        camera.position.set(polttopiste);

        renderZoomz(delta);
        rotateCamera();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        solmunPiirtaja.paivitaSolmut(batch, angleToPoint1);
        edistymismittarinPiirtaja.paivitaMittari(delta, angleToPoint1);

        stage.draw();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private float fixDelta(float delta) {
        if (delta > 0.1f || delta < 0.005f) {
            delta = deltaAVG;
        }

        deltaAVG = (deltaAVG * 19 + delta) / 20;

        return delta;
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

    }

    private void rotateCamera() {
        if (zoomedOut) {
            angleToPoint2 = getAngleToPoint(keskipiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f));
            camera.rotate(-angleToPoint2 - 90);
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
     *
     * @param start  aloituspiste
     * @param target lopetuspiste
     * @return palauttaa kulman
     */
    private float getAngleToPoint(Vector3 start, Vector3 target) {
        return (float) Math.toDegrees(Math.atan2(target.y - start.y, target.x - start.x));
    }

    public void zoom(boolean in) {
        alkaaTapahtua();
        timeSinceTransitionZoom = 0;
        zoomed = true;
        if (in) {
            transition = new fi.ymcafinland.demo.screens.Apuluokat.CameraTransition(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f), zoomDuration);
            zoomedOut = false;
        } else {
            transition = new fi.ymcafinland.demo.screens.Apuluokat.CameraTransition(polttopiste, keskipiste, zoomDuration);
            zoomedOut = true;
        }
        hud.update(solmu);
    }

    public void resetInputProcessor() {
        hud.resetInputProcessor();
    }

    public void setZoom(float ratio) {
        float z = getZoom();
        if (z + ratio < 3 && z + ratio > 0.75) {
            //debug
            //Gdx.app.log("PS", "vanha zoom " + getZoom() + ", uusi " + (z + ratio));
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
     *
     * @param solmu käsiteltävä solmu
     */
    public void setSolmu(Solmu solmu) {
        //todo aina kun kutsutaan setSolmua, zoomataan siihen solmuun ja käännetään kamera ja solmu asiaankuuluvasti.
        if (!this.solmu.equals(solmu)) {
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            transition = new fi.ymcafinland.demo.screens.Apuluokat.CameraTransition(polttopiste, goal, moveDuration);
            hud.update(solmu);
        }
    }

    public void resetStateTime() {
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
        Gdx.app.log("PS", "resize: width " + width + " height " + height);
        viewPort.update(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
        Gdx.app.log("PS", "Playscreenin pause() -metodia kutsuttiin");
    }

    @Override
    public void resume() {
        Gdx.app.log("PS", "Playscreenin resume() -metodia kutsuttiin");
    }

    @Override
    public void hide() {
        Gdx.app.log("PS", "Playscreenin hide() -metodia kutsuttiin");
    }

    @Override
    public void dispose() {
        Gdx.app.log("PS", "Playscreenin dispose() -metodia kutsuttiin");
    }

    public void siirryLahinpaanSolmuun(float x, float y) {

        Vector3 vect = new Vector3(x, y, 0);
        camera.unproject(vect); // camera is your game camera

        float trueX = vect.x;
        float trueY = vect.y;

        Solmu tappaustaLahinSolmu = verkko.annaLahinSolmu(trueX, trueY, solmu);
        setSolmu(tappaustaLahinSolmu);
    }
}
