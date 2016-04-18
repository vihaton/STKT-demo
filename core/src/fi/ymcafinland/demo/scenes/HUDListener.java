package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by jwinter on 29.3.2016.
 */
public class HUDListener implements GestureDetector.GestureListener {
    private Viewport viewport;
    private SpriteBatch sb;
    private GestureDetector detector;
    private HUD hud;


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
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //TODO zoomille jotkut rajat (pienin/suurin mahdollinen zoomaus, kuvan rajat)
        hud.playScreen.alkaaTapahtua();
        if (initialDistance < distance) {
        if (initialDistance < distance && hud.playScreen.getZoom() > -2f) {
            hud.playScreen.setZoom(-0.03f);
        } else if (hud.playScreen.getZoom() < 5f) {
            hud.playScreen.setZoom(0.03f);
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
