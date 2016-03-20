package fi.ymcafinland.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SelviytyjanPurjeet extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	private Sprite sprite;
	
	@Override
	public void create () {
		camera = new OrthographicCamera(180, 300);
		batch = new SpriteBatch();
		img = new Texture("pallokuva.png");
		sprite = new Sprite(img);
		sprite.setOrigin(0,0);
		sprite.setPosition((-sprite.getWidth()/2+60),-sprite.getHeight()/2-50);
	}

	@Override
	public void render () {
		float rgbJakaja = 255f;
		Gdx.gl.glClearColor(0, 0, 139/rgbJakaja, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
}
