package fi.ymcafinland.demo.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.Screens.PlayScreen;
import fi.ymcafinland.demo.SelviytyjanPurjeet;
import logiikka.Solmu;

/**
 * Created by Sasu on 20.3.2016.
 */
public class HUD {
    public Stage stage;

    protected OrthographicCamera camera;
    protected Skin skin;
    protected TextButton karttaNappi;
    protected TextButton parent;
    protected TextButton leftSister;
    protected TextButton rightSister;
    protected TextButton child1;
    protected TextButton child2;
    protected TextButton child3;

    private Viewport viewport;


    public HUD(final PlayScreen screen, SpriteBatch sb, final Solmu solmu){
        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        skinAndStyleCreation();
        buttonCreation(solmu);
        createTable();


        //ToDo Copypastat vittuun ja child 1 2 3 entä jos erimäärä lapsia?
        parent.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                screen.setSolmu(solmu.getMutsi());
            }
        });
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


        karttaNappi.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if(!karttaNappi.isChecked()) {
                    screen.zoom(true);
                }
                else{
                    screen.zoom(false);
                }
            }
        });

    }


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

    private void buttonCreation(Solmu solmu) {
        karttaNappi = new TextButton("Kartta", skin);
        parent = new TextButton(solmu.getMutsi().getOtsikko(), skin);
        leftSister = new TextButton(solmu.getVasenSisarus().getOtsikko(), skin);
        rightSister = new TextButton(solmu.getOikeaSisarus().getOtsikko(), skin);

        ArrayList<Solmu> lapset = solmu.getLapset();

        //ToDo mitä jos eri määrä lapsia?

        if(lapset.size()== 3 ) {
            child1 = new TextButton(lapset.get(0).getOtsikko(), skin);
            child2 = new TextButton(lapset.get(1).getOtsikko(), skin);
            child3 = new TextButton(lapset.get(2).getOtsikko(), skin);
        }
    }

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
