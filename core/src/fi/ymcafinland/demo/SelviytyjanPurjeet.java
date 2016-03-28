package fi.ymcafinland.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import fi.ymcafinland.demo.Screens.PlayScreen;
import fi.ymcafinland.demo.scenes.HUD;
import logiikka.Solmu;

public class SelviytyjanPurjeet extends Game {
	SpriteBatch batch;
	public final static int V_WIDTH = 180;
	public final static int V_HEIGHT = 300;

	
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

