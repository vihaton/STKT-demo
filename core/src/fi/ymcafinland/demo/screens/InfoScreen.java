package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 11.4.2016.
 */
public class InfoScreen implements Screen {

    protected SpriteBatch batch;
    protected Table rootTable;
    protected Label otsikko;

    private FitViewport viewport;
    private OrthographicCamera camera;
    private Texture tausta;
    private ScrollPane pane;
    private Stage stage;
    private Skin skin;
    private Button exitButton;
    private Button alkuButton;


    private static final String infoText =
            "Tähän näkymään tulevat Selviytyjän purjeiden ohjeet: miten ja miksi sitä tehdään.\n\n" +
                    "Tämä demo (v0.2) on kehitysvaiheessa, eikä välttämättä mikään ole vielä lopullista. " +
                    "Erityisesti grafiikat tulevat muuttumaan vielä (moneen) kertaan. Tärkeimpänä kehityksessä " +
                    "pidetään 1) toiminnallisuuksien implementointia, 2) minimikäytettävyyden saavuttamista ja " +
                    "3) grafiikan ja intuitiivisen käytettävyyden saavuttamista.\n\n" +
                    "Tätä sovellusta ovat devanneet Vili Hätönen, Sasu Mäkinen ja Jouni Winter. Materiaalia ovat " +
                    "ensisijaisesti olleet kehittämässä Vili Hätönen, Olli Laukkanen, Tiina Saari ja Sasu Mäkinen.";


    public InfoScreen(SelviytyjanPurjeet sp, Skin masterSkin) {
        this.batch = new SpriteBatch();
        this.skin = masterSkin;
        //todo viewport asettuu myös tietokoneella ajettaessa oikein (stage?)
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
        this.tausta = new Texture("sails02.png");
        this.stage = new Stage();
        luoSisalto(sp);
    }

    public void luoSisalto(SelviytyjanPurjeet sp) {
        rootTable = new Table();
        rootTable.setFillParent(true);

        otsikko = new Label("Selviytyjän purjeet", skin, "otsikko");
        rootTable.add(otsikko).expand().top();
        rootTable.row();

        luoScrollPane();

        rootTable.add(pane).pad(SelviytyjanPurjeet.V_WIDTH / 10).height(100).width(100);
        rootTable.row();

        createExitButton(sp);
        createAlkuTestiButton(sp);

        Table nappiTaulukko = new Table();

        nappiTaulukko.left().add(alkuButton).expandX();
        nappiTaulukko.right().add(exitButton).expandX();

        rootTable.add(nappiTaulukko).padBottom(64).fillX();

        rootTable.validate();

        stage.addActor(rootTable);
        stage.addActor(pane);
    }

    private void luoScrollPane() {
        pane = new ScrollPane(luoInfoteksti());
        pane.setHeight(SelviytyjanPurjeet.V_HEIGHT / 2);
        pane.setWidth(SelviytyjanPurjeet.V_WIDTH / 2);
        pane.setBounds(0, 0, SelviytyjanPurjeet.V_WIDTH * 0.8f, SelviytyjanPurjeet.V_HEIGHT / 2);
        pane.setTouchable(Touchable.enabled);
        pane.validate();
    }

    private Actor luoInfoteksti() {
        Label label = new Label(infoText, skin, "infoteksti");
        label.setWrap(true);
        label.setFontScale(2);
        label.setAlignment(Align.top);
        label.setFillParent(true);

        return label;
    }

    private void createAlkuTestiButton(final SelviytyjanPurjeet sp) {
        alkuButton = new Button(skin.get("alkuButtonStyle", Button.ButtonStyle.class));
        alkuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("IS", "alkutestibuttonia painettiin");
                //todo invoke alkutestinäkymä question screeniin
            }
        });
    }

    public void createExitButton(final SelviytyjanPurjeet sp) {
        exitButton = new Button(skin.get("exitButtonStyle", Button.ButtonStyle.class));
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("IS", "exitbuttonia painettiin");
                sp.resetPlayScreen();
                stage.dispose();
                dispose();
            }
        });
    }

    @Override
    public void show() {
        Gdx.app.log("IS", "show() -metodia kutsuttiin");
        Gdx.input.setInputProcessor(stage);

        float rgbJakaja = 255f;
        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.setDebugAll(true);

        batch.begin();
        batch.draw(tausta, 0, 0, tausta.getWidth(), tausta.getHeight());
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
