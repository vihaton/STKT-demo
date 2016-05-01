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
    private Table exitTable;
    private Label otsikko;
    private Table otsikkoTable;
    private float sidePad;
    private Table rootTable;
    private Skin skin;

    public QuestionScreen(final SelviytyjanPurjeet sp, Pelaaja pelaaja, Vaittamat vaittamat, Skin masterSkin) {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, camera);
        this.pelaaja = pelaaja;
        this.vaittamat = vaittamat;
        this.skin = masterSkin;
        this.sidePad = 64;

        this.exitTable = createExitButton(sp);
        createRootTable();

        this.stage = new Stage(viewport);
        stage.addActor(rootTable);

        this.vaittamanPiirtaja = new VaittamanPiirtaja(stage, masterSkin);

        Gdx.app.log("QS", "QS konstruktori on valmis");
    }

    private Table createExitButton(final SelviytyjanPurjeet sp) {
        Button exitButton = new Button(skin.get("exitButtonStyle", Button.ButtonStyle.class));
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("QS", "exitbuttonia painettiin");
                sendData();
                sp.resetPlayScreen();
            }
        });

        Table exitTable = new Table();
        exitTable.add(exitButton).top().right();

        exitTable.validate();

        return exitTable;
    }

    private void createRootTable() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        otsikkoTable = luoOtsikko();

        rootTable.top().add(otsikkoTable).padTop(sidePad / 2).padLeft(sidePad);
        rootTable.add(exitTable).right().top();

        //Ratkaisevan tärkeä rivi!
        rootTable.validate();
    }

    private Table luoOtsikko() {
        Table ot = new Table();

        otsikko = new Label("Questionscreen on tässä, tämä on siis question screen eli väittämänäkymä, eli QS eli question screen.", skin, "otsikko");
        otsikko.setFillParent(true);
        otsikko.setFontScale(0.7f);
        otsikko.setWrap(true);
        otsikko.setAlignment(Align.center);

        ot.add(otsikko).width(SelviytyjanPurjeet.V_WIDTH - 2 * sidePad);
        ot.validate();

        return ot;
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

//        stage.setDebugAll(true);

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
        otsikko.setText(solmu.getMutsi().getOtsikko() + ":\n" + solmu.getSisalto());
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