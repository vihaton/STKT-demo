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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    protected Table rootTable;
    protected TextField textField;

    private FitViewport viewport;
    private OrthographicCamera camera;
    private BitmapFont fontti;
    private Sprite tausta;
    private ScrollPane pane;
    private Stage stage;
    private Button exitButton;

    private static final String infoText =
            "Tähän näkymään tulevat Selviytyjän purjeiden ohjeet: miten ja miksi sitä tehdään.\n\n" +
                    "Tämä demo (v0.2) on kehitysvaiheessa, eikä välttämättä mikään ole vielä lopullista. " +
                    "Erityisesti grafiikat tulevat muuttumaan vielä (moneen) kertaan. Tärkeimpänä kehityksessä " +
                    "pidetään 1) toiminnallisuuksien implementointia, 2) minimikäytettävyyden saavuttamista ja " +
                    "3) grafiikan ja intuitiivisen käytettävyyden saavuttamista.\n\n" +
                    "Tätä sovellusta ovat devanneet Vili Hätönen, Sasu Mäkinen ja Jouni Winter. Materiaalia ovat " +
                    "ensisijaisesti olleet kehittämässä Vili Hätönen, Olli Laukkanen, Tiina Saari ja Sasu Mäkinen.";


    public InfoScreen(SelviytyjanPurjeet sp) {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        //todo viewport asettuu myös tietokoneella ajettaessa oikein (stage?)
        this.viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.tausta = new Sprite(new Texture("sails02.png"));
        this.stage = new Stage(viewport);
        createExitButton(sp);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.WHITE;

        textField = new TextField("Selviytyjän purjeet", skin);

        Label label = new Label(infoText, labelStyle);
        label.setWrap(true);
        label.setFontScale(2);
        label.setAlignment(Align.bottom);
        label.pack();
        label.setFillParent(true);

        pane = new ScrollPane(label);
        pane.setBounds(SelviytyjanPurjeet.V_WIDTH / 10f, SelviytyjanPurjeet.V_HEIGHT / 5,
                SelviytyjanPurjeet.V_WIDTH * 0.8f, SelviytyjanPurjeet.V_HEIGHT / 2);
        pane.setTouchable(Touchable.enabled);
        pane.validate();

        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.bottom().padBottom(48).add(exitButton);
        stage.addActor(rootTable);
        stage.addActor(pane);
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
