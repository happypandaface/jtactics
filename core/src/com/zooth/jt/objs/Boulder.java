package com.zooth.jt.objs;

import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.zooth.jt.game.*;
import java.util.*;
import com.zooth.jt.*;

public class Boulder
{
  public JTTile tile;
  public JTTile destTile;
  public float time;
  public JTGame game;

  public Boulder(int off, int x, int y)
  {
    tile = new JTTile(off, x, y);
  }

  public void setGame(JTGame g)
  {
    game = g;
  }

  public boolean checkAt(JTTile t)
  {
    return tile.check(t);
  }

  public void draw(SpriteBatch sb)
  {
    Vector2 pos = game.field.getPos(tile);
    if (destTile != null)
    {
      Vector2 diff = game.field.getPos(destTile).sub(pos);
      diff.scl(time);
      pos.add(diff);
    }
    sb.setColor(1,1,1,1);
    float width = game.field.height;
    float height = game.field.height;
    pos.add(game.field.width/2f-width/2f, game.field.height*.3f);
    sb.draw(JTactics.assets.boulder, pos.x, pos.y, width, height);
  }
}
