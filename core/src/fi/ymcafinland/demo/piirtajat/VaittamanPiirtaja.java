package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
public class VaittamanPiirtaja {

    private final Skin skin;
    private ArrayList<Slider> sliderit;
    private Stage stage;
    private Table rootTable;
    private ScrollPane pane;

    public VaittamanPiirtaja(Stage stage, Table rootTable, Skin masterSkin) {
        this.stage = stage;
        this.rootTable = rootTable;
        this.sliderit = new ArrayList<>();
        skin = masterSkin;
    }

    public void renderoi(SpriteBatch batch, float delta) {

        batch.begin();
        for (Slider s : sliderit) {
                s.act(delta);
        }

        pane.act(delta);
        stage.draw();
        batch.end();

    }

    public void paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        rootTable.reset();

        //ToDo jos sliderin arvoa ei muuteta, pelaajan selviytymis attribuutti ei pitäisi muuttua.
        for (final Vaittama nykyinenVaittama : solmunVaittamat) {
            Table vaittamanTaulukko = new Table();
            Label otsikko = new Label(nykyinenVaittama.getTeksti(), skin, "vaittamatyyli");
            otsikko.setFontScale(2);
            otsikko.setWrap(true);
            otsikko.setAlignment(Align.center);
            otsikko.setWidth(SelviytyjanPurjeet.V_WIDTH);

            final Slider slider = new Slider(0.5f, 1.5f, .1f, false, skin.get("sliderStyle", Slider.SliderStyle.class));
            slider.setAnimateDuration(0.1f);
            slider.setValue(nykyinenVaittama.getArvo());
            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("UITest", "slider: " + slider.getValue());

                    nykyinenVaittama.setArvo(slider.getValue());

                }
            });
            slider.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    event.stop();
                    return false;
                }
            });
            sliderit.add(slider);

            vaittamanTaulukko.add(otsikko).width(slider.getWidth() * 3);
            vaittamanTaulukko.row();
            vaittamanTaulukko.add(slider);

            rootTable.add(vaittamanTaulukko);
            rootTable.row();
            //ToDo Oletettavasti jokaisella solmulla on tarpeeksi kysymyksiä ettei näkymä näytä vammaselta scrollpanen sisällä, mutta ei välttämättä vielä demossa. Tehdään jokin purkkaviritelmä? vrt. esim C3 ja C3

            pane = new ScrollPane(rootTable.bottom());
            pane.setBounds(0, 0, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT / 1.35f);
            pane.setTouchable(Touchable.enabled);
            pane.layout();

            stage.addActor(pane);
        }

        rootTable.padBottom(Gdx.graphics.getHeight() / 6);
    }
}
