package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 29.3.2016.
 */
public class HUDListener implements GestureDetector.GestureListener {

    private HUD hud;
    float delta;
    float timer;

    public HUDListener(HUD hud, float delta) {
        this.hud = hud;
        this.delta = delta;
        this.timer = 0;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //debug
        if (SelviytyjanPurjeet.LOG)
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
        fling(velocityX,  velocityY,button, delta);
        return false;
    }


    public boolean fling(float velocityX, float velocityY, int button, float delta) {
        //debug

        if (timer > 0.1f || timer == 0) {
            return false;
        }



        if (SelviytyjanPurjeet.LOG)
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

        timer = 0;
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //debug
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HLIST", "pan -metodia kutsuttu " + timer);
        timer += delta;
        hud.playScreen.panoroi(deltaX, deltaY);

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        if (hud.playScreen.zoomedOut || timer < 0.1f) {
            return false;
        }
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HLIST", "panStop -metodia kutsuttu");
        hud.playScreen.resetPan();

        timer = 0;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //debug
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HLIST", "zoom -metodia kutsuttu");

        hud.playScreen.alkaaTapahtua();
        if (initialDistance < distance) {
            hud.playScreen.kamera.setZoom(-0.04f);
        } else {
            hud.playScreen.kamera.setZoom(0.04f);
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    public void paivitaDelta(float delta){
        this.delta = delta;
    }
}
