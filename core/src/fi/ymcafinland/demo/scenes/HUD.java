package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    Button kysymys;
    protected boolean montaLasta;

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

            child2.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();

                        screen.setSolmu(laps.get(1));

                }
            });

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

        kysymys.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.getSp().setQuestionScreen(solmu);
            }
        });

    }

    /**
     * Päivittää HUDin tiedot
     *
     * @param solmu
     */
    public void update(Solmu solmu) {
        boolean zoomedOut = false;
        if(karttaNappi.isChecked()){
             zoomedOut = true;
        }
        stage.clear();
        this.solmu = solmu;
        hasParent = solmu.getMutsi() != null;
        montaLasta = solmu.getLapset().size() > 0;
        buttonCreation(solmu);
        createTable();
        karttaNappi.setChecked(zoomedOut);
        createListeners(screen, solmu);

    }

    /**
     * Layout hudille
     */
    private void createTable() {
        Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);
        Table tableTop2 = new Table();
        tableTop2.setFillParent(true);
        if(hasParent) {
            tableTop2.top().add(parent);
        }
        tableTop.right().add(karttaNappi);

        stage.addActor(tableTop);
        stage.addActor(tableTop2);

        Table tableMid = new Table();
        tableMid.center();
        tableMid.setFillParent(true);
        tableMid.add(leftSister).expand().center().left();
        tableMid.add(rightSister).expand().center().right();

        stage.addActor(tableMid);


        Table tableBot = new Table();
        tableBot.bottom();
        tableBot.setFillParent(true);
        if(montaLasta) {
            tableBot.add(child1).expand().bottom().left().padBottom(2);
            tableBot.add(child2).expand().bottom().padBottom(2);
            tableBot.add(child3).expand().bottom().right().padBottom(2);
        }
        else {
            tableBot.add(kysymys).expand().bottom().padBottom(2);
        }
        stage.addActor(tableBot);
    }

    /**
     * Luo nappulat HUDiin
     *
     * @param solmu
     */
    private void buttonCreation(Solmu solmu) {
        Button.ButtonStyle styleParent = new Button.ButtonStyle();
        Button.ButtonStyle styleLeft = new Button.ButtonStyle();
        Button.ButtonStyle styleRight = new Button.ButtonStyle();
        Button.ButtonStyle styleChild1 = new Button.ButtonStyle();
        Button.ButtonStyle styleChild2 = new Button.ButtonStyle();
        Button.ButtonStyle styleChild3 = new Button.ButtonStyle();
        Button.ButtonStyle styleKartta = new Button.ButtonStyle();

        styleKartta.up = skin.getDrawable("mini_karttakuva");
        karttaNappi = new Button(styleKartta);
        if (hasParent) {
            styleParent.up = skin.getDrawable(solmu.getMutsi().getMinikuvanNimi());
            parent = new Button(styleParent);
        }
        styleLeft.up = skin.getDrawable(solmu.getVasenSisarus().getMinikuvanNimi());
        leftSister = new Button(styleLeft);
        styleRight.up = skin.getDrawable(solmu.getOikeaSisarus().getMinikuvanNimi());
        rightSister = new Button(styleRight);

        ArrayList<Solmu> lapset = solmu.getLapset();



        if (montaLasta) {
            if(kysymys != null) {
                kysymys.setVisible(false);
                kysymys.setDisabled(true);
            }
            styleChild1.up = skin.getDrawable(lapset.get(0).getMinikuvanNimi());
            child1 = new Button(styleChild1);
            styleChild2.up = skin.getDrawable(lapset.get(1).getMinikuvanNimi());
            child2 = new Button(styleChild2);
            styleChild3.up = skin.getDrawable(lapset.get(2).getMinikuvanNimi());
            child3 = new Button(styleChild2);
        } else{

            child1.setVisible(false);
            child2.setVisible(false);
            child3.setVisible(false);
            child1.setDisabled(true);
            child2.setDisabled(true);
            child3.setDisabled(true);

        }
        Button.ButtonStyle styleKysymys = new Button.ButtonStyle();
        styleKysymys.up = skin.getDrawable("mini_kysymys");
        kysymys = new Button(styleKysymys);
    }



}
