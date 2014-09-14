package com.zooth.jt;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;

public class Guy
{
  JTTile tile;
  JTTile destTile;
  JTGame game;
  boolean inTransit = false;
  float time;
  boolean selected;

  public Guy()
  {
    tile = new JTTile(0, 0, 0);
  }

  public void setGame(JTGame g)
  {
    game = g;
  }

  public void moveTo(JTTile t)
  {
    if (!inTransit)
    {
      if (!t.check(tile))
      {
        destTile = game.field.selectedTile;
        inTransit = true;
        time = 0;
      }
    }
  }

  public boolean checkSelect(JTTile t)
  {
    return (!inTransit && tile.check(t));
  }

  public void setSelected(boolean b)
  {
    selected = b;
  }
  
  public void step(float dt)
  {
    if (inTransit)
    {
      time += dt;
      if (time > 1)
      {
        tile = destTile;
        inTransit = false;
        destTile = null;
      }
    }
  }

  public void draw(SpriteBatch sb, Camera cam)
  {
    Vector2 pos = null;
    if (inTransit && destTile != null)
    {
      Vector2 src = game.field.getPos(tile);
      Vector2 dst = game.field.getPos(destTile);
      pos = dst.cpy().sub(src).scl(time).add(src);
    }else
      pos = game.field.getPos(tile);
    float width = game.field.height*.7f;
    float height = game.field.width*.6f;
    if (selected)
    {
      sb.setColor(1, 1, 1, 1);
      float added = .8f;
      drawTex(sb, JTactics.assets.glow, width*(1f+added), height*(1f+added), height*added/2f, pos);
    }
    sb.setColor(1, 0, 1, 1);
    drawTex(sb, JTactics.assets.guy, width, height, 0, pos);
  }

  public void drawTex(SpriteBatch sb, Texture t, float width, float height, float offsetY, Vector2 pos)
  {
    sb.draw(t, pos.x+game.field.width/2-width/2, pos.y+game.field.height/2-offsetY, width, height);

  }
}
