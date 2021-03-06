package fi.ymcafinland.demo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.main.MasterSkin;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 11.4.2016.
 */
public class InfoScreen extends PohjaScreen {

    protected SpriteBatch batch;
    protected Label otsikko;
    private Texture tausta;
    private ScrollPane pane;
    private Table nappiTaulukko;
    private Button jatkaButton;
    private Button alkuButton;

    private static final String infoText =
            "Selvitä millainen selviytyjä olet!\n" +
                    "\n" +
                    "Ihmiset voidaan jakaa kuuteen selviytyjätyyppiin: fyysinen, älyllinen, " +
                    "eettinen, tunteellinen, sosiaalinen ja luova selviytyjä. " +
                    "Nämä kuusi kategoriaa esiintyvät yksilöissä eri suhteissa, eli yksilöllä voi " +
                    "esimerkiksi olla vahvat fyysiset sekä sosiaaliset selviytymistaidot.\n" +
                    "\n" +
                    "Tässä harjoitteessa selviytyjätyypit on jaettu konkreettisempiin selviytymistapoihin " +
                    "(Fyysinen selviytyjä -> Lepään, Syön, Liikun). Selvitä millainen selviytyjä olet vastaamalla " +
                    "väittämiin, jotka on laitettu jokaisen selviytymistavan alle.\n" +
                    "\n" +
                    "Aloita jatkamalla tarkastelemaan materiaalia, väittämät löytyvät ympyrän ulkokehältä.\n" +
                    "\n" +
                    "ps. selviytymisprofiilisi yhteenvedon löydät materiaalin vasemman yläkulman menusta ;)";


//            "Tähän näkymään tulevat Selviytyjän purjeiden ohjeet: miten ja miksi sitä tehdään.\n\n" +
//                    "Tämä demo (v0.2) on kehitysvaiheessa, eikä välttämättä mikään ole vielä lopullista. " +
//                    "Erityisesti grafiikat tulevat muuttumaan vielä (moneen) kertaan. Tärkeimpänä kehityksessä " +
//                    "pidetään 1) toiminnallisuuksien implementointia, 2) minimikäytettävyyden saavuttamista ja " +
//                    "3) grafiikan ja intuitiivisen käytettävyyden saavuttamista.\n\n" +
//                    "Tätä sovellusta ovat devanneet Vili Hätönen, Sasu Mäkinen ja Jouni Winter. Materiaalia ovat " +
//                    "ensisijaisesti olleet kehittämässä Vili Hätönen, Olli Laukkanen, Tiina Saari ja Sasu Mäkinen.";


    public InfoScreen(SelviytyjanPurjeet sp, Skin masterSkin) {
        super(masterSkin, "IS");
        this.batch = new SpriteBatch();

        this.tausta = skin.get("infonTausta", Texture.class);
        taytaRootTable(sp);
    }

    public void taytaRootTable(SelviytyjanPurjeet sp) {
        otsikko = new Label("Selviytyjän purjeet", skin, "otsikko");
        rootTable.add(otsikko).expand().top();
        rootTable.row();

        luoScrollPane();

        rootTable.add(pane).pad(SelviytyjanPurjeet.V_WIDTH / 10).minSize(SelviytyjanPurjeet.V_WIDTH * 0.8f, SelviytyjanPurjeet.V_HEIGHT * 0.5f);

        rootTable.row();

        createJatkaButton(sp);
        createAlkuTestiButton(sp);

        nappiTaulukko = new Table();
        nappiTaulukko.add(alkuButton).expandX();

        rootTable.add(nappiTaulukko).padBottom(64).fillX();
        rootTable.validate();
    }

    private void luoScrollPane() {
        pane = new ScrollPane(luoInfoteksti(), skin.get("scrollPaneKnob", ScrollPane.ScrollPaneStyle.class));
        pane.setFadeScrollBars(false);
        pane.validate();
    }

    private Label luoInfoteksti() {
        Label label = new Label(infoText, skin, "infoteksti");
        label.setWrap(true);
        label.setFontScale(MasterSkin.HIERO_FONT_SCALE);
        label.setAlignment(Align.center);

        return label;
    }

    private void createAlkuTestiButton(final SelviytyjanPurjeet sp) {
        alkuButton = new Button(skin.get("jatkaButtonStyle", Button.ButtonStyle.class));
        alkuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("IS", "alkutestibuttonia painettiin");
                sp.setQuestionScreen(new Solmu("0", null));
            }
        });
    }

    public void createJatkaButton(final SelviytyjanPurjeet sp) {
        jatkaButton = new Button(skin.get("jatkaButtonStyle", Button.ButtonStyle.class));
        jatkaButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("IS", "jatkabuttonia painettiin");
                sp.setPlayScreenMaxSelviytyjaan();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        float rgbJakaja = 255f;
        Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(tausta, 0, 0, tausta.getWidth(), tausta.getHeight());
        batch.end();

        pane.act(delta);
        stage.draw();
    }

    /**
     * Kun alkutesti on suoritettu kerran, vaihdetaan jatkamisnappulan toiminnoksi jatkaButton
     *
     */
    public void paivitaJatkaButton() {
        nappiTaulukko.clear();
        nappiTaulukko.add(jatkaButton).expandX();

        rootTable.add(nappiTaulukko).padBottom(64).fillX();
        rootTable.validate();
    }
}
