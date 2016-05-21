package fi.ymcafinland.demo.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.screens.PlayScreen;

/**
 * Created by xvixvi on 19.5.2016.
 */
public class Kamera extends OrthographicCamera {

    private float deltaAVG;
    protected boolean zoomedOut;
    protected boolean trans;
    private PlayScreen playScreen;
    private ZoomTransition zoomTransition;
    private final float zoomDuration = 1.0f;    // s
    private float currentZoomDuration = 0;      // viimeisimpänä suoritetun zoomin kesto
    private float timeSinceLastZoomEvent = 0;   // kumulatiivinen deltalukema, nollataan zoomatessa
    private float ylaraja;
    private float alaraja;


    public Kamera(PlayScreen playScreen) {
        this.playScreen = playScreen;

        this.zoomTransition = new ZoomTransition(1f, 1f, 0, true);

        this.deltaAVG = 0.02f;
        //näkymä on aluksi kaukana
        zoomedOut = false;
        this.zoom = 4f;
    }

    public void paivitaKamera(float delta) {
        // Lasketaan kumulatiivinen delta siten ettei ole mahdollisuutta ylivuotoon
        if (timeSinceLastZoomEvent < Float.MAX_VALUE) {
            this.timeSinceLastZoomEvent += delta;
        }

        this.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);
        if (playScreen.seurataanPolttoa) {
            this.position.set(playScreen.polttopiste);
            playScreen.panpiste.x = playScreen.polttopiste.x;
            playScreen.panpiste.y = playScreen.polttopiste.y;

        } else {
            this.position.set(playScreen.panpiste);
        }
        rotateCamera();

        //actZoomia kutsutaan vain jos tällä zoomTransition on käynnissä
        if (timeSinceLastZoomEvent < currentZoomDuration) {
            actZoom(delta);
        }

        this.update();
    }


    private void actZoom(float delta) {
        this.zoom = zoomTransition.zoomAct(delta);

        //Zoom alaraja on 3/4 nykyisestä zoomista, yläraja 1.75 * normaali zoomi.
        this.alaraja = (zoom / 4) * 3;
        this.ylaraja = zoom * 1.75f;

        //Debug
        //Gdx.app.log("Kamera", "camera.zoom: " + camera.zoom);
        //Gdx.app.log("Kamera", "alaraja: " + alaraja + "ylaraja:" + ylaraja);
    }

    private void rotateCamera() {
        this.rotate(-playScreen.getAngleToPoint(playScreen.polttopiste, playScreen.keskipiste) + 90);
    }


    public void setZoom(float lisays) {
        float z = getZoom();
        float uusiZoom = z + lisays;
        if (uusiZoom < ylaraja && uusiZoom > alaraja) {
            //debug
//            Gdx.app.log("Kamera", "vanha zoom " + getZoom() + ", uusi " + (z + increment));
            this.zoom = uusiZoom;
        }
    }

    public float getZoom() {
        return this.zoom;
    }

    public void zoomaaNormaaliin() {
        timeSinceLastZoomEvent = 0;
        currentZoomDuration = zoomDuration * 2;

        zoomTransition = new ZoomTransition(this.zoom, 1f, zoomDuration * 2, true);
//        alaraja = 0.75f;
//        ylaraja = 3f;
    }

    public void zoomaaUlos() {
        timeSinceLastZoomEvent = 0;
        currentZoomDuration = zoomDuration;

        zoomTransition = new ZoomTransition(this.zoom, 6f, zoomDuration, false);
    }

    private float deltaManipulation(float delta) {
        if (delta > 0.1f || delta < 0.005f) {
            delta = deltaAVG;
        }

        deltaAVG = (deltaAVG * 19 + delta) / 20;

        return delta;
    }
}
