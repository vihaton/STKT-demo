package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 4.5.2016.
 */
public class LauncherScreen extends PohjaScreen {

    private SelviytyjanPurjeet sp;
    private Texture tausta;
    private SpriteBatch batch;
    private Label otsikko;
    private Button spButton;
    private Button mockButton1;
    private Button mockButton2;
    private String logtag;

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
        this.logtag = logTag;

        taytaRootTable();
    }

    private void taytaRootTable() {
        otsikko = new Label("S T K T -demo", skin, "otsikko");
        rootTable.add(otsikko).expand().top();
        rootTable.row().height(20);

        createButtons();

        Table nappitaulukko = new Table();

        nappitaulukko.add(spButton).expandX().pad(5);
        nappitaulukko.add(mockButton1).expandX().pad(5);
        nappitaulukko.add(mockButton2).expandX().pad(5);

        nappitaulukko.row().height(5);

        nappitaulukko.add(luoLabel("Selviytyjän purjeet")).expandX().pad(10);
        nappitaulukko.add(luoLabel("Turvaverkko")).expandX().pad(10);
        nappitaulukko.add(luoLabel("Laiva -sovellus")).expandX().pad(10);

        rootTable.add(nappitaulukko).top().padBottom(sp.V_HEIGHT/4 * 3);

        rootTable.validate();
    }

    private Label luoLabel(String teksti) {
        Label label = new Label(teksti, skin, "launcher");
        label.setFontScale(0.15f);
        return label;
    }

    private void createButtons() {
        spButton = new Button(skin.get("spButtonStyle", Button.ButtonStyle.class));
        spButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(logtag, "aloitusbuttonia painettiin, aloitetaan Selviytyjän Purjeet");
                sp.init();
            }
        });

        ChangeListener alert = new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Skin skinDialog = new Skin(Gdx.files.internal("uiskin.json"));
                //TODO (optional) popupeille oma skini jos niitä käytetään muualla ohjelmassa
                Dialog d = new Dialog("En tee, hekkekkee", skinDialog);
                d.button("OK");

                //keskitetään dialogi ja skaalataan suuremmaksi
                d.align(Align.center);
                d.setOrigin(Align.center);
                d.setScale(2f);

                d.show(stage);
            }
        };

        mockButton1 = new Button(skin.get("unavailableButtonStyle", Button.ButtonStyle.class));
        mockButton2 = new Button(skin.get("unavailableButtonStyle", Button.ButtonStyle.class));

        mockButton1.addListener(alert);
        mockButton2.addListener(alert);
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
        stage.draw();

    }
}
