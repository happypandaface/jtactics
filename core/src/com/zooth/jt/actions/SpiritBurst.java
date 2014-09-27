package com.zooth.jt.actions;

import com.badlogic.gdx.math.*;
import java.util.*;
import com.zooth.jt.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.zooth.jt.projectiles.*;
import com.zooth.jt.objs.*;

public class SpiritBurst extends Action
{
  Guy tar;
  public boolean canCast(Guy g)
  {
    return g.hasAp(2);
  }
  public void selected(Guy g)
  {
    guy = g;
    guy.pickingLocation = !guy.pickingLocation;
  }
  public void startAction(Guy g)
  {
    guy = g;
    tar = guy.game.guyAt(dirTile);
    if (tar != null && tar.controller == guy.controller)
    {
      guy.pickingLocation = false;
      guy.useAp(2);
      guy.setupAction();
    }
  }
  
  public Vector2 step(float time)
  {
    Vector2 src = guy.game.field.getPos(guy.tile);
    Vector2 tarSrc = guy.game.field.getPos(tar.tile);
    Vector2 pos = src.cpy();
    Vector2 tarPos = tarSrc.cpy();
    int[] dirs = 
    {
      JTTile.Direction.N,
      JTTile.Direction.NE,
      JTTile.Direction.SE,
      JTTile.Direction.S,
      JTTile.Direction.SW,
      JTTile.Direction.SE,
      JTTile.Direction.NW
    };
    for (int i = 0; i < dirs.length; ++i)
    {
      List<JTTile> dirTiles = tar.tile.direction(dirs[i], 1);
      Vector2 dst = guy.game.field.getPos(dirTiles.get(1));
      Fireball fb = new Fireball();
      float delay = .3f;
      fb.pos = dst.cpy().sub(tarPos).scl(time<delay?0:(float)Math.sin((time-delay)/(1-delay)*Math.PI/2f)).add(tarPos);
      fb.tail = dst.cpy().sub(tarPos).scl(time<delay||time>(1-delay)?(delay)*(float)Math.sin(time*Math.PI):(delay)*(float)Math.sin((delay)*Math.PI));
      guy.game.addProj(fb);

    }
    return pos;
  }
  
  public void end()
  {
    List<JTTile> tiles = tar.getAdjTiles(1);
    tar.getDamaged(guy, 1);
    for (int i = 0; i < tiles.size(); ++i)
    {
      Guy hit = guy.game.guyAt(tiles.get(i));
      if (hit != null)
      {
        hit.getDamaged(guy, 1);
      }
    }
    guy.shutdownAction();
  }
}
