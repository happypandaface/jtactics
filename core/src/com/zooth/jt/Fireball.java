package com.zooth.jt;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;

public class Fireball
{
  public Vector2 pos;
  public Vector2 tail;
  public JTGame game;
  public float tailLen = 5;
  public void setGame(JTGame g)
  {
    game = g;
  }
  public void draw(SpriteBatch sb)
  {
    float width = game.field.height*.5f;
    float height = game.field.height*.5f;
    float offY = game.field.width*.3f;
    sb.setColor(1, 1, 1, 1);
    for (float dst = 0; dst < 1; dst += 1f/tailLen)
    {
      float size = (.4f+.6f*dst);
      sb.draw(JTactics.assets.fireball, pos.x+game.field.width/2-width*size/2+tail.x*dst, pos.y+game.field.height/2-height*size/2+offY+tail.y*dst, width*size, height*size);
    }
  }
}
