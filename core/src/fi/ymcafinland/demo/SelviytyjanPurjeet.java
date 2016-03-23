package fi.ymcafinland.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fi.ymcafinland.demo.scenes.HUD;
import logiikka.Verkko;

public class SelviytyjanPurjeet extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	private Sprite sprite;
	public final static int V_WIDTH = 180;
	public final static int V_HEIGHT = 300;
	private Viewport viewPort;
	private HUD hud;
	private Verkko verkko;
	
	@Override
	public void create () {
		verkko = new Verkko();
		camera = new OrthographicCamera();
		viewPort = new FitViewport(V_WIDTH,V_HEIGHT,camera);
		batch = new SpriteBatch();
		img = new Texture("pallokuva.png");
		sprite = new Sprite(img);
		sprite.setOrigin(0,0);
		sprite.setPosition((-sprite.getWidth() / 2 + 150), -sprite.getHeight() / 2 + 100);
		hud = new HUD(batch);
	}

	@Override
	public void render () {
		float rgbJakaja = 255f;
		Gdx.gl.glClearColor(0, 0, 139 / rgbJakaja, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(hud.stage.getCamera().combined);


		batch.begin();
		sprite.draw(batch);
		batch.end();
		hud.stage.draw();

	}
}
