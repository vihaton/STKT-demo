package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;

import fi.ymcafinland.demo.kasittelijat.KameranKasittelija;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.screens.InfoScreen;
import fi.ymcafinland.demo.screens.LauncherScreen;
import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.screens.QuestionScreen;
import fi.ymcafinland.demo.transitions.ScreenTransition;

/**
 * Created by Sasu on 20.3.2016.
 */
public class HUD {
    public Stage stage;
    public InputMultiplexer im;

    protected Skin skin;
    protected boolean hasParent;
    protected boolean lapsia;
    protected PlayScreen playScreen;
    protected Button karttaNappi;
    protected Button parent;
    protected Button leftSister;
    protected Button rightSister;
    protected Button child1;
    protected Button child2;
    protected Button child3;
    protected Button kysymys;
    protected TextButton palaute;
    protected TextButton info;
    protected TextButton quit;
    protected Button menu;
    private Button.ButtonStyle styleParent;
    private Button.ButtonStyle styleLeft;
    private Button.ButtonStyle styleRight;
    private Button.ButtonStyle styleChild1;
    private Button.ButtonStyle styleChild2;
    private Button.ButtonStyle styleChild3;

    private Viewport viewport;
    private Table menuTable;
    private Table topTable;
    private Table midTable;
    private Table botTable;
    private Table minimapTable;
    private float sidePad;
    private ArrayList<Button> midJaBotTablejenNapit;
    private ArrayList<Button> ylarivinNapit;
    protected Solmu solmu;
    private Solmu mutsi;
    private Solmu vasenSisko;
    private Solmu oikeaSisko;
    private Solmu lapsi1;
    private Solmu lapsi2;
    private Solmu lapsi3;
    private KameranKasittelija kameranKasittelija;
    private HUDListener hudListener;

    public HUD(final PlayScreen playScreen, SpriteBatch sb, Skin masterSkin, Solmu solmu) {
        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());
        kameranKasittelija = playScreen.getKameranKasittelija();

        //HUD EI implementoi inputProcessorin rajapintaa, vaan asettaa inputprocessoriksi tuntemansa inputmultiplexerin.
        this.stage = new Stage(viewport, sb);
        hudListener = new HUDListener(this, kameranKasittelija, 0);
        this.im = new InputMultiplexer(this.stage, new GestureDetector(hudListener), playScreen.stage);
//        playScreen.lisaaStage(stage); //lisää hudin stagen playscreenin tietoon näkymien vaihdoksia varten.

