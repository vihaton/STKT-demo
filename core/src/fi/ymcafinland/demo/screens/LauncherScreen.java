package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by jwinter on 4.5.2016.
 */
public class LauncherScreen extends PohjaScreen {

    /**
     * Luo cameran, viewporti, rootTablen, stagen ja lisää rootTablen stagen aktoriksi.
     * <p/>
     * Lisäksi luo listan screeniin liittyville stageille siirtymien händläämistä varten.
     *
     * @param masterSkin
     * @param logTag
     */
    public LauncherScreen(Skin masterSkin, String logTag) {
        super(masterSkin, logTag);
    }
}
