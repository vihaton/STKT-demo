package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.ymcafinland.demo.logiikka.Solmu;
import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.logiikka.Verkko;
import fi.ymcafinland.demo.screens.QuestionScreen;

public class SelviytyjanPurjeet extends Game {
    public final static int V_WIDTH = 576;
    public final static int V_HEIGHT = 1024;

    protected SpriteBatch batch;

    private Verkko verkko;
    private PlayScreen playscreen;


    @Override
    public void create() {

        //ToDo later
        // kovakoodaus pois, SP tarkistaa juuri tässä buildissa käytettävän kuvakoon ja antaa sen verkolle.
        Texture taustakuva = new Texture("vitunisovalkoinentausta.png");
        verkko = new Verkko(taustakuva.getWidth(), taustakuva.getHeight());

        this.playscreen = new PlayScreen(this, taustakuva, verkko.getSolmut().get(0));
        setScreen(playscreen);

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

    //Luo toistaiseksi aina "uudet" kysymykset
    public void setQuestionScreen(Solmu solmu) {
        setScreen(new QuestionScreen(this, solmu));
    }

    public void resetPlayScreen() {
        setScreen(playscreen);
    }

    public Verkko getVerkko() {
        return verkko;
    }
}

