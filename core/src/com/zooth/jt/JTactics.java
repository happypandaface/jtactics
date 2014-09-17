package com.zooth.jt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zooth.jt.game.*;

public class JTactics extends ApplicationAdapter {
  public SpriteBatch batch;
  public Texture img;
  public JTGame game;
  public OpeningCutscene oc;
  public static JTAssets assets;

  public JTactics()
  {
    game = new JTGame();
    oc = new OpeningCutscene();
    assets = new JTAssets();
  }

  @Override
  public void create () {
    batch = new SpriteBatch();
    img = new Texture("badlogic.jpg");
    game.create();
    oc.create();
    assets.create();
    game.startGame();
  }

  @Override
  public void render () {
    oc.render();
  }
}
