package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.kasittelijat.EdistymismittarinKasittelija;
import fi.ymcafinland.demo.kasittelijat.InfoButtonKasittelija;
import fi.ymcafinland.demo.kasittelijat.SolmunKasittelija;
import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.scenes.HUD;
import fi.ymcafinland.demo.transitions.CameraTransition;
import fi.ymcafinland.demo.transitions.Kamera;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen extends PohjaScreen {

    public final Kamera kamera;
    protected SpriteBatch batch;
    protected Solmu solmu;
    protected CameraTransition transition;
    public boolean zoomedOut = false;
    public Vector3 polttopiste;
    public Vector3 panpiste;
    public Vector3 keskipiste;
    protected float angleToPoint;
    protected long stateTime;
    protected long timer;
    public boolean seurataanPolttoa = true;
    private final float moveDuration = 1.0f;    //s
    private final float minFPS = 55;         //fps
    private final float renderinLoggausAlaraja = (float) Math.pow(minFPS, -1.0);  //s, eli deltan maximiarvo (jos delta on isompi kuin tämä, niin fps on liian pieni
    private final int idleTime = 3000; //ms
    private float laskuriOdotukseen = 0; //ms

    private SelviytyjanPurjeet sp;
    private Verkko verkko;
    private HUD hud;
    private SolmunKasittelija solmunKasittelija;
    private EdistymismittarinKasittelija edistymismittarinKasittelija;
    private InfoButtonKasittelija infoButtonKasittelija;
    private float deltaAVG;
    public boolean ensimmainenSiirtyma = true;
    private boolean trans;
    private boolean zoomed;

    /**
     * Playscreen luokan konstruktori
     *
     * @param sp           SelviytyjänPurjeet -instanssi
     * @param aloitussolmu ensimmäisenä ruutuun ilmestyvä solmu
     */
    public PlayScreen(SelviytyjanPurjeet sp, Solmu aloitussolmu, Pelaaja pelaaja, Skin masterSkin) {
        super(masterSkin, "PS");

        this.kamera = new Kamera(this);
        stage.setViewport(new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, kamera));

        this.sp = sp;
        this.verkko = sp.getVerkko();
        this.solmu = aloitussolmu;

        this.keskipiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.polttopiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.panpiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);


        this.solmunKasittelija = new SolmunKasittelija(stage, sp.getVerkko(), masterSkin);
        this.edistymismittarinKasittelija = new EdistymismittarinKasittelija(stage, masterSkin, pelaaja);
        this.infoButtonKasittelija = new InfoButtonKasittelija(stage, masterSkin, verkko);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();


        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, masterSkin, solmu);

        this.timer = System.currentTimeMillis();

        this.stateTime = 0;

        //Ilman näitä rivejä zoomin kutsuminen ennen liikkumista aiheuttaa NullPointerExeptionin
        this.transition = new CameraTransition(polttopiste, polttopiste, 0);
        trans = false;

        //Asetetaan jatkuva renderin pois päältä, renderöidään kerran.
//        Gdx.graphics.setContinuousRendering(false);
//        Gdx.graphics.requestRendering();

        Gdx.graphics.requestRendering();
    }

    @Override
    public void show() {
        super.show();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        edistymismittarinKasittelija.paivitaMittarinArvo(renderinLoggausAlaraja); //päivitetään edistymismittarin arvo vain kun siirrytään playscreeniin
        hud.resetInputProcessor();
    }

    public void alkaaTapahtua() {
        trans =true;
        stateTime = 0;
        timer = System.currentTimeMillis();
        hud.update(solmu, zoomedOut);
        //debug
//        Gdx.app.log("PS", "UUSI SIIRTO" + stateTime + " " + trans);
    }

    @Override
    public void render(float delta) {
        //debug
        boolean log = false;
//        if (delta > renderinLoggausAlaraja) {
//            Gdx.app.log("PS", "renderloggaus käynnistetty\n" +
//                    "minimi fps:" + minFPS + " fps, tämän ruudun fps:" + Math.pow(delta, -1) + " fps\n" +
//                    "stateTime:" + stateTime + "ms trans:" + trans + " delta:" + delta);
//            log = true;
//        }

        super.render(delta);

        if (trans) {
            actTransition(delta);
        }

        kamera.paivitaKamera(delta);
        batch.setProjectionMatrix(kamera.combined);

        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter paivitaKamera");

        angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        paivitaKasittelijat(delta);
        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter käsittelijöiden päivitys");

        stage.draw();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter stagejen piirtämiset");

//        odota(10);
//        if (log)
//            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter loppuodotus");
    }


    public void odota(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Gdx.app.log("PS" , "odota -metodi keskeytettiin, time in render:" + (System.currentTimeMillis() - timer - stateTime));
        }
    }

    public void paivitaKasittelijat(float delta) {
        solmunKasittelija.paivitaSolmut(angleToPoint);
        edistymismittarinKasittelija.pyoritaMittaria(angleToPoint);
        infoButtonKasittelija.paivitaInfoButtonit(delta, angleToPoint, zoomedOut);
    }

    private void actTransition(float delta) {
        if (stateTime < moveDuration * 1000 + idleTime) {
            transition.act(delta);
            stateTime = System.currentTimeMillis() - timer;
        } else {
            trans = false;
        }
    }

    /**
     * Hakee kulman pisteiden välillä;
     *
     * @param start  aloituspiste
     * @param target lopetuspiste
     * @return palauttaa kulman
     */
    public float getAngleToPoint(Vector3 start, Vector3 target) {
        return (float) Math.toDegrees(Math.atan2(target.y - start.y, target.x - start.x));
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
        if (!this.solmu.equals(solmu) || ensimmainenSiirtyma) {
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            seurataanPolttoa = true;
            transition = new CameraTransition(polttopiste, goal, moveDuration);
        }
    }

    public void resetStateTime() {
        stateTime = 0;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
    }

    public void siirryLahinpaanSolmuun(float x, float y) {

        Vector3 vect = new Vector3(x, y, 0);
        kamera.unproject(vect); // camera is your game camera

        float trueX = vect.x;
        float trueY = vect.y;

        Solmu tappaustaLahinSolmu = verkko.annaLahinSolmu(trueX, trueY, solmu);
        setSolmu(tappaustaLahinSolmu);
        asetaAlkuZoom();
    }

    public void asetaAlkuZoom() {
        zoomedOut = false;
        alkaaTapahtua();
        kamera.zoomaaNormaaliin();
    }

    /**
     * Hudin käyttöön  metodi
     *
     * @param in
     */
    public void nappulaZoom(boolean in) {
        zoomed = true;
        if (in) {
            seurataanPolttoa = true;
            transition = new CameraTransition(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f), moveDuration);
            kamera.zoomaaNormaaliin();
            zoomedOut = false;
        } else {
            transition = new CameraTransition(polttopiste, keskipiste.cpy().lerp(polttopiste, 0.05f), moveDuration);
            kamera.zoomaaUlos();
            zoomedOut = true;
        }
        alkaaTapahtua();
    }

    public void resetPan() {
        alkaaTapahtua();
        Vector3 kpy = polttopiste.cpy();
        polttopiste.x = panpiste.x;
        polttopiste.y = panpiste.y;
        transition = new CameraTransition(polttopiste, kpy, moveDuration);
        seurataanPolttoa = true;
    }

    public Vector3 getPolttopiste(){
        return polttopiste;
    }
    public Vector3 getPanpiste(){
        return panpiste;
    }
    public OrthographicCamera getCamera(){
        return kamera;
    }

}
