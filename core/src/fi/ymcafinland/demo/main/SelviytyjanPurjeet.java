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

    public static final boolean LOG = true;

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
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("SP", "SelviytyjänPurjeet -luokan create() -metodia kutsuttiin");

        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("SP", "Verkon luominen aloitetaan");
        verkko = new Verkko(TAUSTAN_LEVEYS, TAUSTAN_KORKEUS);
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("SP", "Verkko luominen on valmis");

        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("SP", "Vaittamien luominen aloitetaan");
        vaittamat = new Vaittamat();
        if (SelviytyjanPurjeet.LOG)
            Gdx.app.log("SP", "Vaittamien luominen on valmis");

        masterSkin = new MasterSkin();
        pelaaja = new Pelaaja();
        pelaaja.setVaittamienMaara(vaittamat.getMaara());

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

    public void setPlayScreen(Solmu solmu) {
        playscreen.resetStateTime();
        playscreen.seurataanPolttoa = true;
        if(solmu != null)
            playscreen.setSolmu(solmu);
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
        if (SelviytyjanPurjeet.LOG)
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

