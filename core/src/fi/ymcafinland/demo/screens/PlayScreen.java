package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.ymcafinland.demo.kasittelijat.EdistymismittarinKasittelija;
import fi.ymcafinland.demo.kasittelijat.InfoButtonKasittelija;
import fi.ymcafinland.demo.kasittelijat.KameranKasittelija;
import fi.ymcafinland.demo.kasittelijat.SolmunKasittelija;
import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.scenes.HUD;
import fi.ymcafinland.demo.transitions.CameraTransition;
import fi.ymcafinland.demo.transitions.ZoomTransition;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen extends PohjaScreen {

    private KameranKasittelija kameranKasittelija;

    protected SpriteBatch batch;
    protected Solmu solmu;
    protected float timeSinceTransitionZoom = 0;
    protected boolean trans = false;
    public boolean zoomedOut = false;
    protected boolean zoomed = false;
    public Vector3 polttopiste;
    public Vector3 panpiste;
    protected Vector3 keskipiste;
    protected float angleToPoint;
    protected long stateTime;
    protected long timer;
    //public boolean seurataanPolttoa = true;
    private final float moveDuration = 1.0f;    //s
    private final float zoomDuration = 1.0f;    //s
    private float currentZoomDuration = 3.0f;      // viimeisimpänä suoritetun zoomin kesto
    private float timeSinceLastZoomEvent = 0;   // kumulatiivinen deltalukema, nollataan zoomatessa
    private final float minFPS = 45;         //fps
    private final float renderinLoggausAlaraja = (float) Math.pow(minFPS, -1.0);  //s, eli deltan maximiarvo (jos delta on isompi kuin tämä, niin fps on liian pieni
    private final int idleTime = 3000; //ms
    private float laskuriOdotukseen = 0; //ms

    private SelviytyjanPurjeet sp;
    private Verkko verkko;
    private HUD hud;
    private SolmunKasittelija solmunKasittelija;
    private EdistymismittarinKasittelija edistymismittarinKasittelija;
    private InfoButtonKasittelija infoButtonKasittelija;
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

        this.keskipiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.polttopiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        this.panpiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);

        this.kameranKasittelija = new KameranKasittelija(camera, keskipiste, polttopiste, panpiste);
        this.solmunKasittelija = new SolmunKasittelija(stage, sp.getVerkko(), masterSkin, pelaaja);
        this.edistymismittarinKasittelija = new EdistymismittarinKasittelija(stage, masterSkin, pelaaja);
        this.infoButtonKasittelija = new InfoButtonKasittelija(stage, masterSkin, verkko);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();

        //näkymä on aluksi kaukana
        zoomedOut = false;
        kameranKasittelija.setZoom(4f);

        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, masterSkin, solmu);

        this.timer = System.currentTimeMillis();
        this.angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        this.stateTime = 0;

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
        trans = true;
        stateTime = 0;
        timer = System.currentTimeMillis();
        hud.update(solmu, zoomedOut);
        //debug
