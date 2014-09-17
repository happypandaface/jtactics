package com.zooth.jt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.*;

public class JTAssets
{
  public Texture hex;
  public Texture guy;
  public Texture glow;
  public Texture white;
  public Texture fireball;
  public Texture mage;
  public Texture whiteMage;
  public BitmapFont font;

  public void create()
  {
    hex = new Texture("hexTile.png");
    guy = new Texture("guy.png");
    glow = new Texture("glow.png");
    mage = new Texture("mage.png");
    fireball = new Texture("fireball.png");
    whiteMage = new Texture("whiteMage.png");
    {
      Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
      p.setColor(1, 1, 1, 1);
      p.fill();
      white = new Texture(p);
    }
    {
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Chunkfive Ex.ttf"));
      font = generator.generateFont((int)(Gdx.graphics.getWidth()*.05f));
    }
  }
}
