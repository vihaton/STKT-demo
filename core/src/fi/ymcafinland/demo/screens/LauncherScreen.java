package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 4.5.2016.
 */
public class LauncherScreen extends PohjaScreen {

    private SelviytyjanPurjeet sp;
    private Texture tausta;
    private SpriteBatch batch;

    /**
     * Luo cameran, viewporti, rootTablen, stagen ja lisää rootTablen stagen aktoriksi.
     * <p/>
     * Lisäksi luo listan screeniin liittyville stageille siirtymien händläämistä varten.
     *
     * @param masterSkin
     * @param logTag
     */
    public LauncherScreen(SelviytyjanPurjeet sp, Skin masterSkin, String logTag) {
        super(masterSkin, logTag);
        this.sp = sp;
        this.tausta = skin.get("launcherTausta", Texture.class);
        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tausta, 0, 0, tausta.getWidth(), tausta.getHeight());
        batch.end();

        if (Gdx.input.isTouched()) {
            if (Gdx.input.isTouched()) {
                sp.init();
                dispose();
            }
        }
    }
}
