package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.kasittelijat.EdistymismittarinKasittelija;
import fi.ymcafinland.demo.kasittelijat.SolmunKasittelija;
import fi.ymcafinland.demo.scenes.HUD;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.screens.Apuluokat.CameraTransition;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen extends PohjaScreen {

    protected SpriteBatch batch;
    protected Solmu solmu;
    protected fi.ymcafinland.demo.screens.Apuluokat.CameraTransition transition;
    protected float timeSinceTransitionZoom = 0;
    protected boolean trans = false;
    public boolean zoomedOut = false;
    protected boolean zoomed = false;
    protected Vector3 polttopiste;
    protected Vector3 keskipiste;
    protected float angleToPoint;
    protected long stateTime;
    protected long timer;
    private final float moveDuration = 1.0f;    //s
    private final float zoomDuration = 0.5f;    //s
    private final float renderinLoggausAlaraja = 0.5f;  //s
    private final int idleTime = 3000; //ms

    private SelviytyjanPurjeet sp;
    private Verkko verkko;
    private HUD hud;
    private SolmunKasittelija solmunKasittelija;
    private EdistymismittarinKasittelija edistymismittarinKasittelija;
    private float deltaAVG;
    public boolean ensimmainenSiirtyma = true;

    /**
     * Playscreen luokan konstruktori
     *
     * @param sp           SelviytyjänPurjeet -instanssi
     * @param aloitussolmu ensimmäisenä ruutuun ilmestyvä solmu
     */
    public PlayScreen(SelviytyjanPurjeet sp, Solmu aloitussolmu, Pelaaja pelaaja, Skin masterSkin) {
        super(masterSkin, "PS");
        this.sp = sp;
        this.verkko = sp.getVerkko();
        this.solmu = aloitussolmu;

        this.solmunKasittelija = new SolmunKasittelija(stage, sp.getVerkko(), masterSkin);
        this.edistymismittarinKasittelija = new EdistymismittarinKasittelija(stage, masterSkin, pelaaja);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();

        //näkymä on aluksi kaukana
        zoomedOut = true;
        camera.zoom = 4f;

        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, masterSkin, solmu);

        this.timer = System.currentTimeMillis();
        this.keskipiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.polttopiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.angleToPoint = getAngleToPoint(polttopiste, keskipiste);
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
        super.show();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        hud.resetInputProcessor();
    }

    public void alkaaTapahtua() {
        trans = true;
        stateTime = 0;
        timer = System.currentTimeMillis();

        //debug
//        Gdx.app.log("PS", "UUSI SIIRTO" + stateTime + " " + trans);
    }

//todo välillä tulee jäätävä lagaus, joka johtuu render metodin ulkopuolisista asioista t.logit. Mistä vitusta se johtuu?
    @Override
    public void render(float delta) {
        //debug
        boolean log = false;
        if (delta > renderinLoggausAlaraja) {
            Gdx.app.log("PS", "request render alarajalla:" + renderinLoggausAlaraja + "s stateTime:" + stateTime + "ms trans:" + trans + " delta:" + delta);
            log = true;
        }

        super.render(delta);
        if (log) Gdx.app.log("PS", "render stateTime:" + (System.currentTimeMillis()-timer) + "ms @fter super.render");

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        delta = deltaManipulation(delta);

        if (trans) {
            actTransition(delta);
        }
        if (log) Gdx.app.log("PS", "render stateTime:" + (System.currentTimeMillis()-timer) + "ms @fter actTransition");

        camera.position.set(polttopiste);

        renderZoomz(delta);
        rotateCamera();
        if (log) Gdx.app.log("PS", "render stateTime:" + (System.currentTimeMillis()-timer) + "ms @fter rotateCamera");

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        solmunKasittelija.paivitaSolmut(angleToPoint);
        edistymismittarinKasittelija.paivitaMittari(delta, angleToPoint);
        if (log) Gdx.app.log("PS", "render stateTime:" + (System.currentTimeMillis()-timer) + "ms @fter edistysmittarinKasittelija");

        stage.draw();

        batch.setProjectionMatrix(hud.stage.getCamera().combined);
//        hud.stage.setDebugAll(true);
        hud.stage.draw();
        if (log) Gdx.app.log("PS", "render stateTime:" + (System.currentTimeMillis()-timer) + "ms @fter hud.stage.draw");
    }

    private float deltaManipulation(float delta) {
        if (delta > 0.1f || delta < 0.005f) {
            delta = deltaAVG;
        }

        deltaAVG = (deltaAVG * 19 + delta) / 20;

        return delta;
    }

    public void actTransition(float delta) {
        if (stateTime < Math.max(moveDuration * 1000, zoomDuration * 1000) + idleTime) {
            polttopiste = transition.act(delta);
            Gdx.graphics.requestRendering();
            stateTime = System.currentTimeMillis() - timer;
        } else {
            trans = false;
            stateTime = 0;
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
        //todo kun polttopiste == keskipiste, niin ruutu pyörähtää vammaisesti ympäri, asetettava siten ettei polttopiste pääse keskipisteeseen asti kun sitä siirretään sitä kohti
        angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        camera.rotate(-angleToPoint + 90);

        if (zoomedOut && timeSinceTransitionZoom >= zoomDuration * 1000) {
            Vector3 solmunSijainti = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0);
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
            transition = new CameraTransition(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f), zoomDuration);
            zoomedOut = false;
        } else {
            transition = new CameraTransition(polttopiste, keskipiste, zoomDuration);
            zoomedOut = true;
        }
        hud.update(solmu);
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
        if (!this.solmu.equals(solmu) || ensimmainenSiirtyma) {
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            transition = new fi.ymcafinland.demo.screens.Apuluokat.CameraTransition(polttopiste, goal, moveDuration);
            hud.update(solmu);
            Gdx.graphics.requestRendering();
        }
    }

    public void odota(float kuinkaKauanMillisekunneissa) {
        stateTime = 0;
        timer = System.currentTimeMillis();
        while (stateTime < kuinkaKauanMillisekunneissa) {
            stateTime = System.currentTimeMillis() - timer;
        }
        Gdx.app.log("PS", "odotettiin " + stateTime + " millisekunttia");
        ensimmainenSiirtyma = false;
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
        super.resize(width, height);
        hud.resize(width, height);
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
