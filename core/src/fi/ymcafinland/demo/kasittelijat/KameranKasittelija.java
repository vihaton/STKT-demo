package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private CameraTransition transition;
    private ZoomTransition zoomTransition;
    private float zoomAlaraja;
    private float zoomYlaraja;

    public KameranKasittelija(OrthographicCamera camera) {
        this.camera = camera;

        this.polttopiste = new Vector3(SelviytyjanPurjeet.TAUSTAN_LEVEYS / 2, SelviytyjanPurjeet.TAUSTAN_KORKEUS / 2, 0f);
        
        //Ilman näitä rivejä zoomin kutsuminen ennen liikkumista aiheuttaa NullPointerExeptionin
        this.transition = new CameraTransition(polttopiste, polttopiste, 0);
        this.zoomTransition = new ZoomTransition(1f, 1f, 0, true);
    }

    public void actZoom(float delta) {
        camera.zoom = zoomTransition.zoomAct(delta);
//        if (SelviytyjanPurjeet.LOG)
//            Gdx.app.log("PS", "camera.zoom: " + camera.zoom);

        //Zoom alaraja on 3/4 nykyisestä zoomista, yläraja 1.75 * normaali zoomi.
        this.zoomAlaraja = (camera.zoom / 4) * 3;
        this.zoomYlaraja = camera.zoom * 1.75f;
    }

    public void setZoomTransition(ZoomTransition zoomTransition) {
        this.zoomTransition = zoomTransition;
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

    public void setTransition(CameraTransition transition) {
        this.transition = transition;
    }

    public void setZoom(float zoom) {
        camera.zoom = zoom;
    }

    public void rotateCamera(float angleToPoint) {
        camera.rotate(-angleToPoint + 90);
    }

}
