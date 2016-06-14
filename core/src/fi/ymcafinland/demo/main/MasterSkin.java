package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by xvixvi on 19.5.2016.
 *
 *
 * Tarkoitus olisi, että kaikki pelissä käytetyt UI elementit (fontit, tyylit, textuurit...)
 * luodaan Selviytyjän purjeissa. Näin ollen kaikki olisi helposti päivitettävissä yhdessä paikassa.
 */
public class MasterSkin extends Skin {

    public final static float HIERO_FONT_SCALE = 0.35f;

    public MasterSkin() {
        super();

        generoiTexturet();
        generoiFontit();
        generoiLabelStylet();
        generoiSliderStyle();
        generoiProgressBarStylet();
        generoiButtonStylet();
        generoiTextureAtlakset();
    }

    private void generoiTexturet() {
        //infoscreenin taustakuva
        this.add("infonTausta", new Texture("sails02.png"));

        this.add("alku", new Texture("alku.png"));

        this.add("jatka", new Texture("jatka.png"));

        this.add("ruksi", new Texture("ruksi.png"));

        this.add("return", new Texture("return.png"));

        this.add("continue", new Texture("continue.png"));

        this.add("emptynode", new Texture("emptynode.png"));

        this.add("glow", new Texture("glow.png"));

        this.add("glowReady", new Texture("glowReady1.png"));

        this.add("mini_palaute", new Texture("hahmo.png"));

        //infonappula jossa merkki i.
        this.add("i", new Texture("i.png"));

        this.add("transparent", new Texture("transparent.png"));

        //minimap placeholder kuva
        this.add("minimap", new Texture("minimap.png"));

        this.add("launcher", new Texture("launcherBackground.png"));

        this.add("SP_logo", new Texture("ic_launcher-web.png"));

        this.add("unavailable", new Texture("unavailable.png"));

        //menu-alkuiset kuvat ovat playscreenin menuboxia varten.
        this.add("menubar", new Texture("menubar.png"));

        this.add("menubutton", new Texture("menubutton.png"));

        this.add("menutausta", new Texture("menutausta.png"));

        this.add("vaittama", new Texture("vaittama.png"));

    }

    //todo fonttien päivitys järkevämpiin
    private void generoiFontit() {

        //omatekemäfontti
        BitmapFont fontti = new BitmapFont(Gdx.files.internal("font/fontti.fnt"), Gdx.files.internal("font/fontti.png"), false); //must be set true to be flipped
        this.add("fontti", fontti);

        BitmapFont libgdxFont = new BitmapFont(Gdx.files.internal("default.fnt"), Gdx.files.internal("default.png"), false);
        this.add("libgdxFont", libgdxFont);

        BitmapFont hieroFont = new BitmapFont(Gdx.files.internal("font/fonttihiero.fnt"), false);
        this.add("hieroFont", hieroFont);
    }

    private void generoiLabelStylet() {
        //TODO tehdään eri fontti kuin libgdx:n defaultfontti
        Label.LabelStyle launcherStyle = new Label.LabelStyle(this.getFont("hieroFont"), Color.WHITE);
        this.add("launcher", launcherStyle);

        Label.LabelStyle otsikkoStyle = new Label.LabelStyle(this.getFont("fontti"), this.getFont("fontti").getColor());
        this.add("otsikko", otsikkoStyle);

        Label.LabelStyle sisaltotyyli = new Label.LabelStyle(this.getFont("hieroFont"), Color.BLACK);
        this.add("sisalto", sisaltotyyli);

        Label.LabelStyle vaittamatyyli = new Label.LabelStyle(this.getFont("hieroFont"), Color.WHITE);
        this.add("vaittamatyyli", vaittamatyyli);

        Label.LabelStyle arvioStyle = new Label.LabelStyle(this.getFont("hieroFont"), Color.WHITE);
        this.add("arvio", arvioStyle);

        Label.LabelStyle infotekstiStyle = new Label.LabelStyle(this.getFont("hieroFont"), Color.WHITE);
        this.add("infoteksti", infotekstiStyle);
    }

