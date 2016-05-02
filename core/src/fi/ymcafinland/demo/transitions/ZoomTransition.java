package fi.ymcafinland.demo.transitions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by xvixvi on 2.5.2016.
 */
public class ZoomTransition {

    private final float startZoom;
    private final float endZoom;
    private final float duration;
    private final boolean alas;

    private float stateTime = 0.0f;
    private float percent;

    public ZoomTransition(float startZoom, float endZoom, float durationMS, boolean alas) {
        this.startZoom = startZoom;
        this.endZoom = endZoom;
        this.duration = durationMS;
        this.alas = alas;
    }

    public float zoomAct(float delta) {
        stateTime += delta;

        percent = MathUtils.clamp(stateTime / duration, 0.0f, 1.0f);

        if (alas) {
            return Interpolation.bounceOut.apply(startZoom, endZoom, percent);
        } else {
            return MathUtils.clamp(Interpolation.exp5Out.apply(startZoom, endZoom, percent), 0.1f, Float.MAX_VALUE);
        }
    }

}
