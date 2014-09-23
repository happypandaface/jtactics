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

public class BoulderPush extends Action
{
  public Boulder tarBoulder;
  public JTTile destTile;
  public boolean canCast(Guy g)
  {
    int dir = guy.tile.getDirection(tarBoulder.tile);
    List<JTTile> tilesInDir = guy.tile.direction(dir, 2);
    destTile = tilesInDir.get(tilesInDir.size()-1);
    boolean isOkayTile = game.checkIsPathable(destTile);
    return g.hasAp(2) && isOkayTile;
  }
  public void selected(Guy g)
  {
    g.useAp(2);
    g.setupAction();
    // find the destination for the boulder
    int dir = guy.tile.getDirection(tarBoulder.tile);
    List<JTTile> tilesInDir = guy.tile.direction(dir, 2);
    destTile = tilesInDir.get(tilesInDir.size()-1);
    tarBoulder.destTile = destTile;
  }
  public void startAction(Guy g)
  {
  }
  
  public Vector2 step(float time)
  {
    Vector2 src = guy.game.field.getPos(guy.tile);
    Vector2 pos = src.cpy();
    // pass along values so that
    // the boulder can calc and
    // draw itself
    tarBoulder.time = time;
    return pos;
  }
  
  public void end()
  {
    tarBoulder.tile = destTile.copy();
    Guy guyAt = game.guyAt(tarBoulder.tile);
    if (guyAt != null)
    {
      guyAt.getDamaged(guy, 5);
    }
    guy.shutdownAction();
  }
}
