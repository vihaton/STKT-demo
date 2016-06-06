package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.transitions.CameraTransition;
import fi.ymcafinland.demo.transitions.ZoomTransition;

/**
 * Created by xvixvi on 30.5.2016.
 */
public class KameranKasittelija {
    //todo playscreenista kaikki kameran käsittely tänne

    private OrthographicCamera camera;
    private Vector3 polttopiste;
    private Vector3 panpiste;
    private Vector2 camMax = new Vector2(SelviytyjanPurjeet.TAUSTAN_LEVEYS * 0.8f, SelviytyjanPurjeet.TAUSTAN_KORKEUS * 0.8f);
    private Vector2 camMin = new Vector2(SelviytyjanPurjeet.TAUSTAN_LEVEYS * 0.2f, SelviytyjanPurjeet.TAUSTAN_KORKEUS * 0.2f);
    private CameraTransition transition;
    private ZoomTransition zoomTransition;
    private float zoomAlaraja;
    private float zoomYlaraja;
    private final float transDuration = 1.0f;
    private boolean seurataanPolttoa = true;

    public KameranKasittelija(OrthographicCamera camera, Vector3 keskipiste, Vector3 polttopiste, Vector3 panpiste) {
        this.camera = camera;

        this.polttopiste = polttopiste;
        this.panpiste = panpiste;
        
        //Ilman näitä rivejä zoomin kutsuminen ennen liikkumista aiheuttaa NullPointerExeptionin
        this.transition = new CameraTransition(polttopiste, polttopiste, 0);
        this.zoomTransition = new ZoomTransition(1f, 1f, 0, true);
    }

    public void actZoom(float delta) {
        camera.zoom = zoomTransition.zoomAct(delta);
        if (SelviytyjanPurjeet.LOG) Gdx.app.log("PS", "camera.zoom: " + camera.zoom);

        //Zoom alaraja on 3/4 nykyisestä zoomista, yläraja 1.75 * normaali zoomi.
        this.zoomAlaraja = (camera.zoom / 4) * 3;
        this.zoomYlaraja = camera.zoom * 1.75f;
    }

    public void changeZoom(float endZoom, boolean zoomingIn) {
        this.zoomTransition = new ZoomTransition(camera.zoom, endZoom, transDuration, zoomingIn);
    }

    public void pinchZoom(float increment) {
        if (camera.zoom + increment < zoomYlaraja && camera.zoom + increment > zoomAlaraja) {
            //debug
            //Gdx.app.log("PS", "vanha zoom " + getZoom() + ", uusi " + (z + ratio));
            camera.zoom += increment;
        }
    }

    public void actTransition(float delta) {
        transition.act(delta);
    }

    public void transitionFromTo(Vector3 start, Vector3 finish) {
        this.transition = new CameraTransition(start, finish, transDuration);
    }

    public void setZoom(float zoom) {
        camera.zoom = zoom;
    }

    public void rotateCamera(float angleToPoint) {
        camera.rotate(-angleToPoint + 90);
    }

    public void setSeurataanPolttoa(boolean seurataanPolttoa) {
        this.seurataanPolttoa = seurataanPolttoa;
    }

    public void siirrySeurattavaanPisteeseen() {
        if (seurataanPolttoa) {
            camera.position.set(polttopiste);
            panpiste.x = polttopiste.x;
            panpiste.y = polttopiste.y;

        } else {
            panpiste.x = Math.min(camMax.x, Math.max(panpiste.x, camMin.x));
            panpiste.y = Math.min(camMax.y, Math.max(panpiste.y, camMin.y));
            camera.position.set(panpiste);
        }
    }

    public float getTransDuration() {
        return transDuration;
    }

    public void initialZoom() {
        this.zoomTransition = new ZoomTransition(camera.zoom, 1f, transDuration * 2, true);
    }

    public void reset(Vector3 kpy) {
        transitionFromTo(polttopiste, kpy);
        seurataanPolttoa = true;
    }
}
