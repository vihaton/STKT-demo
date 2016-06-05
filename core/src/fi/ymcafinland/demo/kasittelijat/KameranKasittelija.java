package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.graphics.OrthographicCamera;

import fi.ymcafinland.demo.transitions.ZoomTransition;

/**
 * Created by xvixvi on 30.5.2016.
 */
public class KameranKasittelija {
    //todo playscreenista kaikki kameran käsittely tänne

    private OrthographicCamera camera;
    private ZoomTransition zoomTransition;

    private float zoomAlaraja;
    private float zoomYlaraja;

    public KameranKasittelija(OrthographicCamera camera) {
        this.camera = camera;
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
}
