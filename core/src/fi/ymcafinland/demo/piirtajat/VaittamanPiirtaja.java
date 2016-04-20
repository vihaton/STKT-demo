package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private TextureAtlas atlas;
    private ArrayList<Slider> sliderit;
    private Slider.SliderStyle sliderStyle;
    private Stage stage;
    private Table rootTable;

    public VaittamanPiirtaja(Stage stage, Table rootTable) {
        this.stage = stage;
        this.rootTable = rootTable;
        this.sliderit = new ArrayList<>();

        atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        skin = new Skin();
        skin.addRegions(atlas);

        Label.LabelStyle vaittamatyyli = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        skin.add("vaittamatyyli", vaittamatyyli);

        sliderStyle = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));
    }

    public void renderoi(SpriteBatch batch, float delta) {

        batch.begin();
        for (Slider s : sliderit) {
            s.act(delta);
        }
        stage.draw();
        batch.end();

    }

    public void paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        rootTable.reset();

        for (final Vaittama nykyinenVaittama : solmunVaittamat) {
            Table vaittamaTaulukko = new Table();
            //todo väittämätekstien keskittäminen
            Label otsikko = new Label(nykyinenVaittama.getTeksti(), skin, "vaittamatyyli");
            otsikko.setFontScale(2);
            otsikko.setWrap(true);
            otsikko.setAlignment(Align.center);
            otsikko.setWidth(SelviytyjanPurjeet.V_WIDTH);

            //todo slider kuvista isommat, ilman että käytettävyys kärsii
            final Slider slider = new Slider(0.5f, 1.5f, .1f, false, sliderStyle);
            slider.setAnimateDuration(0.1f);
            slider.setValue(nykyinenVaittama.getArvo());
            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("UITest", "slider: " + slider.getValue());
                    nykyinenVaittama.setArvo(slider.getValue());
                }
            });
            sliderit.add(slider);

            //todo jos paljon väittämiä, väittämät eivät peitä otsikkoa! (esim C3, ID = 15) (väittämät scroll panen sisään?)

            vaittamaTaulukko.add(otsikko).width(slider.getWidth() * 3);
            vaittamaTaulukko.row();
            vaittamaTaulukko.add(slider);

            rootTable.add(vaittamaTaulukko);
            rootTable.row();
        }
    }
}
