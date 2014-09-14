package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JTactics extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
  JTGame game;
  static JTAssets assets;
	
  public JTactics()
  {
    game = new JTGame();
    assets = new JTAssets();
  }

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
    game.create();
    assets.create();
    game.startGame();
	}

	@Override
	public void render () {
    game.render();
	}
}
