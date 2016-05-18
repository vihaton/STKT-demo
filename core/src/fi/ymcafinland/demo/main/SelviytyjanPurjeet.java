package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.screens.InfoScreen;
import fi.ymcafinland.demo.screens.LauncherScreen;
import fi.ymcafinland.demo.screens.PalauteScreen;
import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.screens.PohjaScreen;
import fi.ymcafinland.demo.screens.QuestionScreen;

public class SelviytyjanPurjeet extends Game {
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
    private InfoScreen infoScreen;
    private LauncherScreen launcherScreen;
    private Vaittamat vaittamat;
    private Skin masterSkin;
    private Pelaaja pelaaja;


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
        pelaaja = new Pelaaja();

        this.questionScreen = new QuestionScreen(this, pelaaja, vaittamat, masterSkin);
        this.palauteScreen = new PalauteScreen(this, pelaaja, masterSkin);
        this.playscreen = new PlayScreen(this, verkko.getSolmut().get(0), pelaaja, masterSkin);
        this.infoScreen = new InfoScreen(this, masterSkin);
        this.launcherScreen = new LauncherScreen(this, masterSkin, "LS");

        setScreen(launcherScreen);
//        setScreen(playscreen);
    }

    /**
     * Kutsutaan launcherissa kun SP:n käyttö aloitetaan
     */
    public void init() {
        setScreen(infoScreen);
    }

    /**
     * Tarkoitus olisi, että kaikki pelissä käytetyt UI elementit (fontit, tyylit, textuurit...)
     * luodaan Selviytyjän purjeissa. Näin ollen kaikki olisi helposti päivitettävissä yhdessä paikassa.
     */
    private void luoSkin() {
        masterSkin = new Skin();

        generoiTexturet();
        generoiFontit();
        generoiLabelStylet();
        generoiSliderStyle();
        generoiProgressBarStylet();
        generoiButtonStylet();
        generoiTextureAtlakset();
    }

    private void generoiTexturet() {
        masterSkin.add("infonTausta", new Texture("sails02.png"));

        masterSkin.add("alku", new Texture("alku.png"));

        masterSkin.add("ruksi", new Texture("ruksi.png"));

        masterSkin.add("emptynode", new Texture("emptynode.png"));

        masterSkin.add("mini_palaute", new Texture("hahmo.png"));

        masterSkin.add("i", new Texture("i.png"));

        masterSkin.add("transparent", new Texture("transparent.png"));

        masterSkin.add("minimap", new Texture("minimap.png"));

        masterSkin.add("launcher", new Texture("launcherBackground.png"));

        masterSkin.add("SP_logo", new Texture("ic_launcher-web.png"));

        masterSkin.add("unavailable", new Texture("unavailable.png"));

        masterSkin.add("menubutton", new Texture("menubutton.png"));

        masterSkin.add("menutausta", new Texture("menutausta.png"));

        masterSkin.add("menubar", new Texture("menubar.png"));

    }

    private void generoiFontit() {
        BitmapFont fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false); //must be set true to be flipped
        masterSkin.add("fontti", fontti);

        BitmapFont libgdxFont = new BitmapFont(Gdx.files.internal("default.fnt"), Gdx.files.internal("default.png"), false);
        masterSkin.add("libgdxFont", libgdxFont);
    }

    private void generoiLabelStylet() {
        //TODO tehdään eri fontti kuin libgdx:n defaultfontti
        Label.LabelStyle launcherStyle = new Label.LabelStyle(masterSkin.getFont("libgdxFont"), masterSkin.getFont("libgdxFont").getColor());
        masterSkin.add("launcher", launcherStyle);

        Label.LabelStyle otsikkoStyle = new Label.LabelStyle(masterSkin.getFont("fontti"), masterSkin.getFont("fontti").getColor());
        masterSkin.add("otsikko", otsikkoStyle);

        Label.LabelStyle sisaltotyyli = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        masterSkin.add("sisalto", sisaltotyyli);

        Label.LabelStyle vaittamatyyli = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        masterSkin.add("vaittamatyyli", vaittamatyyli);

        Label.LabelStyle arvioStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        masterSkin.add("arvio", arvioStyle);

        Label.LabelStyle infotekstiStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        masterSkin.add("infoteksti", infotekstiStyle);
    }

    private void generoiSliderStyle() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        masterSkin.addRegions(atlas);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(masterSkin.getDrawable("sliderbackground"), masterSkin.getDrawable("sliderknob"));
        masterSkin.add("sliderStyle", sliderStyle);
    }

    private void generoiProgressBarStylet() {
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

        Texture progressBackground = new Texture("progressbar2/progressbackground.png");
        Texture progressKnob = new Texture("progressbar2/progressknob.png");

        progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(progressKnob));
        progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(progressBackground));
        progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(progressKnob));

        masterSkin.add("progressBarStyle", progressBarStyle);
    }

    private void generoiButtonStylet() {
        Button.ButtonStyle styleAlku = new Button.ButtonStyle();
        styleAlku.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("alku", Texture.class)));
        masterSkin.add("alkuButtonStyle", styleAlku);

        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        styleExit.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("ruksi", Texture.class)));
        masterSkin.add("exitButtonStyle", styleExit);

        Button.ButtonStyle styleInfo = new Button.ButtonStyle();
        styleInfo.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("i", Texture.class)));
        masterSkin.add("infoButtonStyle", styleInfo);

        Button.ButtonStyle styleTrans = new Button.ButtonStyle();
        styleTrans.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("transparent", Texture.class)));
        masterSkin.add("transButtonStyle", styleTrans);

        Button.ButtonStyle styleSp = new Button.ButtonStyle();
        styleSp.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("SP_logo", Texture.class)));
        masterSkin.add("spButtonStyle", styleSp);

        Button.ButtonStyle styleUnavailable = new Button.ButtonStyle();
        styleUnavailable.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("unavailable", Texture.class)));
        masterSkin.add("unavailableButtonStyle", styleUnavailable);
    }

    private void generoiTextureAtlakset() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("minisolmut/minisolmut.pack"));
        masterSkin.addRegions(atlas);

        generoiHUDinButtonStylet();
    }

    /**
     * kutsuttava vasta, kun masterSkiniin on lisätty textureatlas minisolmuista.
     */
    private void generoiHUDinButtonStylet() {
        Button.ButtonStyle styleParent = new Button.ButtonStyle();
        Button.ButtonStyle styleLeft = new Button.ButtonStyle();
        Button.ButtonStyle styleRight = new Button.ButtonStyle();
        Button.ButtonStyle styleChild1 = new Button.ButtonStyle();
        Button.ButtonStyle styleChild2 = new Button.ButtonStyle();
        Button.ButtonStyle styleChild3 = new Button.ButtonStyle();
        Button.ButtonStyle styleKartta = new Button.ButtonStyle();
        Button.ButtonStyle stylePalaute = new Button.ButtonStyle();
        Button.ButtonStyle styleKysymys = new Button.ButtonStyle();
        Button.ButtonStyle styleMenu = new Button.ButtonStyle();
        TextButton.TextButtonStyle styleMenubar = new TextButton.TextButtonStyle();

        styleMenubar.font = masterSkin.getFont("libgdxFont");
        styleMenubar.up = masterSkin.getDrawable("menubar");

        styleKartta.up = masterSkin.getDrawable("minimap");
        styleKysymys.up = masterSkin.getDrawable("mini_kysymys");
        stylePalaute.up = new TextureRegionDrawable(new TextureRegion(masterSkin.get("mini_palaute", Texture.class)));
        styleMenu.up = masterSkin.getDrawable("menubutton");

        masterSkin.add("styleParent", styleParent);
        masterSkin.add("styleLeft", styleLeft);
        masterSkin.add("styleRight", styleRight);
        masterSkin.add("styleChild1", styleChild1);
        masterSkin.add("styleChild2", styleChild2);
        masterSkin.add("styleChild3", styleChild3);
        masterSkin.add("styleKartta", styleKartta);
        masterSkin.add("stylePalaute", stylePalaute);
        masterSkin.add("styleKysymys", styleKysymys);
        masterSkin.add("styleMenu", styleMenu);
        masterSkin.add("styleMenubar", styleMenubar);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Screenin vaihto mielivaltaiseen screeniin
     * @param screen
     */
    public void vaihdaScreeniin(PohjaScreen screen) {
        setScreen(screen);
    }

    public void setQuestionScreen(Solmu solmu) {
        questionScreen.setSolmu(solmu);
        setScreen(questionScreen);
    }

    public void setPalauteScreen() {
        setScreen(palauteScreen);
    }

    public void setPlayScreen() {
        playscreen.resetStateTime();
        setScreen(playscreen);
    }

    public void setAlkuScreen() {
        setScreen(launcherScreen);
    }
    public void setInfoScreen(){
        setScreen(infoScreen);
    }

    public Verkko getVerkko() {
        return verkko;
    }

    /**
     * kutsuu setSolmua sille solmulle, jonka arvo on pelaajan selviytymisprofiilissa suurin, ennen kuin
     * asettaa playscreenin ruuduksi.
     */
    public void setPlayScreenMaxSelviytyjaan() {
        Solmu vahvinSelviytymiskeino = verkko.getSolmut().get(pelaaja.getMaxSelviytymisenIndeksi());
        Gdx.app.log("SP", "setPlayScreenMaxSelviytyjaan: vahvimman selviytymiskeinon perusteella set solmuksi laitetaan " + vahvinSelviytymiskeino.getOtsikko());
        playscreen.setSolmu(vahvinSelviytymiskeino);
        playscreen.asetaAlkuZoom();
        setScreen(playscreen);
    }

    public PalauteScreen getPalauteScreen() {
        return palauteScreen;
    }

    public InfoScreen getInfoScreen() {
        return infoScreen;
    }

    public PlayScreen getPlayscreen() {

        return playscreen;
    }

    public QuestionScreen getQuestionScreen() {
        return questionScreen;

    }

    public LauncherScreen getAlkuScreen() {
        return launcherScreen;
    }
}

