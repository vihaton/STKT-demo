package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.ymcafinland.demo.kasittelijat.DialoginKasittelija;
import fi.ymcafinland.demo.kasittelijat.EdistymismittarinKasittelija;
import fi.ymcafinland.demo.kasittelijat.InfoButtonKasittelija;
import fi.ymcafinland.demo.kasittelijat.KameranKasittelija;
import fi.ymcafinland.demo.kasittelijat.SolmunKasittelija;
import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.scenes.HUD;

/**
 * Created by Sasu on 27.3.2016.
 */
public class PlayScreen extends PohjaScreen {

    private final DialoginKasittelija dialoginKasittelija;
    private SelviytyjanPurjeet sp;
    private Verkko verkko;
    private HUD hud;
    private SpriteBatch batch;
    private KameranKasittelija kameranKasittelija;
    private SolmunKasittelija solmunKasittelija;
    private EdistymismittarinKasittelija edistymismittarinKasittelija;
    private InfoButtonKasittelija infoButtonKasittelija;
    private Solmu solmu;
    private boolean TRANSITION_FLAG = false;
    public boolean ZOOMED_OUT_FLAG = false;

    //todo pelkästään kamera tietää mitään pisteistä, pois PS.stä
    public Vector3 polttopiste;
    public Vector3 panpiste;
    private Vector3 keskipiste;
    private float angleToPoint;

    private long stateTime;
    private long timer;
    private float currentZoomDuration = 3.0f;      // viimeisimpänä suoritetun zoomin kesto
    private float timeSinceLastZoomEvent = 0;   // kumulatiivinen deltalukema, nollataan zoomatessa
    private final float minFPS = 45;         //fps
    private final float renderinLoggausAlaraja = (float) Math.pow(minFPS, -1.0);  //s, eli deltan maximiarvo (jos delta on isompi kuin tämä, niin fps on liian pieni
    private final float maxDuration = 1000f;
    private final int idleTime = 3000; //ms
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

        this.kameranKasittelija = new KameranKasittelija(camera, polttopiste, panpiste);
        this.solmunKasittelija = new SolmunKasittelija(stage, sp.getVerkko(), masterSkin, pelaaja, sp.getVaittamat());
        this.edistymismittarinKasittelija = new EdistymismittarinKasittelija(stage, masterSkin, pelaaja);
        this.infoButtonKasittelija = new InfoButtonKasittelija(stage, masterSkin, verkko);

        //  "The image's dimensions should be powers of two (16x16, 64x256, etc) for compatibility and performance reasons."
        this.batch = new SpriteBatch();

        //näkymä on aluksi kaukana
        ZOOMED_OUT_FLAG = false;
        kameranKasittelija.setZoom(4f);

        // Playscreen ei tunne sovelluksen inputprocessoria, vaan tietää HUDin joka huolehtii I/O:sta.
        this.hud = new HUD(this, batch, masterSkin, solmu);

        this.timer = System.currentTimeMillis();
        this.angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        this.stateTime = 0;

        Gdx.graphics.requestRendering();

