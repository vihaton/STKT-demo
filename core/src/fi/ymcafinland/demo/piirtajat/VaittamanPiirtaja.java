package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittama;
import fi.ymcafinland.demo.main.SelviytyjanPurjeet;

/**
 * Created by jwinter on 17.4.2016.
 */
public class VaittamanPiirtaja {

    private final Skin skin;
    private TextureAtlas atlas;
    private Slider slider;
    private Table table;
    private Slider.SliderStyle style;
    private BitmapFont font;
    private Stage stage;
    private Table rootTable;
    private ArrayList<Vaittama> solmunVaittamat;

    public VaittamanPiirtaja(Stage stage, Table rootTable) {
        this.font = new BitmapFont();
        this.stage = stage;
        this.rootTable = rootTable;

        atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        skin = new Skin();
        skin.addRegions(atlas);
        style = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));
        slider = new Slider(-5, 5, .2f, false, style);
        slider.setAnimateDuration(0.1f);
        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(slider);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("UITest", "slider: " + slider.getValue());
            }
        });
        //stage.addActor(table);

    }

    public void renderoi(SpriteBatch batch, GlyphLayout layout, float delta) {
        float y = SelviytyjanPurjeet.V_HEIGHT - 1.5f * layout.height;

        //todo päivittää näytön näkymän, EI LUO MITÄÄN UUSIA TAULUKOITA, LABELEITÄ YM

        batch.begin();
        for (int i = 0; i < solmunVaittamat.size(); i++) {
            Vaittama v = solmunVaittamat.get(i);
            layout.setText(font, v.getTeksti());

            font.draw(batch, layout, 10, y);
            y -= 1.5 * layout.height;

            slider.setValue(v.getArvo());
            slider.act(delta);
            slider.draw(batch, 1);
        }
        batch.end();
    }

    public void paivitaVaittamat(ArrayList<Vaittama> solmunVaittamat) {
        this.solmunVaittamat = solmunVaittamat;

        //todo päivittää rootTableen oikeat väittämätaulukot (label + slider)

    }
}
