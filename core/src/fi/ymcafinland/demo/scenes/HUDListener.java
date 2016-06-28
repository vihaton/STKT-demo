package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import fi.ymcafinland.demo.kasittelijat.KameranKasittelija;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 29.3.2016.
 */
public class HUDListener implements GestureDetector.GestureListener {

    private HUD hud;
    private KameranKasittelija kameranKasittelija;
    float delta = 0.02f;
    float timer = 0;
    boolean ENSIMMAINEN_PANOROINTI_FLAG = true;

    public HUDListener(HUD hud, KameranKasittelija kameranKasittelija) {
        this.hud = hud;
        this.kameranKasittelija = kameranKasittelija;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        //debug
        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "tap -metodia kutsuttu");

        hud.playScreen.hoidaKosketus(x, y);
        return false; //kertoo, että eventti on jo käsitelty: jätetään täpissä falseksi jotta playscreenin stagen buttonit toimivat.
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "fling -metodia kutsuttu");

        if (timer > 0.1f || timer == 0 || hud.playScreen.ZOOMED_OUT_FLAG) { //jos panoroinnin alusta on kulunut yli 0.1s tai panStop on jo käsitellyt tapahtuman tai ollaan zoomattu ulos
            return false;
        }

        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "suoritetaan fling toiminnallisuus");

        if (hud.playScreen.getSolmu().getID().equals("0")) {
            flingKeskella(velocityX, velocityY);
        } else {
            flingReunalla(velocityX, velocityY);
        }

        timer = 0;
        return false;
    }

    private void flingReunalla(float velocityX, float velocityY) {
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
    }

    /**
     * Hoitaa flingauksen, kun ollaan keskipisteessä. Toistaiseksi tynkä, laajennettavissa myöhemmin.
     *
     * @param velocityX
     * @param velocityY
     */
    private void flingKeskella(float velocityX, float velocityY) {
        hud.playScreen.siirryKeskipisteestaLahinpaanSolmuun();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        //debug
        if (ENSIMMAINEN_PANOROINTI_FLAG) kameranKasittelija.keskeytaKameranTransitio();
        ENSIMMAINEN_PANOROINTI_FLAG = false;

        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "pan -metodia kutsuttu, timer: " + timer);

        timer += delta;
        hud.playScreen.panoroi(deltaX, deltaY);

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "panStop -metodia kutsuttu");

        if (timer > 0.1f || hud.playScreen.ZOOMED_OUT_FLAG) {
            if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "suoritetaan panStop toiminnallisuus");
            hud.playScreen.siirraPanPistePolttopisteeseen();
            timer = 0;
            ENSIMMAINEN_PANOROINTI_FLAG = true;
        }
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //debug
        if (SelviytyjanPurjeet.LOG) Gdx.app.log("HLIST", "zoom -metodia kutsuttu");

        hud.playScreen.alkaaTapahtua();
        if (initialDistance < distance) {
            kameranKasittelija.pinchZoom(-0.03f);
        } else {
            kameranKasittelija.pinchZoom(0.03f);
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void paivitaDelta(float delta) {
        this.delta = delta;
    }
}
