package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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
        otsikko = new Label("S T K T", skin, "otsikko");
        rootTable.add(otsikko).expand().top();
        rootTable.row().height(20);

        createButtons();

        Table nappitaulukko = new Table();

        nappitaulukko.left().add(spButton).pad(5);
        nappitaulukko.center().add(mockButton1).pad(5);
        nappitaulukko.right().add(mockButton2).pad(5);

        nappitaulukko.row().height(5);

        nappitaulukko.left().add(new Label("Selvityjän Purjeet", skin, "launcher"));
        nappitaulukko.center().add(new Label("Turvaverkko", skin, "launcher"));
        nappitaulukko.right().add(new Label("Laiva -sovellus", skin, "launcher"));

        rootTable.add(nappitaulukko).top().padBottom(sp.V_HEIGHT/4 * 3);

        rootTable.validate();
    }

    private void createButtons() {
        spButton = new Button(skin.get("spButtonStyle", Button.ButtonStyle.class));
        spButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(logtag, "aloitusbuttonia painettiin, aloitetaan Selviytyjän Purjeet");
                sp.init();
                stage.dispose();
                dispose();
            }
        });

        mockButton1 = new Button(skin.get("unavailableButtonStyle", Button.ButtonStyle.class));
        mockButton2 = new Button(skin.get("unavailableButtonStyle", Button.ButtonStyle.class));

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
