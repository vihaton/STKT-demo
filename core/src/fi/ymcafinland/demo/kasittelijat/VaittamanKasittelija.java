package fi.ymcafinland.demo.kasittelijat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 17.4.2016.
 */
public class VaittamanKasittelija {

    private final Skin skin;
    private ArrayList<Slider> sliderit;
    private Stage stage;
    private Table rootTable;
    private ScrollPane pane;
    private SelviytyjanPurjeet sp;

    public VaittamanKasittelija(Stage stage, Skin masterSkin, SelviytyjanPurjeet sp) {
        this.stage = stage;
        this.rootTable = new Table();
        this.sliderit = new ArrayList<>();
        skin = masterSkin;
        luoScrollPane();
        this.sp = sp;
    }

    private void luoScrollPane() {
        pane = new ScrollPane(rootTable.top());
        pane.setBounds(0, 0, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT / 1.35f);
        pane.validate();

        stage.addActor(pane);
    }

    public void paivita(float delta) {
        for (Slider s : sliderit) {
            s.act(delta);
        }

        pane.act(delta);
    }

    //todo panen yläboundi asetetaan käytössä näkymän otsikon mukaan (jos pitkä otsikko, niin yläraja tulee alemmas)

    /**
     * Ylläpitää questionscreenissä esiintyvää väittämä+slider kokonaisuutta.
     * @param solmunVaittamat
     * @return
     */
    public ArrayList<Float> paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        ArrayList<Float> vaittamienAlkuperaisetArvot = new ArrayList<>();
        rootTable.reset();
        pane.setScrollY(0); //asettaa scrollin yläasentoon

        for (final Vaittama nykyinenVaittama : solmunVaittamat) {
            vaittamienAlkuperaisetArvot.add(nykyinenVaittama.getVaikuttavaArvo());

            Table vaittamanTaulukko = new Table();
            Label otsikko = new Label(nykyinenVaittama.getVaittamanTeksti(), skin, "vaittamatyyli");
            otsikko.setFontScale(2);
            otsikko.setWrap(true);
            otsikko.setAlignment(Align.center);

            final Slider slider = new Slider(-0.5f, 0.5f, .01f, false, skin.get("sliderStyleMid", Slider.SliderStyle.class));
            slider.setAnimateDuration(0.1f);
            slider.setValue(nykyinenVaittama.getNakyvaArvo());

            luoKuuntelijat(nykyinenVaittama, slider);

            sliderit.add(slider);
            vaittamanTaulukko.add(otsikko).pad(15).width(slider.getWidth() * 3);
            vaittamanTaulukko.row();

            Table sliderTaulukko = createSliderToTable(slider);

            rootTable.add(vaittamanTaulukko);
            rootTable.row();
            rootTable.add(sliderTaulukko);
            rootTable.row();
        }

        rootTable.padTop(10);
        rootTable.padBottom(Gdx.graphics.getHeight() / 6);

        Table exitTable = createReturnButton();
        rootTable.add(exitTable).pad(64);

        return vaittamienAlkuperaisetArvot;
    }
    private Table createReturnButton() {
        Button returnButton = new Button(skin.get("returnButtonStyle", Button.ButtonStyle.class));
        returnButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("QS", "returnbuttonia painettiin");

                    sp.setPlayScreen();

            }
        });

        Table exitTable = new Table();
        exitTable.add(returnButton);

        exitTable.validate();

        return exitTable;
    }

    /**
     * Luo slideriin kuuluvat komponentit questionscreenin Tableen.
     *
     * @param slider
     * @return
     */
    private Table createSliderToTable(Slider slider) {
        Label miinus = new Label("-", skin, "vaittamatyyli");
        miinus.setColor(Color.RED);
        miinus.setFontScale(3);
        Label plus = new Label("+", skin, "vaittamatyyli");
        plus.setColor(Color.GREEN);
        plus.setFontScale(2);

        Table sliderTaulukko = new Table();
        sliderTaulukko.add(miinus).pad(15).padBottom(30);
        Slider.SliderStyle mid = skin.get("sliderStyleMid", Slider.SliderStyle.class);
        sliderTaulukko.add(slider).padBottom(15).minWidth(mid.background.getMinWidth() * 0.8f).minHeight(mid.background.getMinHeight() * 0.6f);
        sliderTaulukko.add(plus).pad(15).padBottom(30);
        return sliderTaulukko;
    }

    private void luoKuuntelijat(final Vaittama nykyinenVaittama, final Slider slider) {
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (SelviytyjanPurjeet.LOG)
                    Gdx.app.log("VK", "slider: " + slider.getValue());
                nykyinenVaittama.setArvo(slider.getValue());
            }
        });
        slider.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return false;
            }
        });
    }
}
