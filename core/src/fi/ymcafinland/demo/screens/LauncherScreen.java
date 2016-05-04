package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 4.5.2016.
 */
public class LauncherScreen extends PohjaScreen {

    private SelviytyjanPurjeet sp;
    private Texture tausta;
    private SpriteBatch batch;
    private Label otsikko;

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
        this.batch = new SpriteBatch();
        this.tausta = skin.get("launcher", Texture.class);

        taytaRootTable();
    }

    private void taytaRootTable() {
        otsikko = new Label("STKT", skin, "launcherOtsikko");
        rootTable.add(otsikko).expand().top();
        rootTable.row();

        rootTable.validate();
    }

    @Override
    public void show() {
        super.show();
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tausta, 0, 0, viewport.getWorldWidth() + 10, viewport.getWorldHeight());
        batch.end();

        if (Gdx.input.isTouched()) {
            if (Gdx.input.isTouched()) {
                sp.init();
                dispose();
            }
        }
    }
}
