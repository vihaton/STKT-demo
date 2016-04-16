package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 11.4.2016.
 */
public class InfoScreen implements Screen {
    protected SpriteBatch batch;

    private final SelviytyjanPurjeet sp;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private BitmapFont fontti;
    private Sprite tausta;
    private ScrollPane pane;
    private Stage stage;

    private static final String reallyLongString =
            "This is a really long string that has lots of lines and repeats itself over and over again This is a really long string that has" +
                    " lots of lines and repeats itself over and over again This is a really long string that has lots of lines and repeats itself over and over"+
                    " again This is a really long string that has lots of lines and repeats itself over and over again"+
                    " This is a really long string that has lots of lines and repeats itself over and over again This is a really long string that has lots"+
                    " of lines and repeats itself over and over again";

    public InfoScreen(SelviytyjanPurjeet sp) {
        this.sp = sp;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.tausta = new Sprite(new Texture("sails02.png"));
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextArea textArea = new TextArea(reallyLongString, skin);
        this.pane = new ScrollPane(textArea, skin);

        stage.addActor(pane);


        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        float rgbJakaja = 255f;
        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        pane.draw(batch, 500);
        tausta.draw(batch);
        fontti.draw(batch, "Selviytyjän purjeet", 25 , 900);
        batch.end();

        if (Gdx.input.isTouched()) {
            sp.resetPlayScreen();
            dispose();
        }
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

    @Override
    public void dispose() {

    }
}
