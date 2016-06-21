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

import fi.ymcafinland.demo.logiikka.LinkillinenVaittama;
import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.main.MasterSkin;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 17.4.2016.
 */
public class VaittamanKasittelija {

    private final Skin skin;
    private ArrayList<Slider> sliderit;
    private Stage stage;
    private Table scrollPanesRootTable;

    private Table continueButtonTable;
    private ScrollPane pane;
    private SelviytyjanPurjeet sp;
    private final float sliderinLeveys;
    private final float sliderinKorkeus;

    public VaittamanKasittelija(Stage stage, Skin masterSkin, SelviytyjanPurjeet sp) {
        this.stage = stage;
        this.scrollPanesRootTable = new Table();
        this.sliderit = new ArrayList<>();
        skin = masterSkin;
        luoScrollPane();
        this.sp = sp;
        Slider.SliderStyle mid = skin.get("sliderStyleMid", Slider.SliderStyle.class);
        sliderinLeveys = mid.background.getMinWidth() * 0.8f;
        sliderinKorkeus = mid.background.getMinHeight() * 0.6f;
    }

    public ScrollPane getPane() {
        return pane;
    }

    private void luoScrollPane() {

        pane = new ScrollPane(scrollPanesRootTable.top(), skin.get("scrollPaneKnob", ScrollPane.ScrollPaneStyle.class));
        pane.setFadeScrollBars(false);

        pane.setBounds(0, SelviytyjanPurjeet.V_HEIGHT / 6f, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT / 1.8f);
        pane.validate();
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
     *
     * @param solmunVaittamat
     * @return
     */
    public ArrayList<Float> paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        ArrayList<Float> vaittamienAlkuperaisetArvot = new ArrayList<>();
        scrollPanesRootTable.reset();
        pane.setScrollY(0); //asettaa scrollin yläasentoon

        for (final Vaittama nykyinenVaittama : solmunVaittamat) {
            vaittamienAlkuperaisetArvot.add(nykyinenVaittama.getVaikuttavaArvo());

            Table vaittamanTaulukko = luoVaittamanTaulukko(nykyinenVaittama);

            final Slider slider = new Slider(-0.5f, 0.5f, .01f, false, skin.get("sliderStyleMid", Slider.SliderStyle.class));
            slider.setAnimateDuration(0.1f);
            slider.setValue(nykyinenVaittama.getNakyvaArvo());
            sliderit.add(slider);

            luoKuuntelijat(nykyinenVaittama, slider);

            Table sliderTaulukko = createSliderToTable(slider);

            scrollPanesRootTable.add(vaittamanTaulukko);
            scrollPanesRootTable.row();
            scrollPanesRootTable.add(sliderTaulukko);
            scrollPanesRootTable.row();
        }

        scrollPanesRootTable.padTop(10);
        scrollPanesRootTable.padBottom(Gdx.graphics.getHeight() / 6);


        return vaittamienAlkuperaisetArvot;
    }

    public void setContinueButtonTable(Table continueButtonTable) {
        this.continueButtonTable = continueButtonTable;
    }

    public Table luoVaittamanTaulukko(Vaittama nykyinenVaittama) {
        Table vaittamanTaulukko = new Table();

        Label otsikko = new Label(nykyinenVaittama.getVaittamanTeksti(), skin, "vaittamatyyli");
        otsikko.setFontScale(MasterSkin.HIERO_FONT_SCALE);
        otsikko.setWrap(true);
        otsikko.setAlignment(Align.center);

        //todo väittämän asettaminen kivasti silloinkin, kun on infobutton kehissä
        vaittamanTaulukko.add(otsikko).pad(15).width(sliderinLeveys);

        if (nykyinenVaittama.getClass().equals(LinkillinenVaittama.class)) {
            LinkillinenVaittama lv = (LinkillinenVaittama) nykyinenVaittama;
            Button infoButton = new Button(skin.get("infoButtonStyle", Button.ButtonStyle.class));
            infoButton.addListener(luoKuuntelija(lv.getLinkki()));

            vaittamanTaulukko.add(infoButton).maxSize(64);
        }

        return vaittamanTaulukko;
    }

    private ChangeListener luoKuuntelija(final String linkkisivu) {
        return new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (SelviytyjanPurjeet.LOG)
                    Gdx.app.log("VK", "Infonappulaa painettu");

                Gdx.net.openURI(linkkisivu);
            }
        };
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
        miinus.setFontScale(MasterSkin.HIERO_FONT_SCALE);
        Label plus = new Label("+", skin, "vaittamatyyli");
        plus.setColor(Color.GREEN);
        plus.setFontScale(MasterSkin.HIERO_FONT_SCALE);

        Table sliderTaulukko = new Table();
        sliderTaulukko.add(miinus).pad(15).padBottom(30);
        sliderTaulukko.add(slider).padBottom(15).minWidth(sliderinLeveys).minHeight(sliderinKorkeus);
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
                nykyinenVaittama.setChecked(true);
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
