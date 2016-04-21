package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private ScrollPane pane;

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

        pane.act(delta);
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
            otsikko.setWidth(SelviytyjanPurjeet.V_WIDTH);

            //todo slider kuvista isommat, ilman että käytettävyys kärsii
            final Slider slider = new Slider(0.5f, 1.5f, .05f, false, sliderStyle);
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




            vaittamaTaulukko.add(otsikko).width(slider.getWidth() * 3);
            vaittamaTaulukko.row();
            vaittamaTaulukko.add(slider);

            rootTable.add(vaittamaTaulukko);
            rootTable.row();
            //ToDo Oletettavasti jokaisella solmulla on tarpeeksi kysymyksiä ettei näkymä näytä vammaselta scrollpanen sisällä, mutta ei välttämättä vielä demossa. Tehdään jokin purkkaviritelmä? vrt. esim C3 ja C3


            pane = new ScrollPane(rootTable.bottom());

            pane.setBounds(0, 0, SelviytyjanPurjeet.V_WIDTH, SelviytyjanPurjeet.V_HEIGHT / 1.5f);

            pane.layout();
            pane.setTouchable(Touchable.enabled);

            stage.addActor(pane);
        }
    }
}
