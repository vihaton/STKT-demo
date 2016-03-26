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

import fi.ymcafinland.demo.SelviytyjanPurjeet;
import logiikka.Solmu;

/**
 * Created by Sasu on 20.3.2016.
 */
public class HUD {
    public Stage stage;
    private Viewport viewport;
    TextButton karttaNappi;
    Skin skin;
    TextButton parent;
    TextButton leftSister;
    TextButton rightSister;
    TextButton child1;
    TextButton child2;
    TextButton child3;
    OrthographicCamera camera;
    Solmu s1;
    Solmu s2;
    Solmu s3;
    Solmu s4;
    Solmu s5;
    Solmu s6;
    Solmu s7;



    public HUD(final SelviytyjanPurjeet sp, SpriteBatch sb, Solmu solmu){
        viewport = new FitViewport(SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        s1 = new Solmu(1,null);
        s2 = new Solmu(2,s1);
        s3 = new Solmu(3,s1);
        s4 = new Solmu(4,s1);
        s5 = new Solmu(5,s2);
        s6 = new Solmu(6,s2);
        s7 = new Solmu(7,s2);


        s1.setOtsikko("Kappasolmu");
        s1.setSijainti(0, 0);
        s2.setOtsikko("Disaster");
        s2.setVasenSisarus(s3);
        s2.setOikeaSisarus(s4);
        ArrayList<Solmu> testiS = new ArrayList<Solmu>();
        testiS.add(s5);
        testiS.add(s6);
        testiS.add(s7);
        s2.setLapset(testiS);

        skinAndStyleCreation();



        buttonCreation(s2);

        createTable();



        parent.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //ToDo Nothing happens here. Renderer needed?

                //camera.position.set(s2.getMutsi().getXKoordinaatti(), s2.getMutsi().getYKoordinaatti(), 0f);
                //camera.update();
                sp.setSolmu(s2.getMutsi());
            }
        });

        //ToDo Tee napille karttatoiminta. ZOom Out And Shiz.
        karttaNappi.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Clicked! Is checked: " + karttaNappi.isChecked());
                karttaNappi.setText("Good job!");
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
