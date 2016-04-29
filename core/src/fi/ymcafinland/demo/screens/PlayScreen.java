package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fi.ymcafinland.demo.logiikka.Pelaaja;
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
    private Viewport viewPort;
    private HUD hud;
    private SolmunPiirtaja solmunPiirtaja;
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
        this.skin = masterSkin;
        this.solmunPiirtaja = new SolmunPiirtaja(sp.getVerkko(), masterSkin);
        this.solmu = aloitussolmu;
        this.polttopiste = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
        this.camera = new OrthographicCamera();
        this.pelaaja = pelaaja;
//        viewPort = new FillViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.viewPort = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();

        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, aloitussolmu);

        this.keskipiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.timer = System.currentTimeMillis();
        this.angleToPoint1 = getAngleToPoint(polttopiste, keskipiste);
        this.stateTime = 0;

        //Ilman tätä riviä zoomin kutsuminen ennen liikkumista aiheuttaa NullPointerExeptionin
        this.transition = new CameraTransition(polttopiste, polttopiste, 0);

        //Asetetaan jatkuva renderin pois päältä, renderöidään kerran.
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();

        this.deltaAVG = 0.02f;

        createProgressBar();
    }


    private void createProgressBar() {
        progressBarStyle = new ProgressBar.ProgressBarStyle();

        progressBackground = new Texture("progressbar2/progressbackground.png");
        progressKnob = new Texture("progressbar2/progressknob.png");

        progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(progressKnob));
        progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(progressBackground));
        progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(progressKnob));

        float progBarWidth = progressBackground.getWidth() * 0.35f;
        float progBarHeight = progressKnob.getHeight();

        progressBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
        progressBar.setWidth(progBarWidth);
        progressBar.setHeight(progBarHeight);

        progressBar.setValue(0);

        Label otsikko = new Label("Edistymismittari:", skin, "otsikko");
        otsikko.setScale(0.7f);
        otsikko.setAlignment(Align.center);

        progressTable = new Table();
        progressTable.top().center().add(otsikko);
        progressTable.row();
        //ilmeisesti taulukko käsittelee ProgB. "pisteenä", jonka sijainti on PB:n vasemman alakulman sijainti, eikä esim PB:n keskikohta
        progressTable.add(progressBar);

        progressTable.setWidth(progBarWidth);
        progressTable.setHeight(progBarHeight);
        progressBar.setFillParent(true);

        //siirtää taulukon "origoa" suhteessa taulukon vasempaan alakulmaan. Esim kiertäminen tehdään suhteessa origoon.
        progressTable.setOrigin(progBarWidth, progBarHeight);
        //asettaa taulukon vasemman alakulman sijainnin
        progressTable.setPosition(keskipiste.x - progBarWidth, keskipiste.y - progBarHeight);
    }

    @Override
    public void show() {
        Gdx.app.log("PS", "Playscreenin show() -metodia kutsuttiin");
        float rgbJakaja = 255f;

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
        delta = fixDelta(delta);
        //debug
//        Gdx.app.log("PS", "request render " + stateTime + " " + trans + " " + delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);

        actTransition(delta);

        camera.position.set(polttopiste);

        renderZoomz(delta);
        rotateCamera();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        solmunPiirtaja.piirra(batch, angleToPoint1);

        actProgressBar(delta);

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

    private void actProgressBar(float delta) {
        progressBar.setValue(pelaaja.getVastausmaara());
        progressBar.act(delta);

        progressTable.setTransform(true);
        progressTable.setRotation(angleToPoint1 - 90);

        batch.begin();
        progressTable.draw(batch, 1f);
        batch.end();

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
        if (!this.solmu.equals(solmu)) {
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            transition = new CameraTransition(polttopiste, goal, moveDuration);
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
