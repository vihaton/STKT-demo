package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by Sasu on 2.5.2016.
 */
public class InfoButtonKasittelija {
    private Skin skin;
    private Stage stage;
    private ArrayList<Solmu> solmut;
    private ArrayList<Table> buttonTaulukot;




    public InfoButtonKasittelija(Stage stage, Skin masterSkin, Verkko verkko) {
        this.skin = masterSkin;
        this.stage = stage;
        buttonTaulukot = new ArrayList<>();
        luoListaEnsimmaisenTasonSolmuista(verkko);

        luoInfoNapit();
    }

    private void luoListaEnsimmaisenTasonSolmuista(Verkko verkko) {
        this.solmut = new ArrayList<>();
        for(Solmu s : verkko.getSolmut()){
            if(Integer.parseInt(s.getID()) < 7){
                solmut.add(s);
            }
        }
    }



    private void luoInfoNapit() {
        Button tyhjaButton = new Button(skin.get("transButtonStyle", Button.ButtonStyle.class));

        for (Solmu s : solmut) {
            Button infoButton = new Button(skin.get("infoButtonStyle", Button.ButtonStyle.class));
            luoKuuntelija(infoButton, s);

            Table buttonTable = new Table();

            buttonTable.add(infoButton).minSize(64);
            buttonTable.row();
            buttonTable.add(tyhjaButton).minSize(64);
            buttonTable.row();
            buttonTable.add(tyhjaButton).minSize(64);

            buttonTable.setPosition(s.getXKoordinaatti(), s.getYKoordinaatti());
            buttonTable.setOrigin(Align.center);
            buttonTable.setRotation(s.getKulma());


            buttonTaulukot.add(buttonTable);
            stage.addActor(buttonTable);
        }
    }

    private void luoKuuntelija(Button button, Solmu s){
        if(Integer.parseInt(s.getID()) == 1) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("http://www.ymca.fi");
                }
            });
        }
        if(Integer.parseInt(s.getID()) == 2) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("http://www.mielenterveysseura.fi/fi");
                }
            });
        }
        if(Integer.parseInt(s.getID()) == 3) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("https://www.ray.fi");
                }
            });
        }
        if(Integer.parseInt(s.getID()) == 4) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("http://niceme.me/");
                }
            });
        }
        if(Integer.parseInt(s.getID()) == 5) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("https://www.youtube.com/watch?v=9R8aSKwTEMg");
                }
            });
        }
        if(Integer.parseInt(s.getID()) == 6) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("IBK", "Info nappulaa painettu");

                    Gdx.net.openURI("https://www.riemurasia.net/kuva/Typera-jaatelo/164917");
                }
            });
        }

    }


    public void paivitaInfoButtonit(float delta, float angleToPoint){
        for(Table table : buttonTaulukot) {
            table.setTransform(true);
            table.setRotation(angleToPoint-90);
        }
    }


}



