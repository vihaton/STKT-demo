package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Sasu on 27.3.2016.
 */
class CameraTransition {

    private final Vector3 startPos;
    private final Vector3 goalPos;
    private final float duration;

    private float stateTime = 0.0f;
    private float alpha;

    public CameraTransition(final Vector3 startPos, final Vector3 goalPos, float duration) {
        this.startPos = startPos;
        this.goalPos = goalPos;
        this.duration = duration;
    }

    public Vector3 act(float delta) {
        stateTime += delta;

        alpha = MathUtils.clamp(stateTime / duration, 0.0f, 1.0f);

        return startPos.lerp(goalPos, -2 * alpha*alpha*alpha + 3 * alpha*alpha);
    }

}
