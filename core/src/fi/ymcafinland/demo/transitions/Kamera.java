package fi.ymcafinland.demo.transitions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.screens.PlayScreen;

/**
 * Created by xvixvi on 19.5.2016.
 */
public class Kamera extends OrthographicCamera {

    private  float deltaAVG;
    protected boolean zoomedOut;
    protected boolean trans;
    private float angleToPoint;
    private PlayScreen playScreen;
    private ZoomTransition zoomTransition;
    private final float zoomDuration = 1.0f;    //s



    public Kamera(PlayScreen playScreen) {
        this.playScreen = playScreen;

        this.zoomTransition = new ZoomTransition(1f, 1f, 0, true);

        this.deltaAVG = 0.02f;
        //näkymä on aluksi kaukana
        zoomedOut = false;
        this.zoom = 4f;
    }

    public void paivitaKamera(float delta) {
        this.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);

        if (playScreen.seurataanPolttoa) {
            this.position.set(playScreen.polttopiste);
            rotateCamera();
        } else {
            this.position.set(playScreen.panpiste);
        }
        actZoom(delta);

        this.update();
    }



    private void actZoom(float delta) {
        this.zoom = zoomTransition.zoomAct(delta);
        //Gdx.app.log("PS", "camera.zoom: " + camera.zoom);
    }

    private void rotateCamera() {
        angleToPoint = getAngleToPoint(playScreen.polttopiste, playScreen.keskipiste);
        this.rotate(-angleToPoint + 90);
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




    public void setZoom(float ratio) {
        float z = getZoom();
        if (z + ratio < 3 && z + ratio > 0.75) {
            //debug
            //Gdx.app.log("PS", "vanha zoom " + getZoom() + ", uusi " + (z + ratio));
            this.zoom += ratio;
        }
    }

    public float getZoom() {
        return this.zoom;
    }

    public void zoomaaNormaaliin() {
        playScreen.seurataanPolttoa = true;
        zoomTransition = new ZoomTransition(this.zoom, 1f, zoomDuration * 2, true);

    }

    public void zoomaaUlos() {
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
