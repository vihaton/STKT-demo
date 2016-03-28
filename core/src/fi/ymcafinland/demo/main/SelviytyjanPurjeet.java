package fi.ymcafinland.demo.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.ymcafinland.demo.screens.PlayScreen;
import fi.ymcafinland.demo.logiikka.Verkko;

public class SelviytyjanPurjeet extends Game {
	SpriteBatch batch;
	private Verkko verkko;
	public final static int V_WIDTH = 180;
	public final static int V_HEIGHT = 300;

	
	@Override
	public void create() {
		//ToDo VILI oikea verkkorakenne tänne käytettäväksi.


		//ToDo kovakoodaus pois, SP tarkistaa juuri tässä buildissa käytettävän kuvakoon ja antaa sen verkolle.
		Texture taustakuva = new Texture("pallokuva.png");
		verkko = new Verkko(taustakuva.getWidth(), taustakuva.getHeight());

		//TODO VILI Playscreenille konstruktorissa aloitussolmu
		setScreen(new PlayScreen(this, verkko.getSolmut().get(0)));

	}
	@Override
	public void render () {
		super.render();
	}



	@Override
	public void dispose() {
		super.dispose();

		batch.dispose();
	}



	}

