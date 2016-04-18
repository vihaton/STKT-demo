package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 17.4.2016.
 */
public class VaittamanPiirtaja {

    private final Skin skin;
    private TextureAtlas atlas;
    private ArrayList<Slider> sliderit;
    private Table table;
    private Slider.SliderStyle sliderStyle;
    private BitmapFont font;
    private Stage stage;
    private Table rootTable;
    private ArrayList<Vaittama> solmunVaittamat;
    private Vaittama solmuPointer;

    public VaittamanPiirtaja(Stage stage, Table rootTable) {
        this.font = new BitmapFont();
        this.stage = stage;
        this.rootTable = rootTable;
        this.sliderit = new ArrayList<>();

        atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        skin = new Skin();
        skin.addRegions(atlas);

        Label.LabelStyle vaittamatyyli = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        skin.add("vaittamatyyli", vaittamatyyli);

        sliderStyle = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));

        //stage.addActor(table);

    }

    public void renderoi(SpriteBatch batch, float delta) {

        //todo päivittää näytön näkymän, EI LUO MITÄÄN UUSIA TAULUKOITA, LABELEITÄ YM

        batch.begin();
        for (Slider s:sliderit) {
            s.act(delta);
        }
        stage.draw();
        batch.end();

    }

    public void paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        this.solmunVaittamat = solmunVaittamat;
        rootTable.reset();

        for (Vaittama nykyinenVaittama: solmunVaittamat) {
            solmuPointer = nykyinenVaittama;
            Table vaittamaTaulukko = new Table();
            Label otsikko = new Label(solmuPointer.getTeksti(), skin, "vaittamatyyli");

            final Slider slider = new Slider(-5, 5, .2f, false, sliderStyle);
            slider.setAnimateDuration(0.1f);
            slider.setValue(solmuPointer.getArvo());
            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //todo päivittää väittämien arvot changelistenerissä
                    Gdx.app.log("UITest", "slider: " + slider.getValue());
                    solmuPointer.setArvo(slider.getValue());
                }
            });
            sliderit.add(slider);

            vaittamaTaulukko.add(otsikko);
            vaittamaTaulukko.row();
            vaittamaTaulukko.add(slider);

            rootTable.add(vaittamaTaulukko);
            rootTable.row();
        }
    }
}
