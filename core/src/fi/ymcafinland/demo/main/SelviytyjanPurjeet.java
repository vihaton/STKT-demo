package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.logiikka.Verkko;

public class SelviytyjanPurjeet extends Game {
    SpriteBatch batch;
    private Verkko verkko;
    public final static int V_WIDTH = 540;
    public final static int V_HEIGHT = 960;


    @Override
    public void create() {

        //ToDo kovakoodaus pois, SP tarkistaa juuri tässä buildissa käytettävän kuvakoon ja antaa sen verkolle.
        Texture taustakuva = new Texture("vitunisovalkoinentausta.png");
        verkko = new Verkko(taustakuva.getWidth(), taustakuva.getHeight());

        setScreen(new PlayScreen(this, taustakuva, verkko.getSolmut().get(0)));

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

    public Verkko getVerkko() {
        return verkko;
    }
}