        dialoginKasittelija = new DialoginKasittelija(verkko, masterSkin);
    }

    @Override
    public void show() {
        super.show();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        edistymismittarinKasittelija.paivitaMittarinArvo(renderinLoggausAlaraja); //päivitetään edistymismittarin arvo vain kun siirrytään playscreeniin
        solmunKasittelija.paivitaGlowAnimaatiot();
        hud.resetInputProcessor();
    }

    public void alkaaTapahtua() {
        TRANSITION_FLAG = true;
        stateTime = 0;
        timer = System.currentTimeMillis();
        hud.update(solmu, ZOOMED_OUT_FLAG);
        //debug
        if (SelviytyjanPurjeet.SPAMLOG)
            Gdx.app.log("PS", "UUSI SIIRTO" + stateTime + " " + TRANSITION_FLAG);
    }

    @Override
    public void render(float delta) {
        //debug
        boolean log = false;
        if (delta > renderinLoggausAlaraja && SelviytyjanPurjeet.SPAMLOG) {
            Gdx.app.log("PS", "renderloggaus käynnistetty\n" +
                    "minimi fps:" + minFPS + " fps, tämän ruudun fps:" + Math.pow(delta, -1) + " fps\n" +
                    "stateTime:" + stateTime + "ms TRANSITION_FLAG:" + TRANSITION_FLAG + " delta:" + delta);
            log = true;
        }

        super.render(delta);

        // Lasketaan kumulatiivinen delta siten ettei ole mahdollisuutta ylivuotoon
        if (timeSinceLastZoomEvent + delta < Float.MAX_VALUE) this.timeSinceLastZoomEvent += delta;

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        if (TRANSITION_FLAG) actTransition(delta);

        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter actTransition");

        kameranKasittelija.siirrySeurattavaanPisteeseen();
        kameranKasittelija.rotateCamera(getAngleToPoint(polttopiste, keskipiste));
        if (timeSinceLastZoomEvent < currentZoomDuration) kameranKasittelija.actZoom(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        paivitaKasittelijat(delta);
        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter käsittelijöiden päivitys");

        stage.draw();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (log)
            Gdx.app.log("PS", "time in render:" + (System.currentTimeMillis() - timer - stateTime) + "ms @fter stagejen piirtämiset");

        hud.paivitaDelta(delta);
    }

    public void paivitaKasittelijat(float delta) {
        angleToPoint = getAngleToPoint(polttopiste, keskipiste);
        solmunKasittelija.paivitaSolmut(angleToPoint);
        edistymismittarinKasittelija.pyoritaMittaria(angleToPoint);
        infoButtonKasittelija.paivitaInfoButtonit(delta, angleToPoint, ZOOMED_OUT_FLAG);
    }

    public void actTransition(float delta) {
        TRANSITION_FLAG = kameranKasittelija.actTransition(delta);
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
        timeSinceLastZoomEvent = 0;
        if (in) {
            kameranKasittelija.setSeurataanPolttoa(true);
            kameranKasittelija.transitionFromTo(polttopiste, new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f));
            kameranKasittelija.changeZoom(1f, true);
            ZOOMED_OUT_FLAG = false;
        } else {
            kameranKasittelija.transitionFromTo(polttopiste, keskipiste.cpy().lerp(polttopiste, 0.05f));
            kameranKasittelija.changeZoom(6f, false);
            ZOOMED_OUT_FLAG = true;
        }
        currentZoomDuration = kameranKasittelija.getTransDuration() * 3;
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
            ensimmainenSiirtyma = false;
            Vector3 goal = new Vector3(solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), 0f);
            this.solmu = solmu;
            alkaaTapahtua();
            kameranKasittelija.setSeurataanPolttoa(true);
            kameranKasittelija.transitionFromTo(polttopiste, goal);

            dialoginKasittelija.poistaDialogit();
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

    public void siirryKeskipisteestaLahinpaanSolmuun() {

    }

    /**
     * Selvittää, mitä tehdään kosketuksella kohtaan x,y
     *
     * @param x näytön suhteellinen x
     * @param y -:- y
     */
    public void hoidaKosketus(float x, float y) {

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
            hoidaKosketusSolmuun(trueX, trueY, tappaustaLahinSolmu);
        }
    }

    private void hoidaKosketusSolmuun(float trueX, float trueY, Solmu tappaustaLahinSolmu) {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("PS", "kosketus osui tarpeeksi lähelle solmua " + tappaustaLahinSolmu.getID() + "\n" +
                    "täppäyksen etäisyys solmuun " + Math.hypot(tappaustaLahinSolmu.getXKoordinaatti() - trueX, tappaustaLahinSolmu.getYKoordinaatti() - trueY));

        int solmunID = Integer.parseInt(tappaustaLahinSolmu.getID());

        if (ZOOMED_OUT_FLAG) {
            setSolmu(tappaustaLahinSolmu);
            asetaAlkuZoom();
        } else if (solmunID == 0) {
            sp.setPalauteScreen();
        } else {
            naytaDialogi(tappaustaLahinSolmu);
        }
    }

    private void naytaDialogi(Solmu solmu) {
        if (ZOOMED_OUT_FLAG || dialoginKasittelija.DIALOG_FLAG) {
            dialoginKasittelija.poistaDialogit();
            return;
        }

        float PPtoKP = getAngleToPoint(polttopiste, keskipiste);
        dialoginKasittelija.naytaDialogi(stage, solmu, solmu.getXKoordinaatti(), solmu.getYKoordinaatti(), PPtoKP);
    }

    public void asetaAlkuZoom() {
        ZOOMED_OUT_FLAG = false;
        alkaaTapahtua();
        kameranKasittelija.initialZoom();
    }

    public void paivitaPisteenKoordinaatit(Vector3 paivitettava, Vector3 kopioitava) {
        paivitettava.x = kopioitava.x;
        paivitettava.y = kopioitava.y;
    }

    public void siirraPanPistePolttopisteeseen() {
        alkaaTapahtua();
        kameranKasittelija.nopeaSiirtyminenPPtoKP();
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

        if (SelviytyjanPurjeet.SPAMLOG)
            Gdx.app.log("PS", "@panoroi\n" +
                    "PPtoKP: " + PPtoKP + "\n" +
                    "cos " + cos + ", sin " + sin + "\n" +
                    "atan " + Math.toDegrees(atanRadians) + "\n" +
                    "deltaX: " + deltaX + ", deltaY: " + deltaY);

        panpiste.x += deltaX;
        panpiste.y += deltaY;
    }

    public Solmu getSolmu() {
        return solmu;
    }

    public KameranKasittelija getKameranKasittelija() {
        return kameranKasittelija;
    }

    public void siirraKameraPolttopisteeseen() {
        kameranKasittelija.setSeurataanPolttoa(true);
        kameranKasittelija.siirrySeurattavaanPisteeseen();
    }
}
