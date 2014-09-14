package com.zooth.jt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JTAssets
{
  Texture hex;
  Texture guy;
  Texture glow;
  public void create()
  {
    hex = new Texture("hexTile.png");
    guy = new Texture("guy.png");
    glow = new Texture("glow.png");
  }
}
