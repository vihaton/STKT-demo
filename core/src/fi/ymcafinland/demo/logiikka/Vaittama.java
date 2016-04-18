package fi.ymcafinland.demo.logiikka;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Created by xvixvi on 16.4.2016.
 */
public class Vaittama {

    private String teksti;
    private String solmunID;
    private float arvo;
    private Slider slider;

    public Vaittama(String txt, String solmunID) {
        this.teksti = txt;
        this.solmunID = solmunID;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        Skin skin = new Skin();
        skin.addRegions(atlas);
        Slider.SliderStyle style = new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob"));
        this.slider = new Slider(-5, 5, .2f, false, new Slider.SliderStyle(skin.getDrawable("sliderbackground"), skin.getDrawable("sliderknob")));
        slider.setAnimateDuration(0.3f);
    }

    public String getTeksti() {
        return teksti;
    }

    public String getSolmunID() {
        return solmunID;
    }

    public float getArvo() {
        return this.arvo;
    }

    public void setArvo(float arvo) {
        this.arvo = arvo;
    }
}
