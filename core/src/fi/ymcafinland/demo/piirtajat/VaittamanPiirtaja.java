package fi.ymcafinland.demo.piirtajat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

import java.util.ArrayList;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittama;

/**
 * Created by jwinter on 17.4.2016.
 */
public class VaittamanPiirtaja {

    private final Skin skin;
    private TextureAtlas atlas;
    private Slider slider;
    private Table table;
    private Slider.SliderStyle style;
    private ArrayList<Vaittama> solmunVaittamat;
    private BitmapFont font;

    public VaittamanPiirtaja(ArrayList<Vaittama> solmunVaittamat) {
        this.solmunVaittamat = solmunVaittamat;
        this.font = new BitmapFont();

        atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        skin = new Skin();
        skin.addRegions(atlas);
        style = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));
        slider = new Slider(-5, 5, .2f, false, style);
        slider.setAnimateDuration(0.3f);
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

    public void piirra(SpriteBatch batch, GlyphLayout layout, float x, float y, float delta) {

        batch.begin();
        for (int i = 0; i < solmunVaittamat.size(); i++) {
            layout.setText(font, solmunVaittamat.get(i).getTeksti());
            font.draw(batch, layout, x, y);
            y -= 1.5 * layout.height;
            slider.act(delta);
            slider.draw(batch, 1);
        }
        batch.end();
    }
}