    private void generoiSliderStyle() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("slider/slider.pack"));
        this.addRegions(atlas);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(this.getDrawable("sliderbackground"), this.getDrawable("sliderknob"));
        this.add("sliderStyle", sliderStyle);

        Slider.SliderStyle sliderStyleMid = new Slider.SliderStyle(this.getDrawable("sliderbackgroundmid"), this.getDrawable("sliderknob"));
        this.add("sliderStyleMid", sliderStyleMid);
    }

    private void generoiProgressBarStylet() {
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

        Texture progressBackground = new Texture("progressbar2/progressbackground.png");
        Texture progressKnob = new Texture("progressbar2/progressknob.png");

        progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(progressKnob));
        progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(progressBackground));
        progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(progressKnob));

        this.add("progressBarStyle", progressBarStyle);
    }

    private void generoiButtonStylet() {
        Button.ButtonStyle styleAlku = new Button.ButtonStyle();
        styleAlku.up = new TextureRegionDrawable(new TextureRegion(this.get("alku", Texture.class)));
        this.add("alkuButtonStyle", styleAlku);

        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        styleExit.up = new TextureRegionDrawable(new TextureRegion(this.get("ruksi", Texture.class)));
        this.add("exitButtonStyle", styleExit);

        Button.ButtonStyle styleJatka = new Button.ButtonStyle();
        styleJatka.up = new TextureRegionDrawable(new TextureRegion(this.get("jatka", Texture.class)));
        this.add("jatkaButtonStyle", styleJatka);

        Button.ButtonStyle styleReturn = new Button.ButtonStyle();
        styleReturn.up = new TextureRegionDrawable(new TextureRegion(this.get("return", Texture.class)));
        this.add("returnButtonStyle", styleReturn);

        Button.ButtonStyle styleContinue = new Button.ButtonStyle();
        styleContinue.up = new TextureRegionDrawable(new TextureRegion(this.get("continue", Texture.class)));
        this.add("continueButtonStyle", styleContinue);

        Button.ButtonStyle styleInfo = new Button.ButtonStyle();
        styleInfo.up = new TextureRegionDrawable(new TextureRegion(this.get("i", Texture.class)));
        this.add("infoButtonStyle", styleInfo);

        Button.ButtonStyle styleTrans = new Button.ButtonStyle();
        styleTrans.up = new TextureRegionDrawable(new TextureRegion(this.get("transparent", Texture.class)));
        this.add("transButtonStyle", styleTrans);

        Button.ButtonStyle styleSp = new Button.ButtonStyle();
        styleSp.up = new TextureRegionDrawable(new TextureRegion(this.get("SP_logo", Texture.class)));
        this.add("spButtonStyle", styleSp);

        Button.ButtonStyle styleUnavailable = new Button.ButtonStyle();
        styleUnavailable.up = new TextureRegionDrawable(new TextureRegion(this.get("unavailable", Texture.class)));
        this.add("unavailableButtonStyle", styleUnavailable);

        Button.ButtonStyle styleVaittama = new Button.ButtonStyle();
        styleVaittama.up = new TextureRegionDrawable(new TextureRegion(this.get("vaittama", Texture.class)));
        this.add("vaittamaButtonStyle", styleVaittama);
    }

    private void generoiTextureAtlakset() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("minisolmut/minisolmut.pack"));
        this.addRegions(atlas);

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

        styleKartta.up = this.getDrawable("minimap");
        styleKysymys.up = this.getDrawable("mini_kysymys");
        stylePalaute.up = new TextureRegionDrawable(new TextureRegion(this.get("mini_palaute", Texture.class)));
        styleMenu.up = this.getDrawable("menubutton");
        styleMenubar.font = this.getFont("libgdxFont");
        styleMenubar.up = this.getDrawable("menubar");

        this.add("styleParent", styleParent);
        this.add("styleLeft", styleLeft);
        this.add("styleRight", styleRight);
        this.add("styleChild1", styleChild1);
        this.add("styleChild2", styleChild2);
        this.add("styleChild3", styleChild3);
        this.add("styleKartta", styleKartta);
        this.add("stylePalaute", stylePalaute);
        this.add("styleKysymys", styleKysymys);
        this.add("styleMenu", styleMenu);
        this.add("styleMenubar", styleMenubar);
    }

}
