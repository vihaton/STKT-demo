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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;

import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;
import fi.ymcafinland.demo.logiikka.Solmu;

/**
 * Created by Sasu on 20.3.2016.
 */
public class HUD {
    public Stage stage;
    public InputMultiplexer im; //todo voisiko tehottomuus johtua multiplexeristä? Swaipit tuntuvat toimivan jouhevasti, mutta kun nappeja painetaan niin hommat menee välillä vituiks.

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
    protected boolean lapsia;
    protected PlayScreen playScreen;
    private Button.ButtonStyle styleParent;
    private Button.ButtonStyle styleLeft;
    private Button.ButtonStyle styleRight;
    private Button.ButtonStyle styleChild1;
    private Button.ButtonStyle styleChild2;
    private Button.ButtonStyle styleChild3;

    private Viewport viewport;
    private Table topTable;
    private Table midTable;
    private Table botTable;
    private float sidePad;
    private ArrayList<Button> midJaBotTablejenNapit;

    public HUD(final PlayScreen playScreen, SpriteBatch sb, Skin masterSkin, Solmu solmu) {

        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());

        //HUD EI implementoi inputProcessorin rajapintaa, vaan asettaa inputprocessoriksi tuntemansa inputmultiplexerin.
        this.stage = new Stage(viewport, sb);
        this.im = new InputMultiplexer(this.stage, new GestureDetector(new HUDListener(this, viewport, sb)));

        skin = masterSkin;
        this.solmu = solmu;
        this.playScreen = playScreen;

        hasParent = solmu.getMutsi() != null;
        lapsia = solmu.getLapset().size() > 1;
        sidePad = 10;

        buttonCreation();
        updateButtons(solmu);
        createTables();
        updateTables();
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
        if (lapsia) {
            final ArrayList<Solmu> laps = solmu.getLapset();
            child1.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.setSolmu(laps.get(0));
                }
            });

            child2.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.setSolmu(laps.get(1));

                }
            });

            child3.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.setSolmu(laps.get(2));
                }
            });
        } else {
            kysymys.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    playScreen.getSp().setQuestionScreen(solmu);
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

        this.solmu = solmu;
        hasParent = solmu.getMutsi() != null;
        lapsia = solmu.getLapset().size() > 0;
        updateButtons(solmu);
        updateTables();

        karttaNappi.setChecked(zoomedOut);
        createListeners(playScreen, solmu);
        setZoomedHUDState(zoomedOut);
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
        parent.setVisible(hasParent);
        parent.setDisabled(!hasParent);

        karttaNappi.setChecked(playScreen.zoomedOut);   //jos ollaan zoomattu ulos, karttanappi on painettuna

        styleLeft.up = skin.getDrawable(solmu.getVasenSisarus().getMinikuvanNimi());
        styleRight.up = skin.getDrawable(solmu.getOikeaSisarus().getMinikuvanNimi());

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
        topTable.top().left().add(palaute);
        topTable.add(parent).expandX();
        topTable.right().add(karttaNappi);

        botTable.clearChildren();
        if (lapsia) {
            botTable.bottom().add(child1);
            botTable.add(child2).expandX();
            botTable.add(child3);
        } else {
            botTable.add(kysymys);
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
        topTable.pad(sidePad);

        midTable = new Table();
        midTable.setFillParent(true);
        midTable.pad(sidePad);
        midTable.add(leftSister);
        midTable.add(new Actor()).expandX();
        midTable.add(rightSister);

        botTable = new Table();
        botTable.setFillParent(true);
        botTable.pad(sidePad);

        stage.addActor(topTable);
        stage.addActor(midTable);
        stage.addActor(botTable);
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
        karttaNappi.align(Align.right);

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

        midJaBotTablejenNapit = new ArrayList<>();
        Collections.addAll(midJaBotTablejenNapit, new Button[]{leftSister, rightSister, child1, child2, child3, kysymys});
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
        if (lapsia) {
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
