package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
 *
 * QuestionScreen luokalla käsitellään Selviytyjän purjeiden "kolmatta tasoa", ja sen kysymyksistä tulevaa
 * dataa.
 */
public class QuestionScreen implements Screen {
    protected SpriteBatch batch;

    private FitViewport viewport;
    private OrthographicCamera camera;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private BitmapFont fontti;
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
        this.fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false);
        this.pelaaja = pelaaja;
        this.vaittamat = vaittamat;
        this.skin = masterSkin;
        solmunID = "7";
        solmunVaittamat = vaittamat.getKarttaSolmujenVaittamista().get(solmunID);

        Table vaittamienTaulukko = stagenluonti(createExitButton(sp));
        this.vaittamanPiirtaja = new VaittamanPiirtaja(stage, vaittamienTaulukko, masterSkin);

        camera.setToOrtho(false, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT);
        Gdx.app.log("QS", "QS konstruktori on valmis");
    }

    /**
     * Palauttaa taulukon, joka annetaan väittämänpiirtäjälle, eli johon laitetaan kaikki väittämät slaidereineen
     * @param exitTable
     * @return Table vaittamienTaulukko
     */
    private Table stagenluonti(Table exitTable) {
        this.stage = new Stage(viewport);
        rootTable = new Table();
        rootTable.setFillParent(true);
        Label otsikko = new Label("Questionscreen", skin, "otsikko");
        rootTable.top().add(otsikko);
        rootTable.row();

        Table vaittamienTaulukko = new Table();
        rootTable.center().add(vaittamienTaulukko);

        stage.addActor(rootTable);
        stage.addActor(exitTable);

        return vaittamienTaulukko;
    }

    public Table createExitButton(final SelviytyjanPurjeet sp) {
        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        texture = new Texture("ruksi.png");

        styleExit.up = new TextureRegionDrawable(new TextureRegion(texture));
        exitButton = new Button(styleExit);
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                sendData();
                sp.resetPlayScreen();
            }
        });

        Table exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.top().right().add(exitButton);
        return exitTable;
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

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        glyphLayout.setText(skin.getFont("fontti"), solmu.getMutsi().getOtsikko());
        float x = (SelviytyjanPurjeet.V_WIDTH - glyphLayout.width) / 2;
        float y = (SelviytyjanPurjeet.V_HEIGHT - 2 * glyphLayout.height);

        batch.begin();
        fontti.draw(batch, glyphLayout, x, y);
        y -= glyphLayout.height;
        glyphLayout.setText(skin.getFont("fontti"), "väittämät");
        fontti.draw(batch, glyphLayout, (SelviytyjanPurjeet.V_WIDTH - glyphLayout.width) / 2, y);

        batch.end();

        vaittamanPiirtaja.renderoi(batch, delta);

        stage.draw();

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