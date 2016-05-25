package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Verkko;

/**
 * Created by Sasu on 2.5.2016.
 */
public class InfoButtonKasittelija {
    private Skin skin;
    private Stage stage;
    private ArrayList<Solmu> solmut;
    private ArrayList<Table> buttonTaulukot;
    private boolean zoomedOut;


    public InfoButtonKasittelija(Stage stage, Skin masterSkin, Verkko verkko) {
        this.skin = masterSkin;
        this.stage = stage;
        buttonTaulukot = new ArrayList<>();
        luoListaEnsimmaisenTasonSolmuista(verkko);

        luoInfoNapit();
    }

    private void luoListaEnsimmaisenTasonSolmuista(Verkko verkko) {
        this.solmut = new ArrayList<>();
        for (Solmu s : verkko.getSolmut()) {
            if (Integer.parseInt(s.getID()) < 7) {
                solmut.add(s);
            }
        }
    }


    private void luoInfoNapit() {
        for (Solmu s : solmut) {
            Button infoButton = new Button(skin.get("infoButtonStyle", Button.ButtonStyle.class));
            lisaaKuuntelija(infoButton, s);

            Table buttonTable = new Table();

            buttonTable.add(infoButton).maxSize(64);
            buttonTable.row();
            buttonTable.add(new Actor()).minSize(64, 150);

            buttonTable.setPosition(s.getXKoordinaatti(), s.getYKoordinaatti());
            buttonTable.setOrigin(Align.center);
            buttonTable.setRotation(s.getKulma());

            buttonTaulukot.add(buttonTable);
            stage.addActor(buttonTable);
        }
    }

    //todo bug: infonappulat ei toimi, luuleeko panoroinniks?
    private void lisaaKuuntelija(Button button, Solmu s) {

        //Switch case hoitaa vertailun, luoKuuntelija metodi luo kuuntelijan nappulalle halutun nettisivun kera
        switch (Integer.parseInt(s.getID())) {
            case 1:
                button.addListener(luoKuuntelija("http://www.ymca.fi"));
                break;
            case 2:
                button.addListener(luoKuuntelija("http://www.mielenterveysseura.fi/fi"));
                break;
            case 3:
                button.addListener(luoKuuntelija("https://www.ray.fi"));
                break;
            case 4:
                button.addListener(luoKuuntelija("http://www.cameraobscura.fi/"));
                break;
            case 5:
                button.addListener(luoKuuntelija("https://www.youtube.com/watch?v=9R8aSKwTEMg"));
                break;
            case 6:
                button.addListener(luoKuuntelija("https://www.riemurasia.net/kuva/Typera-jaatelo/164917"));
                break;
        }
        //Todo tänne log ehdollisuus masterswitchin kautta?
        Gdx.app.log("IBK", "kuuntelija " + button.getClickListener() + "luotu");
//        if (Integer.parseInt(s.getID()) == 1) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("http://www.ymca.fi");
//                    }
//                }
//            });
//        }
//        if (Integer.parseInt(s.getID()) == 2) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("http://www.mielenterveysseura.fi/fi");
//                    }
//                }
//            });
//        }
//        if (Integer.parseInt(s.getID()) == 3) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("https://www.ray.fi");
//                    }
//                }
//            });
//        }
//        if (Integer.parseInt(s.getID()) == 4) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("http://www.cameraobscura.fi/");
//                    }
//                }
//            });
//        }
//        if (Integer.parseInt(s.getID()) == 5) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("https://www.youtube.com/watch?v=9R8aSKwTEMg");
//                    }
//                }
//            });
//        }
//        if (Integer.parseInt(s.getID()) == 6) {
//            button.addListener(new ChangeListener() {
//                public void changed(ChangeEvent event, Actor actor) {
//                    Gdx.app.log("IBK", "Info nappulaa painettu");
//
//                    if (!zoomedOut) {
//                        Gdx.net.openURI("https://www.riemurasia.net/kuva/Typera-jaatelo/164917");
//                    }
//                }
//            });
//        }
    }

    private ChangeListener luoKuuntelija(final String linkkisivu) {
        return new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("IBK", "Info nappulaa painettu");

                if (!zoomedOut) {
                    Gdx.net.openURI(linkkisivu);
                }
            }
        };
    }

    public void paivitaInfoButtonit(float delta, float angleToPoint, boolean zoomedOut) {
        this.zoomedOut = zoomedOut;
        for (Table table : buttonTaulukot) {
            table.setTransform(true);
            table.setRotation(angleToPoint - 90);
        }
    }


}



