package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by xvixvi on 30.4.2016.
 *
 * PerusScreenillä on fitviewportti, orthographic camera, stage, rootTable, skini ja loggaamiseen tarkoitettu logTag.
 *
 * Kaikki rootTableen perustuvaa layoutia käyttävät screenit voivat periä PerusScreenin, joka paketoi loggauksen ja perustoiminnallisuudet,
 * jotka ovat kaikille screeneille samat.
 */
public class PohjaScreen implements Screen {
    protected FitViewport viewport;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected Table rootTable;
    protected Skin skin;

    private String logTag;



    /**
     * Luo cameran, viewporti, rootTablen, stagen ja lisää rootTablen stagen aktoriksi.
     * @param masterSkin
     * @param logTag
     */
    public PohjaScreen(Skin masterSkin, String logTag) {
        this.skin = masterSkin;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
        this.rootTable = new Table();
        rootTable.setFillParent(true);
        this.stage = new Stage(viewport);
        stage.addActor(rootTable);
        this.logTag = logTag;
    }

    @Override
    public void show() {
        Gdx.app.log(logTag, "show() -metodia kutsuttiin");
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Tyhjentää ruudun ja mahdollisesti asettaa debuggauksen päälle.
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.setDebugAll(true);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(logTag, "resize: width " + width + " height " + height);
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        Gdx.app.log(logTag, "pause() - metodia kutsuttiin");
    }

    @Override
    public void resume() {
        Gdx.app.log(logTag, "resume() - metodia kutsuttiin");
    }

    @Override
    public void hide() {
        Gdx.app.log(logTag, "hide() - metodia kutsuttiin");
    }

    /**
     * Dispose metodi sulkee kysymyksen asettamalla selviytyjän purjeisiin kysymykseksi null. Oletettavasti
     * dispose lähettää ensin keräämänsä datan eteenpäin.
     */
    @Override
    public void dispose() {
        Gdx.app.log(logTag, "dispose() - metodia kutsuttiin");
    }
}
