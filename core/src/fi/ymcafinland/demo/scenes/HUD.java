package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.logiikka.Solmu;

/**
 * Created by Sasu on 20.3.2016.
 */
public class HUD {
    public Stage stage;

    protected Solmu solmu;

    protected OrthographicCamera camera;
    protected Skin skin;
    protected Sprite map;
    protected Button karttaNappi;
    protected Button parent;
    protected boolean hasParent;
    protected Button leftSister;
    protected Button rightSister;
    protected Button child1;
    protected Button child2;
    protected Button child3;
    protected boolean montaLasta;
    Skin karttaSkin;
    TextureAtlas atlas;


    PlayScreen screen;
    SpriteBatch sb;

    private Viewport viewport;


    public HUD(final PlayScreen screen, final Sprite map, SpriteBatch sb, final Solmu solmu) {

        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(viewport, sb);
        GestureDetector gd = new GestureDetector(new HUDListener (this, viewport, map, sb));
        InputMultiplexer im = new InputMultiplexer(gd, stage);
        Gdx.input.setInputProcessor(im);
        atlas = new TextureAtlas(Gdx.files.internal("minisolmut/minisolmut.pack"));
        skin = new Skin();
        skin.addRegions(atlas);

        this.map = map;
        this.solmu = solmu;
        this.screen = screen;
        this.sb = sb;
        hasParent = solmu.getMutsi() != null;
        montaLasta = solmu.getLapset().size() > 1;


        skinAndStyleCreation();
        buttonCreation(solmu);
        createTable();
        createListeners(screen, solmu);


    }

    /**
     * Tapahtumankuuntelijat nappuloille
     *
     * @param screen
     * @param solmu
     */
    private void createListeners(final PlayScreen screen, final Solmu solmu) {
        //ToDo Copypastat vittuun ja child 1 2 3 entä jos erimäärä lapsia?

        if (hasParent) {
            parent.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    screen.setSolmu(solmu.getMutsi());
                }
            });
        }
        //INVERSED LEFT AND RIGHT
        rightSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.setSolmu(solmu.getVasenSisarus());
            }
        });
        leftSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.setSolmu(solmu.getOikeaSisarus());
            }
        });
        if(montaLasta) {
            child1.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();
                    screen.setSolmu(laps.get(0));
                }
            });
        }
        child2.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                ArrayList<Solmu> laps = solmu.getLapset();
                if (!montaLasta) {
                    screen.setSolmu(laps.get(0));
                } else {
                    screen.setSolmu(laps.get(1));
                }
            }
        });
        if(montaLasta) {
            child3.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();
                    screen.setSolmu(laps.get(2));
                }
            });
        }


        karttaNappi.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!karttaNappi.isChecked()) {
                    screen.zoom(true);
                } else {
                    screen.zoom(false);

                }
            }
        });

    }

    /**
     * Päivittää HUDin tiedot
     *
     * @param solmu
     */
    public void update(Solmu solmu) {
        stage.clear();
        this.solmu = solmu;
        hasParent = solmu.getMutsi() != null;
        montaLasta = solmu.getLapset().size() > 1;
        buttonCreation(solmu);
        createTable();
        createListeners(screen, solmu);
    }

    /**
     * Layout hudille
     */
    private void createTable() {
        Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);
        tableTop.add(parent).top().padTop(5).padLeft(50);
        tableTop.add(karttaNappi).top().right();

        stage.addActor(tableTop);

        Table tableMid = new Table();
        tableMid.center();
        tableMid.setFillParent(true);
        tableMid.add(leftSister).expand().center().left();
        tableMid.add(rightSister).expand().center().right();

        stage.addActor(tableMid);


        Table tableBot = new Table();
        tableBot.bottom();
        tableBot.setFillParent(true);
        tableBot.add(child1).expand().bottom().left().padBottom(2);
        tableBot.add(child2).expand().bottom().padBottom(2);
        tableBot.add(child3).expand().bottom().right().padBottom(2);
        stage.addActor(tableBot);
    }

    /**
     * Luo nappulat HUDiin
     *
     * @param solmu
     */
    private void buttonCreation(Solmu solmu) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        karttaNappi = new TextButton("Kartta", karttaSkin);
        if (hasParent) {
            style.up = skin.getDrawable(solmu.getMutsi().getMiniKuva());
            parent = new Button(style);
        }
        style.up = skin.getDrawable(solmu.getVasenSisarus().getMiniKuva());
        leftSister = new Button(style);
        style.up = skin.getDrawable(solmu.getOikeaSisarus().getMiniKuva());
        rightSister = new Button(style);

        ArrayList<Solmu> lapset = solmu.getLapset();

        //ToDo mitä jos eri määrä lapsia?
        //tiedetään, että lapsia on vain yksi -V

        if (montaLasta) {
            style.up = skin.getDrawable(lapset.get(0).getMiniKuva());
            child1 = new Button(style);
            style.up = skin.getDrawable(lapset.get(1).getMiniKuva());
            child2 = new Button(style);
            style.up = skin.getDrawable(lapset.get(2).getMiniKuva());
            child3 = new Button(style);
        } else {
            if(!lapset.isEmpty()) {
                style.up = skin.getDrawable(lapset.get(0).getMiniKuva());
                child2 = new Button(style);

            }
        }
    }

    /**
     * Grafiikkaa nappuloille
     */
    private void skinAndStyleCreation() {

    karttaSkin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        karttaSkin.add("white", new Texture(pixmap));

        karttaSkin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = karttaSkin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = karttaSkin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = karttaSkin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = karttaSkin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = karttaSkin.getFont("default");
        karttaSkin.add("default", textButtonStyle);
    }
}
