package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.piirtajat.VaittamanPiirtaja;


/**
 * Created by jwinter on 29.3.2016.
 * <p/>
 * QuestionScreen luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class QuestionScreen implements Screen {
    protected SpriteBatch batch;

    private FitViewport viewport;
    private OrthographicCamera camera;
    private final Pelaaja pelaaja;
    private final Vaittamat vaittamat;
    private ArrayList<Vaittama> solmunVaittamat;
    private String solmunID;
    private Stage stage;
    private VaittamanPiirtaja vaittamanPiirtaja;
    Solmu solmu;
    private Button exitButton;
    private Texture texture;
    private Table rootTable;
    private Skin skin;

    public QuestionScreen(final SelviytyjanPurjeet sp, Pelaaja pelaaja, Vaittamat vaittamat, Skin masterSkin) {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
        this.pelaaja = pelaaja;
        this.vaittamat = vaittamat;
        this.skin = masterSkin;

        createRootTable(createExitButton(sp));

        this.stage = new Stage(viewport);
        stage.addActor(rootTable);

        this.vaittamanPiirtaja = new VaittamanPiirtaja(stage, masterSkin);

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);
        Gdx.app.log("QS", "QS konstruktori on valmis");
    }

    private Table createExitButton(final SelviytyjanPurjeet sp) {
        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        texture = new Texture("ruksi.png");

        styleExit.up = new TextureRegionDrawable(new TextureRegion(texture));
        exitButton = new Button(styleExit);
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("QS", "exitbuttonia painettiin");
                sendData();
                sp.resetPlayScreen();
            }
        });

        Table exitTable = new Table();
        exitTable.setSize(texture.getWidth(), texture.getHeight());
        exitTable.add(exitButton);
        exitTable.setTouchable(Touchable.childrenOnly);
        return exitTable;
    }

    private void createRootTable(Table exitTable) {
        rootTable = new Table();
        rootTable.setFillParent(true);

        Label otsikko = new Label("Questionscreen", skin, "otsikko");
        rootTable.top().add(otsikko);
        rootTable.right().add(exitTable);

        //Ratkaisevan tärkeä rivi!
        rootTable.validate();
    }

    /**
     * sendData lähettää saadun datan eteenpäin. sendData konfirmoi kysymykseen laitetun tiedon, ja
     * kutsuu tiedon lähettämisen jälkeen dispose -metodia.
     */
    public void sendData() {
        //todo väittämän vastaus vaikuttaa palautteeseen vain kerran (sulku-avaus-bugi)
        //todo pelaaja.lisaaVastaus() vain kerran
        float kerroin = 1f;
        for (Vaittama v : solmunVaittamat) {

            pelaaja.lisaaVastaus();
            kerroin *= v.getArvo();


        }
        int solmunID = Integer.parseInt(this.solmunID);

        if (solmunID < 10) {
            pelaaja.setFyysinen(pelaaja.getFyysinen() * kerroin);
        } else if (solmunID < 13) {
            pelaaja.setAlyllinen(pelaaja.getAlyllinen() * kerroin);
        } else if (solmunID < 16) {
            pelaaja.setEettinen(pelaaja.getEettinen() * kerroin);
        } else if (solmunID < 19) {
            pelaaja.setTunteellinen(pelaaja.getTunteellinen() * kerroin);
        } else if (solmunID < 22) {
            pelaaja.setSosiaalinen(pelaaja.getSosiaalinen() * kerroin);
        } else {
            pelaaja.setLuova(pelaaja.getLuova() * kerroin);
        }
    }

    @Override
    public void show() {
        Gdx.app.log("QS", "QuestionScreenin show() -metodia kutsuttiin");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.setDebugAll(true);

        vaittamanPiirtaja.renderoi(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    public void setSolmu(Solmu solmu) {
        this.solmu = solmu;
        solmunID = solmu.getID();
        solmunVaittamat = vaittamat.getYhdenSolmunVaittamat(solmunID);
        vaittamanPiirtaja.paivitaVaittamat(solmunVaittamat);
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