package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import fi.ymcafinland.demo.screens.InfoScreen;
import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.screens.PalauteScreen;
import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.screens.QuestionScreen;

public class SelviytyjanPurjeet extends Game {
    //TODO koko ohjelmalle yhteinen Skin
    //Todo kovakoodaus pois, SP tarkistaa juuri tässä buildissa käytettävän kuvakoon ja antaa sen verkolle.
    public final static int V_WIDTH = 576;
    public final static int V_HEIGHT = 1024;

    public static final int TAUSTAN_LEVEYS = 8192;
    public static final int TAUSTAN_KORKEUS = 8192;

    protected SpriteBatch batch;

    private Verkko verkko;
    private PlayScreen playscreen;
    private PalauteScreen palauteScreen;
    private QuestionScreen questionScreen;
    private Vaittamat vaittamat;
    private Skin masterSkin;


    @Override
    public void create() {
        Gdx.app.log("SP", "SelviytyjänPurjeet -luokan create() -metodia kutsuttiin");


        Gdx.app.log("SP", "Verkon luominen aloitetaan");
        verkko = new Verkko(TAUSTAN_LEVEYS, TAUSTAN_KORKEUS);
        Gdx.app.log("SP", "Verkko luominen on valmis");

        Gdx.app.log("SP", "Vaittamien luominen aloitetaan");
        vaittamat = new Vaittamat();
        Gdx.app.log("SP", "Vaittamien luominen on valmis");

        luoSkin();
        Pelaaja pelaaja = new Pelaaja();

        this.questionScreen = new QuestionScreen(this, pelaaja, vaittamat, masterSkin);
        this.palauteScreen = new PalauteScreen(this, pelaaja, masterSkin);
        this.playscreen = new PlayScreen(this, verkko.getSolmut().get(0), pelaaja, masterSkin);

        setScreen(new InfoScreen(this));
        Gdx.app.log("SP", "ruuduksi asetettiin infoscreen, create() metodi päättyy");
    }

    private void luoSkin() {
        masterSkin = new Skin();

        generoiFontit();
        generoiLabelStyles();
        generoiSliderStyle();

    }

    private void generoiFontit() {
        BitmapFont fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false); //must be set true to be flipped
        masterSkin.add("fontti", fontti);
    }

    private void generoiLabelStyles() {
        Label.LabelStyle otsikkoStyle = new Label.LabelStyle(masterSkin.getFont("fontti"), masterSkin.getFont("fontti").getColor());
        masterSkin.add("otsikko", otsikkoStyle);

        Label.LabelStyle sisaltotyyli = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        masterSkin.add("sisalto", sisaltotyyli);

        Label.LabelStyle vaittamatyyli = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        masterSkin.add("vaittamatyyli", vaittamatyyli);

        Label.LabelStyle arvioStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        masterSkin.add("arvio", arvioStyle);
    }

    private void generoiSliderStyle() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        masterSkin.addRegions(atlas);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(masterSkin.getDrawable("sliderbackground"), masterSkin.getDrawable("sliderknob"));
        masterSkin.add("sliderStyle", sliderStyle);
    }

    @Override
    public void render() {
        super.render();
    }


    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public void setQuestionScreen(Solmu solmu) {
        questionScreen.setSolmu(solmu);
        setScreen(questionScreen);
    }

    public void setPalauteScreen() {
        setScreen(palauteScreen);
    }

    public void resetPlayScreen() {
		playscreen.resetInputProcessor();
        playscreen.resetStateTime();
        setScreen(playscreen);
    }

    public Verkko getVerkko() {
        return verkko;
    }
}

