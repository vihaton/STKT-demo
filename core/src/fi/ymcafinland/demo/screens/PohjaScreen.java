package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by xvixvi on 30.4.2016.
 * <p/>
 * PerusScreenillä on fitviewportti, orthographic camera, stage, rootTable, skini ja loggaamiseen tarkoitettu logTag.
 * <p/>
 * Kaikki rootTableen perustuvaa layoutia käyttävät screenit voivat periä PerusScreenin, joka paketoi loggauksen ja perustoiminnallisuudet,
 * jotka ovat kaikille screeneille samat.
 */
public class PohjaScreen implements Screen {
    protected FitViewport viewport;
    protected OrthographicCamera camera;
    public Stage stage;
    protected Table rootTable;
    protected Skin skin;
    protected ArrayList<Stage> screeniinLiittyvatStaget;

    private String logTag;

    /**
     * Luo cameran, viewporti, rootTablen, stagen ja lisää rootTablen stagen aktoriksi.
     * <p/>
     * Lisäksi luo listan screeniin liittyville stageille siirtymien händläämistä varten.
     *
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
        screeniinLiittyvatStaget = new ArrayList<>();
        screeniinLiittyvatStaget.add(stage);
        this.logTag = logTag;
    }

    @Override
    public void show() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "show() -metodia kutsuttiin");
        Gdx.input.setInputProcessor(stage);

        for (Stage stage : screeniinLiittyvatStaget) {
            stage.addAction(Actions.alpha(0));      //asettaa stagen täysin feidatuksi
            stage.addAction(Actions.fadeIn(0.5f)); //feidaa stagen takaisin näkyviin
        }
    }

    /**
     * Tyhjentää ruudun ja mahdollisesti asettaa debuggauksen päälle.
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Stage stage : screeniinLiittyvatStaget) {
//            stage.setDebugAll(true);
            stage.act();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "resize: width " + width + " height " + height);
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "pause() - metodia kutsuttiin");
    }

    @Override
    public void resume() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "resume() - metodia kutsuttiin");
    }

    @Override
    public void hide() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "hide() - metodia kutsuttiin");
    }

    /**
     * Dispose metodi sulkee kysymyksen asettamalla selviytyjän purjeisiin kysymykseksi null. Oletettavasti
     * dispose lähettää ensin keräämänsä datan eteenpäin.
     */
    @Override
    public void dispose() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log(logTag, "dispose() - metodia kutsuttiin");
    }

    public ArrayList<Stage> getScreeniinLiittyvatStaget() {
        return screeniinLiittyvatStaget;
    }

    public void lisaaStage(Stage stage) {
        screeniinLiittyvatStaget.add(stage);
    }

    public Stage getStage() {
        return stage;
    }
}