//        Gdx.app.LOG("PS", "UUSI SIIRTO" + stateTime + " " + trans);
    }

    @Override
    public void render(float delta) {
        //debug
        boolean log = false;
        if (delta > renderinLoggausAlaraja && SelviytyjanPurjeet.LOG) {
            Gdx.app.log("PS", "renderloggaus käynnistetty\n" +
                    "minimi fps:" + minFPS + " fps, tämän ruudun fps:" + Math.pow(delta, -1) + " fps\n" +
                    "stateTime:" + stateTime + "ms trans:" + trans + " delta:" + delta);
            log = true;
        }

        super.render(delta);

        // Lasketaan kumulatiivinen delta siten ettei ole mahdollisuutta ylivuotoon
        if (timeSinceLastZoomEvent < Float.MAX_VALUE) {
            this.timeSinceLastZoomEvent += delta;
        }

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        if (trans) {
            actTransition(delta);
        }
        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter actTransition");

        kameranKasittelija.siirrySeurattavaanPisteeseen();
        kameranKasittelija.rotateCamera(getAngleToPoint(polttopiste, keskipiste));

        //actZoomia kutsutaan vain jos zoomTransition on käynnissä
        if (timeSinceLastZoomEvent < currentZoomDuration) {
            kameranKasittelija.actZoom(delta);
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        paivitaKasittelijat(delta);
        if (log) {
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter käsittelijöiden päivitys");
        }

        stage.draw();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (log) {
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter stagejen piirtämiset");
        }

//        odota(10);
//        if (LOG)
//            Gdx.app.LOG("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter loppuodotus");
        hud.paivitaDelta(delta);
    }

    public void odota(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            if (SelviytyjanPurjeet.LOG)
                Gdx.app.log("PS", "odota -metodi keskeytettiin, time in render:" + (System.currentTimeMillis() - timer - stateTime));
        }
    }

    public void paivitaKasittelijat(float delta) {
        angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        solmunKasittelija.paivitaSolmut(angleToPoint);
        edistymismittarinKasittelija.pyoritaMittaria(angleToPoint);
        infoButtonKasittelija.paivitaInfoButtonit(delta, angleToPoint, zoomedOut);
    }

    public void actTransition(float delta) {
        if (stateTime < Math.max(moveDuration * 1000, zoomDuration * 1000) + idleTime) {
            kameranKasittelija.actTransition(delta);
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
    private float getAngleToPoint(Vector3 start, Vector3 target) {
        return (float) Math.toDegrees(Math.atan2(target.y - start.y, target.x - start.x));
    }

    /**
     * Hudin käyttöön  metodi
     *
     * @param in
     */
    //ToDo Zoom riippumaan pallon koosta?
    public void nappulaZoom(boolean in) {
        timeSinceTransitionZoom = 0;
        timeSinceLastZoomEvent = 0;
        zoomed = true;
        if (in) {
            kameranKasittelija.setSeurataanPolttoa(true);
            kameranKasittelija.setTransition(new CameraTransition(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f), moveDuration));
            kameranKasittelija.setZoomTransition(new ZoomTransition(camera.zoom, 1f, zoomDuration, true));
            zoomedOut = false;
        } else {
            kameranKasittelija.setTransition(new CameraTransition(polttopiste, keskipiste.cpy().lerp(polttopiste, 0.05f), moveDuration));
            kameranKasittelija.setZoomTransition(new ZoomTransition(camera.zoom, 6f, zoomDuration, false));
            zoomedOut = true;
        }
        currentZoomDuration = zoomDuration * 3;
        alkaaTapahtua();
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
            kameranKasittelija.setSeurataanPolttoa(true);
            kameranKasittelija.setTransition(new CameraTransition(polttopiste, goal, moveDuration));
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
        //todo unproject ei toimi täysin oikein kun ollaan zoomattu ulos
        camera.unproject(vect); // camera is your game camera

        float trueX = vect.x;
        float trueY = vect.y;

        if (SelviytyjanPurjeet.LOG) {
            Gdx.app.log("PS", "täppäyksen koordinaatit x: " + trueX + " y: " + trueY);
        }

        if (verkko.kosketusTarpeeksiLahelleJotainSolmua(trueX, trueY)) {
            Solmu tappaustaLahinSolmu = verkko.annaEdellistaKosketustaLahinSolmu();

            if (SelviytyjanPurjeet.LOG)
                Gdx.app.log("PS", "kosketus osui tarpeeksi lähelle solmua " + tappaustaLahinSolmu.getID() + "\n" +
                        "täppäyksen etäisyys solmuun " + Math.hypot(tappaustaLahinSolmu.getXKoordinaatti() - trueX, tappaustaLahinSolmu.getYKoordinaatti() - trueY));

            setSolmu(tappaustaLahinSolmu);
            asetaAlkuZoom();
        }
    }

    public void asetaAlkuZoom() {
        zoomedOut = false;
        alkaaTapahtua();
        kameranKasittelija.setZoomTransition(new ZoomTransition(camera.zoom, 1f, zoomDuration * 2, true));
    }

    public void paivitaPiste(Vector3 paivitettava, Vector3 kopioitava) {
        paivitettava.x = kopioitava.x;
        paivitettava.y = kopioitava.y;
    }

    public void resetPan() {
        alkaaTapahtua();
        Vector3 kpy = polttopiste.cpy();
        paivitaPiste(polttopiste, panpiste);
        kameranKasittelija.setTransition(new CameraTransition(polttopiste, kpy, moveDuration));
        kameranKasittelija.setSeurataanPolttoa(true);
    }

    public Vector3 getPolttopiste() {
        return polttopiste;
    }

    public Vector3 getPanpiste() {
        return panpiste;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Muuttaa panorointiliikkeen puhelimen näytöltä pelin taustan avaruuteen.
     * <p/>
     * Ottaa huomioon kulman solmusta keskipisteeseen (PPtoKP) ja panoroinnin suunnan näytöllä (atanRadians).
     *
     * @param deltaX äxän muutos puhelimen näytöllä
     * @param deltaY yyn muutos puhelimen näytöllä
     */
    public void panoroi(float deltaX, float deltaY) {
        kameranKasittelija.setSeurataanPolttoa(false);
        float PPtoKP = getAngleToPoint(polttopiste, keskipiste);
        float muutos = (float) Math.hypot(deltaX, deltaY);

        float atanRadians = (float) Math.atan2(deltaY, deltaX);
        float cos = (float) Math.cos(Math.toRadians(PPtoKP + 90) - atanRadians);
        float sin = (float) Math.sin(Math.toRadians(PPtoKP + 90) - atanRadians);

        deltaX = muutos * cos * camera.zoom;
        deltaY = muutos * sin * camera.zoom;

        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("PS", "@panoroi\n" +
                    "PPtoKP: " + PPtoKP + "\n" +
                    "cos " + cos + ", sin " + sin + "\n" +
                    "atan " + Math.toDegrees(atanRadians) + "\n" +
                    "deltaX: " + deltaX + ", deltaY: " + deltaY);

        panpiste.x += deltaX;
        panpiste.y += deltaY;
    }

    public KameranKasittelija getKameranKasittelija() {
        return kameranKasittelija;
    }
}
