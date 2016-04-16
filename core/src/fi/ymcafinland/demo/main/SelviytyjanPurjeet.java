package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.ymcafinland.demo.logiikka.Pelaaja;
import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.logiikka.Vaittamat;
import fi.ymcafinland.demo.screens.PalauteScreen;
import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.screens.QuestionScreen;

public class SelviytyjanPurjeet extends Game {
    public final static int V_WIDTH = 576;
    public final static int V_HEIGHT = 1024;

    //Todo kovakoodaus pois, SP tarkistaa juuri tässä buildissa käytettävän kuvakoon ja antaa sen verkolle.
    public static final int T_LEVEYS = 8192;
    public static final int T_KORKEUS = 8192;

    protected SpriteBatch batch;
    private Verkko verkko;
    private PlayScreen playscreen;
    private PalauteScreen palauteScreen;
    private QuestionScreen questionScreen;
    private Vaittamat vaittamat;


    @Override
    public void create() {
        Gdx.app.log("SP", "SelviytyjänPurjeet -luokan create() -metodia kutsuttiin");

        Gdx.app.log("SP", "Verkon luominen aloitetaan");
        verkko = new Verkko(T_LEVEYS, T_KORKEUS);
        Gdx.app.log("SP", "Verkko luominen on valmis");

        Gdx.app.log("SP", "Vaittamien luominen aloitetaan");
        vaittamat = new Vaittamat();
        Gdx.app.log("SP", "Vaittamien luominen on valmis");

        Pelaaja pelaaja = new Pelaaja();
        questionScreen = new QuestionScreen(this, pelaaja, vaittamat);
        palauteScreen = new PalauteScreen(this, pelaaja);

        this.playscreen = new PlayScreen(this, verkko.getSolmut().get(0));
        setScreen(playscreen);
        Gdx.app.log("SP", "ruuduksi asetettiin playscreen, create() metodi päättyy");
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
        setScreen(playscreen);
    }

    public Verkko getVerkko() {
        return verkko;
    }
}

