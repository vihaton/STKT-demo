package fi.ymcafinland.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fi.ymcafinland.demo.Screens.PlayScreen;

public class SelviytyjanPurjeet extends Game {
	public final static int V_WIDTH = 180;
	public final static int V_HEIGHT = 300;

	protected SpriteBatch batch;

	
	@Override
	public void create() {
		//ToDo oikea verkkorakenne tänne käytettäväksi.







		setScreen(new PlayScreen(this));

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

