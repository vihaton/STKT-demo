package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
    Table table;
    TextField textField;
    private Button exitButton;


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
        //todo viewport asettuu myös tietokoneella ajettaessa oikein (stage?)
        this.viewport = new FitViewport(sp.V_WIDTH, sp.V_HEIGHT, camera);

        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.tausta = new Sprite(new Texture("sails02.png"));
        this.stage = new Stage(viewport);
        createExitButton(sp);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.valueOf("A07E10FF");

        textField = new TextField("Selviytyjän purjeet", skin);

        Label label = new Label(reallyLongString, labelStyle);
        label.setWidth(sp.V_WIDTH / 3);
        label.setWrap(true);
        label.setFontScale(2);
        label.setAlignment(Align.bottom);
        label.pack();
        label.setFillParent(true);
        pane = new ScrollPane(label);
        pane.setBounds(sp.V_WIDTH / 3.5f, sp.V_HEIGHT / 5, sp.V_WIDTH / 2, sp.V_HEIGHT / 2); //This should be the bounds of the scroller and the scrollable content need to be inside this
        pane.layout();
        pane.setTouchable(Touchable.enabled);
        this.stage = new Stage(viewport);

        table = new Table();
        table.setFillParent(true);

        table.bottom().padBottom(48).add(exitButton);
        stage.addActor(table);



        stage.addActor(pane);

        camera.setToOrtho(false, sp.V_WIDTH, sp.V_HEIGHT);
    }

    public void createExitButton(final SelviytyjanPurjeet sp) {
        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        Texture texture = new Texture("ruksi.png");

        styleExit.up = new TextureRegionDrawable(new TextureRegion(texture));
        exitButton = new Button(styleExit);
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                sp.resetPlayScreen();
                stage.dispose();
                dispose();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        float rgbJakaja = 255f;
        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(stage);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        tausta.draw(batch);
        fontti.draw(batch, "Selviytyjän purjeet", 25 , 900);
        batch.end();
        pane.act(delta);
        stage.draw();

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

    @Override
    public void dispose() {

    }
}
