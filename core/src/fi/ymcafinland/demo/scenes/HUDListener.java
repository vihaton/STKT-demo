package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jwinter on 29.3.2016.
 */
public class HUDListener implements GestureDetector.GestureListener {

    private HUD hud;

    //todo zoomit, swaipit ja tapit yhteisymmärrykseen
    public HUDListener(HUD hud) {
        this.hud = hud;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //debug
        Gdx.app.log("HLIST", "tap -metodia kutsuttu");

        hud.siirryLahinpaanPalloon(x, y);
        return false; //kertoo, että eventti on jo käsitelty: jätetään täpissä falseksi jotta playscreenin stagen buttonit toimivat.
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //debug
        if (!hud.playScreen.zoomedOut) {
            return false;
        }
        if (Math.abs(velocityX) > 250 || Math.abs(velocityY) > 250) {
            return false;
        }
        Gdx.app.log("HLIST", "fling -metodia kutsuttu");
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                hud.right();
            } else {
                hud.left();
            }
        } else {
            if (velocityY > 0) {
                hud.down();
            } else {
                hud.up();
            }
        }


        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //debug
        Gdx.app.log("HLIST", "pan -metodia kutsuttu, deltaX: " + deltaX + ", deltaY: " + deltaY);

        hud.playScreen.panoroi(deltaX, deltaY);

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        if (!hud.playScreen.zoomedOut) {
            Gdx.app.log("HLIST", "panStop -metodia kutsuttu");
            hud.playScreen.resetPan();
        }

        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //debug
        Gdx.app.log("HLIST", "zoom -metodia kutsuttu");

        hud.playScreen.alkaaTapahtua();
        if (initialDistance < distance) {
            hud.playScreen.kamera.setZoom(-0.03f);
        } else {
            hud.playScreen.kamera.setZoom(0.03f);
        }
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
