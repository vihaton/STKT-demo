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
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    protected TextButton karttaNappi;
    protected TextButton parent;
    protected boolean hasParent;
    protected TextButton leftSister;
    protected TextButton rightSister;
    protected TextButton child1;
    protected TextButton child2;
    protected TextButton child3;
    protected boolean montaLasta;

    PlayScreen screen;
    SpriteBatch sb;

    private Viewport viewport;


    public HUD(final PlayScreen screen, final Sprite map, SpriteBatch sb, final Solmu solmu) {

        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(viewport, sb);
        GestureDetector gd = new GestureDetector(new HUDListener (this, viewport, map, sb));
        InputMultiplexer im = new InputMultiplexer(gd, stage);
        Gdx.input.setInputProcessor(im);

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
        rightSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.setSolmu(solmu.getOikeaSisarus());
            }
        });
        leftSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.setSolmu(solmu.getVasenSisarus());
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
        karttaNappi = new TextButton("Kartta", skin);
        if (hasParent) {
            parent = new TextButton(solmu.getMutsi().getOtsikko(), skin);
        }
        leftSister = new TextButton(solmu.getVasenSisarus().getOtsikko(), skin);
        rightSister = new TextButton(solmu.getOikeaSisarus().getOtsikko(), skin);

        ArrayList<Solmu> lapset = solmu.getLapset();

        //ToDo mitä jos eri määrä lapsia?
        //tiedetään, että lapsia on vain yksi -V

        if (montaLasta) {
            child1 = new TextButton(lapset.get(0).getOtsikko(), skin);
            child2 = new TextButton(lapset.get(1).getOtsikko(), skin);
            child3 = new TextButton(lapset.get(2).getOtsikko(), skin);
        } else {
            if(!lapset.isEmpty()) {

                child2 = new TextButton(lapset.get(0).getOtsikko(), skin);

            }
        }
    }

    /**
     * Grafiikkaa nappuloille
     */
    private void skinAndStyleCreation() {
        skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }
}
