package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.screens.PlayScreen;


/**
 * Created by jwinter on 29.3.2016.
 *
 * QuestionScreen luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class QuestionScreen implements Screen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private Solmu solmu;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private BitmapFont fontti;
    //SLIDER TEST V
    final Slider slider;
    Skin skin;
    private TextureAtlas atlas;
    Slider.SliderStyle style;
    Table table;
    Stage stage;
    //SLIDER TEST ^

    public QuestionScreen(SelviytyjanPurjeet sp, Solmu solmu) {
        this.sp = sp;
        this.solmu = solmu;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.stage = new Stage(viewport);

        //SLIDER TEST V
        atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        skin = new Skin();
        skin.addRegions(atlas);
        style = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));
        slider = new Slider(-5, 5, .2f, false, style);
        slider.setAnimateDuration(0.3f);
        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(slider);
        slider.setValue(0);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("UITest", "slider: " + slider.getValue());

            }
        });
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        //SLIDER TEST ^



        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
    }

    /**
     * sendData lähettää saadun datan eteenpäin. sendData konfirmoi kysymykseen laitetun tiedon, ja
     * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
     */
    public void sendData() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        glyphLayout.setText(fontti, "Kolmannen tason");
        float x = (sp.V_WIDTH - glyphLayout.width) / 2;
        float y = (sp.V_HEIGHT  / 2 + glyphLayout.height);

        batch.begin();
        fontti.draw(batch, glyphLayout, x, y);
        y -= glyphLayout.height;
        glyphLayout.setText(fontti, "väittämät");
        fontti.draw(batch, glyphLayout, (sp.V_WIDTH - glyphLayout.width) / 2, y);
        batch.end();
        //SLIDER TEST V
        stage.draw();
        //SLIDER TEST ^

//        if (Gdx.input.isTouched()) {
//            sp.resetPlayScreen();
//            dispose();
//        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Dispose metodi sulkee kysymyksen asettamalla selviytyjän purjeisiin kysymykseksi null. Oletettavasti
     * dispose lähettää ensin keräämänsä datan eteenpäin.
     */
    @Override
    public void dispose() {

    }

}