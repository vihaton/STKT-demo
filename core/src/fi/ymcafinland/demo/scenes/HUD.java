package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public InputMultiplexer im;

    protected Solmu solmu;
    protected Skin skin;
    protected Button karttaNappi;
    protected Button parent;
    protected boolean hasParent;
    protected Button leftSister;
    protected Button rightSister;
    protected Button child1;
    protected Button child2;
    protected Button child3;
    protected Button kysymys;
    protected Button palaute;
    protected boolean montaLasta;
    protected Texture textureHahmo;
    protected PlayScreen playScreen;
    private Button.ButtonStyle styleParent;
    private Button.ButtonStyle styleLeft;
    private Button.ButtonStyle styleRight;
    private Button.ButtonStyle styleChild1;
    private Button.ButtonStyle styleChild2;
    private Button.ButtonStyle styleChild3;

    private Viewport viewport;

    //todo myös hudi masterSkinin piiriin muiden luokkien tapaan
    public HUD(final PlayScreen playScreen, SpriteBatch sb, Skin masterSkin, Solmu solmu) {

        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());

        //HUD EI implementoi inputProcessorin rajapintaa, vaan asettaa inputprocessoriksi tuntemansa inputmultiplexerin.
        this.stage = new Stage(viewport, sb);
        this.im = new InputMultiplexer(this.stage, new GestureDetector(new HUDListener(this, viewport, sb)));

        skin = masterSkin;
        this.solmu = solmu;
        this.playScreen = playScreen;

        hasParent = solmu.getMutsi() != null;
        montaLasta = solmu.getLapset().size() > 1;

        buttonCreation();
        updateButtons(solmu);
        createTable();
        createListeners(playScreen, solmu);
    }

    /**
     * Tapahtumankuuntelijat nappuloille
     *
     * @param playScreen
     * @param solmu
     */
    private void createListeners(final PlayScreen playScreen, final Solmu solmu) {

        if (hasParent) {
            parent.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.setSolmu(solmu.getMutsi());
                }
            });
        }
        rightSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.setSolmu(solmu.getOikeaSisarus());
            }
        });
        leftSister.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.setSolmu(solmu.getVasenSisarus());
            }
        });
        if (montaLasta) {
            child1.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();
                    playScreen.setSolmu(laps.get(0));
                }
            });

            child2.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();

                    playScreen.setSolmu(laps.get(1));

                }
            });

            child3.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ArrayList<Solmu> laps = solmu.getLapset();
                    playScreen.setSolmu(laps.get(2));
                }
            });
        }

        karttaNappi.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!karttaNappi.isChecked()) {
                    playScreen.zoom(true);
                } else {
                    playScreen.zoom(false);
                }
            }
        });

        kysymys.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.getSp().setQuestionScreen(solmu);
            }
        });

        palaute.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                playScreen.getSp().setPalauteScreen();
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
        if (karttaNappi.isChecked()) {
            zoomedOut = true;
        }

        stage.clear();
        this.solmu = solmu;
        hasParent = solmu.getMutsi() != null;
        montaLasta = solmu.getLapset().size() > 0;
        buttonCreation();
        updateButtons(solmu);
        createTable();
        karttaNappi.setChecked(zoomedOut);
        createListeners(playScreen, solmu);
        setZoomedHUDState(zoomedOut);
    }

    private void setZoomedHUDState(boolean zoomedOut) {
        if (hasParent) {
            parent.setVisible(!zoomedOut);
            parent.setDisabled(zoomedOut);
        }
        leftSister.setVisible(!zoomedOut);
        rightSister.setVisible(!zoomedOut);
        child1.setVisible(!zoomedOut);
        child2.setVisible(!zoomedOut);
        child3.setVisible(!zoomedOut);
        kysymys.setVisible(!zoomedOut);
        leftSister.setDisabled(zoomedOut);
        rightSister.setDisabled(zoomedOut);
        child1.setDisabled(zoomedOut);
        child2.setDisabled(zoomedOut);
        child3.setDisabled(zoomedOut);
        kysymys.setDisabled(zoomedOut);
    }


    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(im);
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
        Table tableTop3 = new Table();
        tableTop3.setFillParent(true);
        tableTop3.top();
        if (hasParent) {
            tableTop2.top().add(parent);
        }
        tableTop.right().add(karttaNappi);
        tableTop3.left().add(palaute);

        stage.addActor(tableTop);
        stage.addActor(tableTop2);
        stage.addActor(tableTop3);

        Table tableMid = new Table();
        tableMid.center();
        tableMid.setFillParent(true);
        tableMid.add(leftSister).expand().center().left();
        tableMid.add(rightSister).expand().center().right();

        stage.addActor(tableMid);


        Table tableBot = new Table();
        tableBot.bottom();
        tableBot.setFillParent(true);
        if (montaLasta) {
            tableBot.add(child1).expand().bottom().left().padBottom(2);
            tableBot.add(child2).expand().bottom().padBottom(2);
            tableBot.add(child3).expand().bottom().right().padBottom(2);
        } else {
            tableBot.add(kysymys).expand().bottom().padBottom(2);
        }
        stage.addActor(tableBot);
    }

    /**
     * Luo nappulat HUDiin
     */
    private void buttonCreation() {
        styleParent = skin.get("styleParent", Button.ButtonStyle.class);
        styleLeft = skin.get("styleLeft", Button.ButtonStyle.class);
        styleRight = skin.get("styleRight", Button.ButtonStyle.class);
        styleChild1 = skin.get("styleChild1", Button.ButtonStyle.class);
        styleChild2 = skin.get("styleChild2", Button.ButtonStyle.class);
        styleChild3 = skin.get("styleChild3", Button.ButtonStyle.class);

        float scale = 1.2f;

        karttaNappi = new Button(skin.get("styleKartta", Button.ButtonStyle.class));
        karttaNappi.setScale(scale);

        kysymys = new Button(skin.get("styleKysymys", Button.ButtonStyle.class));
        kysymys.setScale(scale);

        palaute = new Button(skin.get("stylePalaute", Button.ButtonStyle.class));
        palaute.setScale(scale);

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
    }

    /**
     * Päivittää nappuloiden kuvat vastaamaan tämänhetkistä tilannetta
     *
     * @param solmu
     */
    private void updateButtons(Solmu solmu) {
        if (hasParent) {
            styleParent.up = skin.getDrawable(solmu.getMutsi().getMinikuvanNimi());
        }

        styleLeft.up = skin.getDrawable(solmu.getVasenSisarus().getMinikuvanNimi());
        styleRight.up = skin.getDrawable(solmu.getOikeaSisarus().getMinikuvanNimi());

        ArrayList<Solmu> lapset = solmu.getLapset();

        if (montaLasta) {
            if (kysymys != null) {
                kysymys.setVisible(false);
                kysymys.setDisabled(true);
            }
            styleChild1.up = skin.getDrawable(lapset.get(0).getMinikuvanNimi());
            styleChild2.up = skin.getDrawable(lapset.get(1).getMinikuvanNimi());
            styleChild3.up = skin.getDrawable(lapset.get(2).getMinikuvanNimi());

        } else {
            child1.setVisible(false);
            child2.setVisible(false);
            child3.setVisible(false);
            child1.setDisabled(true);
            child2.setDisabled(true);
            child3.setDisabled(true);
        }
    }


    public void right() {
        Gdx.app.log("HList", "Swaipattu oikealle");
        playScreen.setSolmu(solmu.getVasenSisarus());
    }

    public void left() {
        Gdx.app.log("HList", "Swaipattu vasemmalle");
        playScreen.setSolmu(solmu.getOikeaSisarus());
    }

    public void down() {
        Gdx.app.log("Hlist", "Swaipattu alas");
        if (hasParent) {
            playScreen.setSolmu(solmu.getMutsi());
        }
    }

    public void up() {
        Gdx.app.log("HList", "Swaipattu ylös");
        if (montaLasta) {
            playScreen.setSolmu(solmu.getLapset().get(1));
        } else if (kysymys.isVisible()) {
            playScreen.getSp().setQuestionScreen(solmu);
        }
    }

    public void siirryLahinpaanPalloon(float x, float y) {
        playScreen.siirryLahinpaanSolmuun(x, y);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
