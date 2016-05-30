package fi.ymcafinland.demo.transitions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fi.ymcafinland.demo.screens.PohjaScreen;

/**
 * Created by xvixvi on 2.5.2016.
 * <p/>
 * Vaihtaa Screenin toiseksi, erilaisia mahdollisuuksia.
 */
public class ScreenTransition {

    private final PohjaScreen startScreen;
    private final PohjaScreen endScreen;
    private final float duration;
    private final Stage startStage;
    private final Stage endStage;

    public ScreenTransition(PohjaScreen startScreen, PohjaScreen endScreen, float duration) {
        this.startScreen = startScreen;
        this.startStage = startScreen.getStage();
        this.endScreen = endScreen;
        this.endStage = endScreen.getStage();

        this.duration = duration;
    }

    public void actTransition() {

    }

    /**
     * Vaihtaa screenin toiseen animoimalla startScreenin feidaamalla endScreeniksi.
     */
    public void fadeTransition() {
        for (Stage stage : startScreen.getScreeniinLiittyvatStaget()) {
            stage.addAction(Actions.fadeOut(duration));
        }

        lisaaScreeninVaihtoStartStagenToimintoihin();
    }

    public void wildFadeTransition() {
        for (Stage stage : startScreen.getScreeniinLiittyvatStaget()) {
            stage.addAction(Actions.fadeIn(duration, Interpolation.elastic));
        }

        lisaaScreeninVaihtoStartStagenToimintoihin();
    }

    public void lisaaScreeninVaihtoStartStagenToimintoihin() {
        startStage.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(endScreen);
            }
        }));
    }
}
