package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by jwinter on 29.3.2016.
 */
public class HUDListener implements GestureDetector.GestureListener {
    private Viewport viewport;
    private SpriteBatch sb;
    private GestureDetector detector;
    private HUD hud;

    //todo zoomit, swaipit ja tapit yhteisymmärrykseen
    public HUDListener(HUD hud, Viewport viewport, SpriteBatch sb) {
        this.hud = hud;
        this.viewport = viewport;
        this.sb = sb;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(count >1) {
            hud.siirryLahinpaanPalloon(x, y);
        }
        return false; //kertoo, että eventti on jo käsitelty: jätetään täpissä falseksi jotta playscreenin stagen buttonit toimivat.
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(velocityX > 250 || velocityY > 250) {
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
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Vector3 touchPos = new Vector3(x,y,0);
        hud.playScreen.getCamera().unproject(touchPos);
        hud.playScreen.getPolttopiste().x = touchPos.x;
        hud.playScreen.getPolttopiste().y = touchPos.y;


        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        hud.playScreen.alkaaTapahtua();
        if (initialDistance < distance && hud.playScreen.getZoom() > 0.2f) {
            hud.playScreen.setZoom(-0.03f);
        } else if (hud.playScreen.getZoom() < 5f) {
            hud.playScreen.setZoom(0.03f);
        }
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