        skin = masterSkin;
        this.solmu = solmu;
        this.playScreen = playScreen;
        menuTable = new Table();
        hasParent = solmu.getMutsi() != null;
        lapsia = solmu.getLapset().size() > 1;
        sidePad = 10;

//        createButtons();
        createNuolet();
//        updateButtons(solmu);
        createTables();
//        updateTables();
        createListeners();
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Tapahtumankuuntelijat nappuloille
     */
    private void createListeners() {
//        luoLapsienKuuntelijat();
        mutsi = new Solmu("0", null);
        vasenSisko = new Solmu("0", null);
        oikeaSisko = new Solmu("0", null);

        parent.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.setSolmu(mutsi);
            }
        });

        rightSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.setSolmu(oikeaSisko);
            }
        });
        leftSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.setSolmu(vasenSisko);
            }
        });
        kysymys.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                siirryQuestionScreeniin(solmu);
            }
        });


        karttaNappi.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!karttaNappi.isChecked()) {
                    playScreen.nappulaZoom(true);
                } else {
                    playScreen.nappulaZoom(false);
                }
            }
        });
        menu.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menuTable = new Table();
                menuTable.setBounds(topTable.getX(), topTable.getY() + (0.8f * SelviytyjanPurjeet.V_HEIGHT), 200, 200);
                menuTable.add(palaute).minSize(200, 66);
                menuTable.row();
                menuTable.add(info).minSize(200, 66);
                menuTable.row();
                menuTable.add(quit).minSize(200, 66);
                menuTable.setBackground(skin.getDrawable("menutausta"));
                topTable.top().left().add(menuTable);

                stage.addActor(menuTable);

            }
        });
        palaute.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.getSp().setPalauteScreen();
            }
        });
        info.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                siirryInfoScreeniin();
            }
        });
        quit.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                siirryAlkuScreeniin();
            }
        });

    }

    public void siirryQuestionScreeniin(Solmu solmu) {
        QuestionScreen qs = playScreen.getSp().getQuestionScreen();
        qs.setSolmu(solmu);
        ScreenTransition st = new ScreenTransition(playScreen, qs, 0.5f);

        if (solmu.getID().equals("13")) {
            if (SelviytyjanPurjeet.LOG)
                Gdx.app.log("HUD", "kutsutaan ST.wildFadeTransitionia playScreenistä quostionScreeniin");
            st.wildFadeTransition();
        } else {
            if (SelviytyjanPurjeet.LOG)
                Gdx.app.log("HUD", "kutsutaan ST.fadeTransitionia playScreenistä quostionScreeniin");
            st.fadeTransition();
        }
    }
    public void siirryInfoScreeniin() {
        InfoScreen is = playScreen.getSp().getInfoScreen();
        ScreenTransition st = new ScreenTransition(playScreen, is, 0.5f);
        st.fadeTransition();
    }
    public void siirryAlkuScreeniin() {
        LauncherScreen as = playScreen.getSp().getAlkuScreen();
        ScreenTransition st = new ScreenTransition(playScreen, as, 0.5f);
        st.fadeTransition();
    }


    /**
     * Päivittää HUDin tiedot
     *
     * @param solmu
     */
    public void update(Solmu solmu, boolean zoomedOut) {
        hasParent = solmu.getMutsi() != null;
        lapsia = solmu.getLapset().size() > 0;

        menuTable.remove();


//        updateButtons(solmu);
        updateListeners(solmu);

        karttaNappi.setChecked(zoomedOut);
        setZoomedHUDState(zoomedOut);
    }

    private void updateListeners(Solmu solmu) {
        this.solmu = solmu;
        mutsi = solmu.getMutsi();
        vasenSisko = solmu.getVasenSisarus();
        oikeaSisko = solmu.getOikeaSisarus();
        ArrayList<Solmu> lapset = solmu.getLapset();

        if (lapsia) {
            lapsi1 = lapset.get(0);
            lapsi2 = lapset.get(1);
            lapsi3 = lapset.get(2);
        }

    }

    /**
     * Päivittää nappuloiden kuvat vastaamaan tämänhetkistä tilannetta
     *
     * @param solmu
     */
    private void updateButtons(Solmu solmu) {
        //Tilapäislisäehto, muutetaan kun hudi laitetaan toimimaan nollasolmun kanssa
        if (hasParent && !solmu.getMutsi().getID().equals("0")) {
            styleParent.up = skin.getDrawable(solmu.getMutsi().getMinikuvanNimi());
        }
        parent.setVisible(hasParent);
        parent.setDisabled(!hasParent);

        if (!solmu.getID().equals("0")) {
            styleLeft.up = skin.getDrawable(solmu.getVasenSisarus().getMinikuvanNimi());
            styleRight.up = skin.getDrawable(solmu.getOikeaSisarus().getMinikuvanNimi());
        }

        ArrayList<Solmu> lapset = solmu.getLapset();

        if (lapsia) {
            styleChild1.up = skin.getDrawable(lapset.get(0).getMinikuvanNimi());
            styleChild2.up = skin.getDrawable(lapset.get(1).getMinikuvanNimi());
            styleChild3.up = skin.getDrawable(lapset.get(2).getMinikuvanNimi());
        }
    }

    /**
     * päivittää taulukot ja napit siten, että oikeat asiat näkyvät
     */
    private void updateTables() {
        topTable.clearChildren();
        topTable.top().left().add(menu);
        topTable.add(parent).expandX();
        minimapTable.right().top().add(karttaNappi).size(230);

        botTable.clearChildren();
        if (lapsia) {
            botTable.bottom().add(child1);
            botTable.add(child2).expandX();
            botTable.add(child3);
        } else {
            botTable.bottom().add(kysymys);
        }
    }


    private void setZoomedHUDState(boolean zoomedOut) {
        if (hasParent) {
            parent.setVisible(!zoomedOut);
            parent.setDisabled(zoomedOut);
        }

        for (Button b : midJaBotTablejenNapit) {
            b.setVisible(!zoomedOut);
            b.setDisabled(zoomedOut);
        }
    }

    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(im);
    }

    /**
     * Layout hudille:
     * <p/>
     * topTablessa ylärivin napit, midTablessa sisarusnapit, botTablessa alarivi
     */
    private void createTables() {
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().left().add(menu);
        topTable.add(parent).expandX();
        minimapTable = new Table();
        minimapTable.setFillParent(true);
        minimapTable.right().top().add(karttaNappi).size(230);



        midTable = new Table();
        midTable.setFillParent(true);
        midTable.pad(sidePad);
        midTable.add(leftSister);
        midTable.add(new Actor()).expandX();
        midTable.add(rightSister);

        botTable = new Table();
        botTable.setFillParent(true);
        botTable.bottom().add(kysymys);
        botTable.pad(sidePad);

        stage.clear();
        stage.addActor(topTable);
        stage.addActor(minimapTable);
        stage.addActor(midTable);
        stage.addActor(botTable);
    }

    /**
     * Luo nappulat HUDiin
     */
    private void createNuolet() {
        float scale = 1.2f;

        parent = new Button(skin.get("styleNuoliYlos", Button.ButtonStyle.class));
        parent.setScale(scale);


        leftSister =  new Button(skin.get("styleNuoliVasen", Button.ButtonStyle.class));
        leftSister.setScale(scale);


        rightSister =  new Button(skin.get("styleNuoliOikea", Button.ButtonStyle.class));
        rightSister.setScale(scale);


        kysymys =  new Button(skin.get("styleNuoliAlas", Button.ButtonStyle.class));
        kysymys.setScale(scale);


        karttaNappi = new Button(skin.get("styleKartta", Button.ButtonStyle.class));
        karttaNappi.setScale(scale);
        karttaNappi.align(Align.right);

        palaute = new TextButton("Palaute",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        palaute.setScale(scale);

        info = new TextButton("Info",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        info.setScale(scale);

        quit = new TextButton("QUIT",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        quit.setScale(scale);

        menu = new Button(skin.get("styleMenu", Button.ButtonStyle.class));
        menu.setScale(scale);

        midJaBotTablejenNapit = new ArrayList<>();
        Collections.addAll(midJaBotTablejenNapit, leftSister, rightSister, kysymys);

        ylarivinNapit = new ArrayList<>();
        Collections.addAll(ylarivinNapit, karttaNappi, parent, menu);

    }
    private void createButtons() {
        styleParent = skin.get("styleParent", Button.ButtonStyle.class);
        styleLeft = skin.get("styleLeft", Button.ButtonStyle.class);
        styleRight = skin.get("styleRight", Button.ButtonStyle.class);
        styleChild1 = skin.get("styleChild1", Button.ButtonStyle.class);
        styleChild2 = skin.get("styleChild2", Button.ButtonStyle.class);
        styleChild3 = skin.get("styleChild3", Button.ButtonStyle.class);

        float scale = 1.2f;

        karttaNappi = new Button(skin.get("styleKartta", Button.ButtonStyle.class));
        karttaNappi.setScale(scale);
        karttaNappi.align(Align.right);

        kysymys = new Button(skin.get("vaittamaButtonStyle", Button.ButtonStyle.class));
        kysymys.setScale(scale);

        //ToDo oikea skin
        menu = new Button(skin.get("styleMenu", Button.ButtonStyle.class));
        menu.setScale(scale);


        palaute = new TextButton("Palaute",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        palaute.setScale(scale);

        info = new TextButton("Info",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        info.setScale(scale);

        quit = new TextButton("QUIT",skin.get("styleMenubar", TextButton.TextButtonStyle.class));
        quit.setScale(scale);


        parent = new Button(styleParent);
        parent.setScale(scale);

        leftSister = new Button(styleLeft);
        leftSister.setScale(scale);

        rightSister = new Button(styleRight);
        rightSister.setScale(scale);

        child1 = new Button(styleChild1);
        child1.setScale(scale);

        child2 = new Button(styleChild2);
        child2.setScale(scale);

        child3 = new Button(styleChild3);
        child3.setScale(scale);

        midJaBotTablejenNapit = new ArrayList<>();
        Collections.addAll(midJaBotTablejenNapit, leftSister, rightSister, child1, child2, child3, kysymys);

        ylarivinNapit = new ArrayList<>();
        Collections.addAll(ylarivinNapit, karttaNappi, parent, menu);
    }


    public void right() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HList", "Swaipattu oikealle");
        playScreen.paivitaPiste(playScreen.polttopiste, playScreen.panpiste);

        playScreen.setSolmu(solmu.getVasenSisarus());
    }

    public void left() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HList", "Swaipattu vasemmalle");
        playScreen.paivitaPiste(playScreen.polttopiste, playScreen.panpiste);

        playScreen.setSolmu(solmu.getOikeaSisarus());
    }

    public void down() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("Hlist", "Swaipattu alas");

        if (hasParent) {
            playScreen.paivitaPiste(playScreen.polttopiste, playScreen.panpiste);
            playScreen.setSolmu(solmu.getMutsi());
        }else{
            playScreen.setSolmu(solmu);
            playScreen.resetPan();
        }

    }

    public void up() {
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("HList", "Swaipattu ylös");

        if (lapsia) {

            playScreen.paivitaPiste(playScreen.polttopiste, playScreen.panpiste);

            playScreen.setSolmu(solmu.getLapset().get(1));
        } else if (kysymys.isVisible()) {
            if(solmu.getID().equals("0")){
                playScreen.setSolmu(solmu);
                playScreen.resetPan();
                return;
            }
            siirryQuestionScreeniin(solmu);
        }
    }

    public void siirryLahinpaanPalloon(float x, float y) {
        playScreen.siirryLahinpaanSolmuun(x, y);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    public void paivitaDelta(float delta){
        hudListener.paivitaDelta(delta);
    }
}
