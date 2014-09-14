package com.zooth.jt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JTAssets
{
  Texture hex;
  public void create()
  {
    hex = new Texture("hexTile.png");
  }
}
